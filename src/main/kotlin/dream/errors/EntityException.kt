package dream.errors

/**
 * Represents a base exception for entities.
 */
open class EntityException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Represents a create exception.
 *
 * Throwed when [EntityType] tries to create a new entity and fails.
 */
class EntityCreateException(message: String, cause: Throwable? = null) : Exception(message, cause)
