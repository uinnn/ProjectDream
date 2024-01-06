package dream.attribute

import dream.collections.*

/**
 * Class representing a simple attribute map.
 *
 * A simple attribute map stores and manages attributes.
 */
class SimpleAttributeMap : AttributeMap() {
  
  /**
   * Map storing attributes by their description (case-insensitive).
   */
  val byDescription: MutableMap<String, IAttribute> = LowerCaseMap()
  
  /**
   * Set of watchable attributes.
   */
  val watchableAttributes: MutableSet<IAttribute> = HashSet()
  
  /**
   * Set of watched attributes.
   */
  val watchedAttributes
    get() = allAttributes.filterTo(HashSet()) { it.type.shouldWatch }
  
  /**
   * Finds an attribute by its name or description.
   *
   * @param name The name or description of the attribute.
   * @return The attribute with the specified name or description, or `null` if not found.
   */
  override fun find(name: String): IAttribute? {
    return super.find(name) ?: byDescription[name]
  }
  
  /**
   * Registers a new attribute type and creates an attribute instance.
   *
   * @param type The attribute type to register.
   * @return The created attribute instance.
   */
  override fun registerType(type: AttributeType): IAttribute {
    val attribute = super.registerType(type)
    
    if (type is RangedAttributeType && type.description.isNotEmpty()) {
      byDescription[type.description] = attribute
    }
    
    return attribute
  }
  
  /**
   * Creates an attribute instance of the specified type.
   *
   * @param type The attribute type.
   * @return The created attribute instance.
   */
  override fun createAttribute(type: AttributeType): IAttribute {
    return Attribute(this, type)
  }
  
  /**
   * Updates the specified attribute and its children.
   *
   * @param attribute The attribute to update.
   */
  override fun updateAttribute(attribute: IAttribute) {
    if (attribute.type.shouldWatch) {
      watchableAttributes.add(attribute)
    }
    
    childrens[attribute.type].forEach { get(it)?.markDirty() }
  }
}
