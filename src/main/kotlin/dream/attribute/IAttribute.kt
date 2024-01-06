package dream.attribute

import java.util.*

/**
 * Interface representing an attribute instance.
 *
 * The [IAttribute] interface defines properties and methods for managing an instance of an attribute.
 */
interface IAttribute {
  
  /**
   * The type associated with this attribute.
   */
  val type: AttributeType
  
  /**
   * The base value of the attribute.
   */
  var baseValue: Double
  
  /**
   * A collection of all attribute modifiers applied to this instance.
   */
  val allModifiers: MutableSet<AttributeModifier>
  
  /**
   * The calculated value of the attribute, taking into account the base value and applied modifiers.
   */
  var value: Double
  
  /**
   * Retrieves the modifiers for the specified name.
   *
   * @param name The name for which to retrieve the modifiers.
   * @return A collection of attribute modifiers for the specified operation.
   */
  fun getModifiers(name: String): MutableSet<AttributeModifier>
  
  /**
   * Retrieves the modifiers for the specified operation.
   *
   * @param operation The operation for which to retrieve the modifiers.
   * @return A collection of attribute modifiers for the specified operation.
   */
  fun getModifiers(operation: Operation): MutableSet<AttributeModifier>
  
  /**
   * Checks if the specified modifier is applied to this instance.
   *
   * @param modifier The modifier to check.
   * @return `true` if the modifier is applied, `false` otherwise.
   */
  fun hasModifier(modifier: AttributeModifier): Boolean
  
  /**
   * Retrieves the modifier with the specified UUID.
   *
   * @param id The UUID of the modifier to retrieve.
   * @return The attribute modifier with the specified UUID, or `null` if not found.
   */
  fun getModifier(id: UUID): AttributeModifier?
  
  /**
   * Applies the specified modifier to this instance.
   *
   * @param modifier The modifier to apply.
   */
  fun applyModifier(modifier: AttributeModifier)
  
  /**
   * Removes the specified modifier from this instance.
   *
   * @param modifier The modifier to remove.
   */
  fun removeModifier(modifier: AttributeModifier)
  
  /**
   * Removes all modifiers from this instance.
   */
  fun removeAllModifiers()
  
  /**
   * Marks the attribute as dirty, indicating that it needs to be updated.
   */
  fun markDirty()
}
