package dream.attribute

import com.google.common.collect.*
import dream.collections.*
import dream.misc.*
import kotlin.collections.set

/**
 * Abstract class representing an attribute map.
 *
 * The [AttributeMap] class provides a base implementation for managing attributes and their instances.
 */
@Open
abstract class AttributeMap : Iterable<IAttribute> {
  
  /**
   * Mutable map storing attribute instances indexed by their corresponding attributes.
   */
  val attributes: MutableMap<AttributeType, IAttribute> = HashMap()
  
  /**
   * Mutable map storing attribute instances indexed by their unlocalized names.
   */
  val lookup: MutableMap<String, IAttribute> = LowerCaseMap()
  
  /**
   * Multimap storing child-parent relationships between attributes.
   */
  val childrens: Multimap<AttributeType, AttributeType> = HashMultimap.create()
  
  /**
   * Collection of all attribute instances in the map.
   */
  val allAttributes get() = attributes.values
  
  /**
   * Retrieves the attribute instance associated with the specified attribute.
   *
   * @param type The attribute for which to retrieve the instance.
   * @return The attribute instance associated with the specified attribute, or `null` if not found.
   */
  operator fun get(type: AttributeType): IAttribute? {
    return attributes[type]
  }
  
  /**
   * Finds the attribute instance with the specified unlocalized name.
   *
   * @param name The unlocalized name of the attribute.
   * @return The attribute instance with the specified unlocalized name, or `null` if not found.
   */
  fun find(name: String): IAttribute? {
    return lookup[name]
  }
  
  /**
   * Registers an attribute and creates an associated attribute instance.
   *
   * @param type The attribute to register.
   * @return The created attribute instance.
   * @throws IllegalArgumentException if the attribute is already registered.
   */
  fun registerType(type: AttributeType): IAttribute {
    require(type !in attributes) { "Attribute is already registered!" }
    
    val instance = createAttribute(type)
    lookup[type.unlocalName] = instance
    attributes[type] = instance
    
    var children = type.children
    while (children != null) {
      childrens.put(children, type)
      children = children.children
    }
    
    return instance
  }
  
  /**
   * Creates an attribute instance for the specified attribute.
   *
   * @param type The attribute for which to create an instance.
   * @return The created attribute instance.
   */
  abstract fun createAttribute(type: AttributeType): IAttribute
  
  /**
   * Updates the specified attribute instance.
   *
   * @param attribute The attribute instance to update.
   */
  fun updateAttribute(attribute: IAttribute) {
  }
  
  /**
   * Removes the specified modifiers from the attribute instances.
   *
   * @param modifiers The modifiers to remove, indexed by attribute unlocalized names.
   */
  fun removeModifiers(modifiers: Multimap<String, AttributeModifier>) {
    modifiers.forEach { key, value -> find(key)?.removeModifier(value) }
  }
  
  /**
   * Applies the specified modifiers to the attribute instances.
   *
   * @param modifiers The modifiers to apply, indexed by attribute unlocalized names.
   */
  fun applyModifiers(modifiers: Multimap<String, AttributeModifier>) {
    modifiers.forEach { key, value ->
      val instance = find(key) ?: return@forEach
      instance.removeModifier(value)
      instance.applyModifier(value)
    }
  }
  
  override fun iterator(): Iterator<IAttribute> {
    return allAttributes.iterator()
  }
}
