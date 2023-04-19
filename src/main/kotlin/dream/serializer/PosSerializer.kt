package dream.serializer

import dream.pos.*
import dream.utils.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*

/**
 * Represents a serializer for [Pos].
 */
object PosSerializer : KSerializer<Pos> {
   override val descriptor = LongDesc("Pos")
   
   override fun deserialize(decoder: Decoder): Pos {
      return Pos(decoder.decodeLong())
   }
   
   override fun serialize(encoder: Encoder, value: Pos) {
      encoder.encodeLong(value.asLong())
   }
}

/**
 * Represents a serializer for [MutablePos].
 */
object MutablePosSerializer : KSerializer<MutablePos> {
   override val descriptor = LongDesc("MutablePos")
   
   override fun deserialize(decoder: Decoder): MutablePos {
      return MutablePos(decoder.decodeLong())
   }
   
   override fun serialize(encoder: Encoder, value: MutablePos) {
      encoder.encodeLong(value.asLong())
   }
}
