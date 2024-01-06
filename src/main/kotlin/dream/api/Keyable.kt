package dream.api

import dream.Key

/**
 * Represents an object that's have a [Key].
 */
interface Keyable {

  /**
   * The key.
   */
  val key: Key
}
