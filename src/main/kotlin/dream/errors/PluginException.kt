package dream.errors

/**
 * Represents a base plugin exception.
 */
open class PluginException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Module not found exception.
 */
class ModuleNotFoundException(message: String, cause: Throwable? = null) : PluginException(message, cause)
