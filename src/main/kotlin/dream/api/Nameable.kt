package dream.api

import dream.chat.*

/**
 * Represents an object that's can have a name.
 */
interface Nameable {

  /**
   * The name of this object.
   */
  val name: String

  /**
   * Returns if this object has a name.
   */
  val hasName: Boolean
    get() = name.isNotBlank()

  /**
   * Returns the display name of this object.
   */
  val displayName: Component
    get() = text(name)
}
