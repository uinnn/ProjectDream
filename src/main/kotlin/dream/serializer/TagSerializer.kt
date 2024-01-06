package dream.serializer

import dream.nbt.*
import dream.nbt.types.*
import dream.utils.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.*
import kotlinx.serialization.encoding.*

/**
 * A serializer for [ByteTag].
 */
object ByteTagSerializer : KSerializer<ByteTag> {
  override val descriptor = ByteDesc("ByteTag")
  override fun deserialize(decoder: Decoder) = decoder.decodeByte().toTag()
  override fun serialize(encoder: Encoder, value: ByteTag) = encoder.encodeByte(value.value)
}

/**
 * A serializer for [ShortTag].
 */
object ShortTagSerializer : KSerializer<ShortTag> {
  override val descriptor = ShortDesc("ShortTag")
  override fun deserialize(decoder: Decoder) = decoder.decodeShort().toTag()
  override fun serialize(encoder: Encoder, value: ShortTag) = encoder.encodeShort(value.value)
}

/**
 * A serializer for [IntTag].
 */
object IntTagSerializer : KSerializer<IntTag> {
  override val descriptor = IntDesc("IntTag")
  override fun deserialize(decoder: Decoder) = decoder.decodeInt().toTag()
  override fun serialize(encoder: Encoder, value: IntTag) = encoder.encodeInt(value.value)
}

/**
 * A serializer for [LongTag].
 */
object LongTagSerializer : KSerializer<LongTag> {
  override val descriptor = LongDesc("LongTag")
  override fun deserialize(decoder: Decoder) = decoder.decodeLong().toTag()
  override fun serialize(encoder: Encoder, value: LongTag) = encoder.encodeLong(value.value)
}

/**
 * A serializer for [FloatTag].
 */
object FloatTagSerializer : KSerializer<FloatTag> {
  override val descriptor = FloatDesc("FloatTag")
  override fun deserialize(decoder: Decoder) = decoder.decodeFloat().toTag()
  override fun serialize(encoder: Encoder, value: FloatTag) = encoder.encodeFloat(value.value)
}

/**
 * A serializer for [DoubleTag].
 */
object DoubleTagSerializer : KSerializer<DoubleTag> {
  override val descriptor = DoubleDesc("DoubleTag")
  override fun deserialize(decoder: Decoder) = decoder.decodeDouble().toTag()
  override fun serialize(encoder: Encoder, value: DoubleTag) = encoder.encodeDouble(value.value)
}

/**
 * A serializer for [StringTag].
 */
object StringTagSerializer : KSerializer<StringTag> {
  override val descriptor = StringDesc("StringTag")
  override fun deserialize(decoder: Decoder) = decoder.decodeString().toTag()
  override fun serialize(encoder: Encoder, value: StringTag) = encoder.encodeString(value.value)
}

/**
 * A serializer for [ByteArrayTag].
 */
object ByteArrayTagSerializer : KSerializer<ByteArrayTag> {
  val serializer = ByteArraySerializer()

  override val descriptor = serializer.descriptor
  override fun deserialize(decoder: Decoder) = serializer.deserialize(decoder).toTag()
  override fun serialize(encoder: Encoder, value: ByteArrayTag) = serializer.serialize(encoder, value.value)
}

/**
 * A serializer for [IntArrayTag].
 */
object IntArrayTagSerializer : KSerializer<IntArrayTag> {
  val serializer = IntArraySerializer()

  override val descriptor = serializer.descriptor
  override fun deserialize(decoder: Decoder) = serializer.deserialize(decoder).toTag()
  override fun serialize(encoder: Encoder, value: IntArrayTag) = serializer.serialize(encoder, value.value)
}

/**
 * A serializer for [ListTag].
 */
object ListTagSerializer : KSerializer<ListTag<out Tag>> {
  val serializer = ByteArraySerializer()

  override val descriptor = serializer.descriptor

  override fun deserialize(decoder: Decoder): ListTag<out Tag> {
    return serializer.deserialize(decoder).decodeTag().cast()
  }

  override fun serialize(encoder: Encoder, value: ListTag<out Tag>) {
    serializer.serialize(encoder, value.toBytes())
  }
}

/**
 * A serializer for [CompoundTag].
 */
object CompoundTagSerializer : KSerializer<CompoundTag> {
  val serializer = ByteArraySerializer()

  override val descriptor = serializer.descriptor

  override fun deserialize(decoder: Decoder): CompoundTag {
    return serializer.deserialize(decoder).decodeCompound()
  }

  override fun serialize(encoder: Encoder, value: CompoundTag) {
    serializer.serialize(encoder, value.toBytes())
  }
}

/**
 * A serializer for [EmptyTag].
 */
object EmptyTagSerializer : KSerializer<EmptyTag> {
  override val descriptor = StringDesc("EmptyTag")
  override fun deserialize(decoder: Decoder) = EmptyTag
  override fun serialize(encoder: Encoder, value: EmptyTag) = encoder.encodeString("EmptyTag")
}
