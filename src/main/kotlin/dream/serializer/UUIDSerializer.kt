package dream.serializer

import dream.utils.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.*
import java.util.*

/**
 * A serializer for [uuid].
 */
object UUIDSerializer : KSerializer<UUID> {
  override val descriptor = StringDesc("UUID")
  override fun deserialize(decoder: Decoder) = uuid(decoder.decodeString())
  override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
}

/**
 * A nullable serializer for [uuid].
 */
object NullableUUIDSerializer : KSerializer<UUID?> {
  override val descriptor = StringDesc("UUID")

  override fun deserialize(decoder: Decoder): UUID? {
    val data = decoder.decodeString()
    return if (data == "") null else UUID.fromString(data)
  }

  override fun serialize(encoder: Encoder, value: UUID?) {
    encoder.encodeString(value?.toString() ?: "")
  }
}
