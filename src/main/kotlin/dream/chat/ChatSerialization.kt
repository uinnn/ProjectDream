package dream.chat

import kotlinx.serialization.json.*

/**
 * The JSON format used in chat serialization.
 */
@JvmField
public val ChatJson = Json {
   encodeDefaults = true
   allowStructuredMapKeys = true
}
