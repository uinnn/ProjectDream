package dream.serializer

import dream.collision.AABB
import dream.misc.*
import dream.utils.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.*

/**
 * A serializer for [AABB].
 */
object AABBSerializer : KSerializer<AABB> {
  override val descriptor = desc("AABB") {
    pos("min")
    pos("max")
  }

  override fun deserialize(decoder: Decoder) = decoder.decode(descriptor) {
    AABB(decode(PosSerializer), decode(PosSerializer))
  }

  override fun serialize(encoder: Encoder, value: AABB) = encoder.encode(descriptor) {
    encode(value.min, PosSerializer)
    encode(value.max, PosSerializer)
  }
}
