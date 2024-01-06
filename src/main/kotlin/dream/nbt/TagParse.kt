package dream.nbt

/**
 * A parser for tags.
 */
interface TagParse<T : Tag> {

  /**
   * Parses type creating a new tag by [str] or null.
   */
  fun parseOrNull(str: String): T?

  /**
   * Parses type creating a new tag by [str].
   */
  fun parse(str: String): T

  /**
   * Gets if [str] matches to be parsed.
   */
  fun matches(str: String): Boolean
}
