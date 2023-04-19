package dream.misc

import dream.serializer.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.*

/**
 * Represents a [CompositeEncoder] wrapped with a [SerialDescriptor].
 *
 * Since this already have a default descriptor,
 * this means that is not more necessary to use:
 * ```
 * // with CompositeEncoder
 * decoder.encodeStringElement(description, index, value)
 *
 * // with WrappedCompositeEncoder
 * decoder.encodeString(value) // encodes in next index
 * ```
 */
class WrappedCompositeEncoder(
   delegate: CompositeEncoder,
   val descriptor: SerialDescriptor,
) : CompositeEncoder by delegate {
   
   var currentIndex = 0
   
   fun incIndex() = currentIndex++
   
   fun encodeString(value: String, index: Int = currentIndex) {
      encodeStringElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeInt(value: Int, index: Int = currentIndex) {
      encodeIntElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeByte(value: Byte, index: Int = currentIndex) {
      encodeByteElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeShort(value: Short, index: Int = currentIndex) {
      encodeShortElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeLong(value: Long, index: Int = currentIndex) {
      encodeLongElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeDouble(value: Double, index: Int = currentIndex) {
      encodeDoubleElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeFloat(value: Float, index: Int = currentIndex) {
      encodeFloatElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeInline(index: Int = currentIndex) {
      encodeInlineElement(descriptor, index)
      incIndex()
   }
   
   fun encodeChar(value: Char, index: Int = currentIndex) {
      encodeCharElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeBoolean(value: Boolean, index: Int = currentIndex) {
      encodeBooleanElement(descriptor, index, value)
      incIndex()
   }
   
   fun encodeEnum(value: Enum<*>, index: Int = currentIndex) {
      encodeString(value.name, index)
   }
   
   fun encodeUUID(value: UUID, index: Int = currentIndex) {
      encode(value, UUIDSerializer, index)
   }
   
   fun encodeNullableUUID(value: UUID?, index: Int = currentIndex) {
      encodeNullable(value, NullableUUIDSerializer, index)
   }
   
   inline fun <reified T> encode(
      value: T,
      serializer: SerializationStrategy<T> = serializer(),
      index: Int = currentIndex,
   ) {
      encodeSerializableElement(descriptor, index, serializer, value)
      currentIndex++
   }
   
   inline fun <reified T> encodeNullable(
      value: T?,
      serializer: SerializationStrategy<T> = serializer(),
      index: Int = currentIndex,
   ) {
      encodeNullableSerializableElement(descriptor, index, serializer, value)
      currentIndex++
   }
}

/**
 * Starts the process for encoding data in a [WrappedCompositeEncoder].
 */
inline fun Encoder.encode(descriptor: SerialDescriptor, block: WrappedCompositeEncoder.() -> Unit) {
   val composite = WrappedCompositeEncoder(beginStructure(descriptor), descriptor)
   var ex: Throwable? = null
   try {
      return composite.block()
   } catch (e: Throwable) {
      ex = e
      throw e
   } finally {
      if (ex == null) composite.endStructure(descriptor)
   }
}
