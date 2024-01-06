package dream.serializer

import dream.chat.*
import dream.misc.*
import dream.utils.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.*

/**
 * A serializer for [ClickEvent].
 */
object ClickEventSerializer : KSerializer<ClickEvent> {
  override val descriptor = desc("ClickEvent") {
    string("value")
    string("action")
  }

  override fun deserialize(decoder: Decoder) = decoder.decode(descriptor) {
    ClickEvent(ClickAction.byId(decodeString()), decodeString())
  }

  override fun serialize(encoder: Encoder, value: ClickEvent) = encoder.encode(descriptor) {
    encodeString(value.action.id)
    encodeString(value.value)
  }
}

/**
 * A serializer for [HoverEvent].
 */
object HoverEventSerializer : KSerializer<HoverEvent> {
  override val descriptor = desc("HoverEvent") {
    string("value")
    string("action")
  }

  override fun deserialize(decoder: Decoder) = decoder.decode(descriptor) {
    HoverEvent(HoverAction.byId(decodeString()), decodeString())
  }

  override fun serialize(encoder: Encoder, value: HoverEvent) = encoder.encode(descriptor) {
    encodeString(value.action.id)
    encodeString(value.value)
  }
}
