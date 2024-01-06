package dream.attribute

import dream.utils.*
import java.util.*

/**
 * Class representing an attribute.
 *
 * An attribute is a value associated with a specific type and is part of an attribute map.
 *
 * @param map The attribute map that this attribute belongs to.
 * @param type The type of the attribute.
 */
class Attribute(val map: AttributeMap, override val type: AttributeType) : IAttribute {
  
  /**
   * Map storing the attribute modifiers by operation.
   */
  val byOperation = enumMap<Operation, MutableSet<AttributeModifier>>().apply {
    Operation.entries.forEach { put(it, HashSet()) }
  }
  
  /**
   * Map storing the attribute modifiers by name.
   */
  val byName: MutableMap<String, MutableSet<AttributeModifier>> = HashMap()
  
  /**
   * Map storing the attribute modifiers by UUID.
   */
  val byId: MutableMap<UUID, AttributeModifier> = HashMap()
  
  /**
   * The base value of the attribute.
   */
  override var baseValue = type.defaultValue
    set(value) {
      if (field != value) {
        field = value
        markDirty()
      }
    }
  
  /**
   * The current value of the attribute.
   */
  override var value: Double = 0.0
    get() {
      if (needsUpdate) {
        field = computeValue()
        needsUpdate = false
      }
      
      return field
    }
  
  /**
   * Flag indicating whether the attribute needs to be updated.
   */
  var needsUpdate = true
  
  /**
   * Collection of all attribute modifiers associated with this attribute.
   */
  override val allModifiers
    get() = Operation.entries.flatMapTo(HashSet(), ::getModifiers)
  
  /**
   * Retrieves the attribute modifiers with the specified name.
   *
   * @param name The name of the attribute modifiers.
   * @return The attribute modifiers with the specified name.
   */
  override fun getModifiers(name: String): MutableSet<AttributeModifier> {
    return byName.computeIfAbsent(name) { HashSet() }
  }
  
  /**
   * Retrieves the attribute modifiers with the specified operation.
   *
   * @param operation The operation of the attribute modifiers.
   * @return The attribute modifiers with the specified operation.
   */
  override fun getModifiers(operation: Operation): MutableSet<AttributeModifier> {
    return byOperation[operation]!!
  }
  
  /**
   * Checks if the attribute has the specified modifier.
   *
   * @param modifier The attribute modifier to check.
   * @return `true` if the attribute has the modifier, `false` otherwise.
   */
  override fun hasModifier(modifier: AttributeModifier): Boolean {
    return modifier.id in byId
  }
  
  /**
   * Retrieves the attribute modifier with the specified UUID.
   *
   * @param id The UUID of the attribute modifier.
   * @return The attribute modifier with the specified UUID, or `null` if not found.
   */
  override fun getModifier(id: UUID): AttributeModifier? {
    return byId[id]
  }
  
  /**
   * Applies the specified attribute modifier to the attribute.
   *
   * @param modifier The attribute modifier to apply.
   */
  override fun applyModifier(modifier: AttributeModifier) {
    if (!hasModifier(modifier)) {
      getModifiers(modifier.name).add(modifier)
      getModifiers(modifier.operation).add(modifier)
      byId[modifier.id] = modifier
      markDirty()
    }
  }
  
  /**
   * Removes the specified attribute modifier from the attribute.
   *
   * @param modifier The attribute modifier to remove.
   */
  override fun removeModifier(modifier: AttributeModifier) {
    byOperation.values.forEach { it.remove(modifier) }
    byName[modifier.name]?.let {
      it.remove(modifier)
      if (it.isEmpty()) byName -= modifier.name
    }
    byId -= modifier.id
    markDirty()
  }
  
  /**
   * Removes all attribute modifiers from the attribute.
   */
  override fun removeAllModifiers() {
    allModifiers.forEach(::removeModifier)
  }
  
  /**
   * Marks the attribute as dirty, indicating that it needs to be updated.
   */
  override fun markDirty() {
    needsUpdate = true
    map.updateAttribute(this)
  }
  
  /**
   * Computes the current value of the attribute based on its base value and associated modifiers.
   *
   * @return The computed value of the attribute.
   */
  fun computeValue(): Double {
    var base = baseValue
    for (modifier in childrenModifiers(Operation.SUM)) {
      base += modifier.amount
    }
    
    var value = base
    for (modifier in childrenModifiers(Operation.PERCENT)) {
      value += base * modifier.amount
    }
    
    for (modifier in childrenModifiers(Operation.MULTIPLIER)) {
      value *= 1.0 + modifier.amount
    }
    
    return type.clampValue(value)
  }
  
  /**
   * Retrieves the attribute modifiers associated with the specified operation from the attribute's children.
   *
   * @param operation The operation of the attribute modifiers.
   * @return The attribute modifiers associated with the specified operation from the attribute's children.
   */
  fun childrenModifiers(operation: Operation): Set<AttributeModifier> {
    val modifiers = getModifiers(operation).toMutableSet()
    
    var children = type.children
    while (children != null) {
      val attribute = map[type]
      if (attribute != null) {
        modifiers.addAll(attribute.getModifiers(operation))
      }
      
      children = children.children
    }
    
    return modifiers
  }
}
