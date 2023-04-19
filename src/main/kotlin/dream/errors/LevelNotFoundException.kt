package dream.errors

/**
 * Represents a world not found error.
 *
 * This is thrown when a try of getting a world by name or id is called
 * and the server don't have/knows the world.
 */
class LevelNotFoundException(message: String, cause: Throwable? = null) : Exception(message, cause)
