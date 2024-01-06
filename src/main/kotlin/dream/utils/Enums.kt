package dream.utils

/**
 * Gets a mask for this enum.
 */
inline val Enum<*>.mask: Int
  get() = 1 shl ordinal
