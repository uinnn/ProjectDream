package dream.errors

/**
 * Represents a base exception for packets.
 */
open class PacketException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * A exception that is thrown if any error is throwed when compressing packets.
 */
class PacketCompressionException(message: String, cause: Throwable? = null) : PacketException(message, cause)
