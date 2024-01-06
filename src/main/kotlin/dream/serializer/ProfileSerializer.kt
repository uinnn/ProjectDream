package dream.serializer

import com.mojang.authlib.GameProfile
import dream.misc.*
import dream.utils.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.*

/**
 * A serializer for [GameProfile].
 */
object ProfileSerializer : KSerializer<GameProfile> {
  override val descriptor = desc("GameProfile") {
    nullableUUID("id")
    string("name")
  }

  override fun deserialize(decoder: Decoder) = decoder.decode(descriptor) {
    GameProfile(decodeNullableUUID(), decodeString())
  }

  override fun serialize(encoder: Encoder, value: GameProfile) = encoder.encode(descriptor) {
    encodeNullableUUID(value.id)
    encodeString(value.name)
  }
}
