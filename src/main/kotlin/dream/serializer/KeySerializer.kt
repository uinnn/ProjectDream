package dream.serializer

import dream.*
import dream.utils.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*

/**
 * A serializer for [Key].
 */
object KeySerializer : KSerializer<Key> {
   override val descriptor = StringDesc("Key")
   override fun deserialize(decoder: Decoder) = Key.parse(decoder.decodeString())
   override fun serialize(encoder: Encoder, value: Key) = encoder.encodeString(value.toString())
}
