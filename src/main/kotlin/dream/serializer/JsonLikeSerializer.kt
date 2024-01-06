package dream.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

abstract class JsonLikeSerializer<T : JsonStorable<*>> : KSerializer<T> {

  companion object {
    @JvmField
    val serializer = MapSerializer(String.serializer(), JsonElement.serializer())
  }

  override val descriptor = createDescriptor(this::class.simpleName ?: "JsonLikeObject")

  fun createDescriptor(name: String): SerialDescriptor {
    return object : SerialDescriptor by serializer.descriptor {
      override val serialName: String = "dream.serializer.$name"
    }
  }

  override fun serialize(encoder: Encoder, value: T) {
    serializer.serialize(encoder, value.toJson())
  }

  override fun deserialize(decoder: Decoder): T {
    return createObject(JsonObject(serializer.deserialize(decoder)))
  }

  abstract fun createObject(data: JsonObject): T
}
