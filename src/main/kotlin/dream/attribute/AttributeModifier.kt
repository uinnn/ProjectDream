package dream.attribute

import dream.api.*
import dream.misc.*
import java.util.*
import java.util.UUID.*

/**
 * Class representing an attribute modifier.
 *
 * The [AttributeModifier] class represents a modifier applied to an attribute,
 * which can change its value based on a specific operation and amount.
 *
 * @param name The name of the modifier.
 * @param amount The amount of the modifier.
 * @param operation The operation to be performed by the modifier.
 * @param id The unique identifier of the modifier. Defaults to a random UUID.
 * @param isStorable Indicates whether the modifier is storable. Defaults to `false`.
 */
@Open
data class AttributeModifier(
  override val name: String,
  var amount: Double,
  var operation: Operation,
  override val id: UUID = randomUUID(),
  var isStorable: Boolean = false,
) : Identifier {

  /**
   * Checks if the modifier is equal to another object.
   *
   * @param other The other object to compare.
   * @return `true` if the modifier is equal to the other object, `false` otherwise.
   */
  override fun equals(other: Any?): Boolean {
    return if (other is AttributeModifier) id == other.id else false
  }

  /**
   * Calculates the hash code of the modifier.
   *
   * @return The calculated hash code.
   */
  override fun hashCode(): Int {
    return id.hashCode()
  }
}
