package dream.serializer

import kotlinx.serialization.json.*

/**
 * A interface for storing [T] values to JSON.
 */
interface JsonStorable<T> {

  /**
   * Serializes this object to [data].
   */
  fun serialize(data: JsonObjectBuilder)

  /**
   * Deserializes this object from [data].
   */
  fun deserialize(data: JsonObject)

  /**
   * Creates a new [JsonObject] based on this object.
   */
  fun toJson(): JsonObject {
    return buildJsonObject {
      serialize(this)
    }
  }
}
