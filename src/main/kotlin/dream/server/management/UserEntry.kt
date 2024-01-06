package dream.server.management

import dream.serializer.JsonStorable

interface UserEntry<T> : JsonStorable<T> {

  /**
   * The value of this entry.
   */
  var value: T?

  /**
   * If this entry have any value.
   */
  val hasValue: Boolean get() = value != null

  /**
   * Checks if this entry have been expired.
   *
   * Default implementation is false.
   */
  val isExpired: Boolean get() = false
}
