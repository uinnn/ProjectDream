package dream.errors

import dream.nbt.types.CompoundTag

/**
 * Base exception for any tag-related exceptions.
 */
open class TagException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Represents a corrupt tag exception. Throwed when reading an invalid NBT file.
 */
class CorruptTagException(message: String, cause: Throwable? = null) : TagException(message, cause)

/**
 * Represents a loading tag exception. Throwed when reading invalid [CompoundTag] entries.
 */
class LoadingTagException(message: String, cause: Throwable? = null) : TagException(message, cause)

/**
 * Represents a SNBT parser exception. Throwed when parsing or converting a NBT to SNBT.
 */
class TagParserException(message: String, cause: Throwable? = null) : TagException(message, cause)

