package dream.nbt

import com.google.common.base.Splitter
import dream.errors.TagParserException
import dream.nbt.types.*
import dream.utils.cast

/**
 * Represents a stringified NBT parser.
 */
object TagParser {
  private val PATTERN = "\\[[-+\\d|,\\s]]".toRegex()
  private val DOUBLE = "[-+]?[0-9]*\\.?[0-9]+[d|D]".toRegex()
  private val FLOAT = "[-+]?[0-9]*\\.?[0-9]+[f|F]".toRegex()
  private val BYTE = "[-+]?[0-9]+[b|B]".toRegex()
  private val LONG = "[-+]?[0-9]+[l|L]".toRegex()
  private val SHORT = "[-+]?[0-9]+[s|S]".toRegex()
  private val INTEGER = "[-+]?[0-9]+".toRegex()
  private val DOUBLE_UNTYPED = "[-+]?[0-9]*\\.?[0-9]+".toRegex()
  private val SPLITTER = Splitter.on(',').omitEmptyStrings()

  /**
   * Throws a [TagParserException] with [message].
   */
  private fun error(message: String): Nothing = throw TagParserException(message)

  /**
   * Parses [string] to a [CompoundTag].
   */
  fun parse(string: String): CompoundTag {
    val trimmed = string.trim()
    return when {
      !trimmed.startsWith('{') -> error("Invalid tag parsing. Expected to get '{' char as first char.")

      else -> CompoundParser(string).parse().cast()
    }
  }

  /**
   * Represents a SNBT parser.
   */
  sealed class Parser(val string: String) {
    abstract fun parse(): Tag
  }

  /**
   * Represents a parser for [CompoundTag].
   */
  class CompoundParser(string: String) : Parser(string) {
    override fun parse(): CompoundTag {
      TODO()
    }
  }

  /**
   * Represents a parser for [ListTag].
   */
  class ListParser(string: String) : Parser(string) {
    override fun parse(): Tag {
      TODO()
    }
  }

  /**
   * Represents a parser for primitive NBT.
   *
   */
  class PrimitiveParser(string: String) : Parser(string) {
    override fun parse(): Tag {
      TODO()
    }
  }
}
