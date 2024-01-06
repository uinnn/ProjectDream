package dream.chat

import kotlinx.serialization.json.*

/**
 * The JSON format used in chat serialization.
 */
@JvmField
val ChatJson = Json {
  encodeDefaults = true
  allowStructuredMapKeys = true
}
