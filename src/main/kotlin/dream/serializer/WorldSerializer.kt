package dream.serializer

import dream.level.*
import dream.utils.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*

/**
 * A serializer for [Level].
 *
 * This serializes the name of the world.
 */
object WorldSerializer : KSerializer<Level> {
   override val descriptor = StringDesc("World")
   override fun deserialize(decoder: Decoder) = TODO("Not yet implemented")
   override fun serialize(encoder: Encoder, value: Level) = encoder.encodeString(value.name)
}

/**
 * A serializer for [Level].
 *
 * This serializes the id of the world.
 */
object WorldIdSerializer : KSerializer<Level> {
   override val descriptor = StringDesc("World")
   override fun deserialize(decoder: Decoder) = TODO("Not yet implemented")
   override fun serialize(encoder: Encoder, value: Level) = encoder.encodeString(value.id.toString())
}
