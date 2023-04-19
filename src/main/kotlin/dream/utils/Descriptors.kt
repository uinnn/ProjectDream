@file:Suppress("NOTHING_TO_INLINE")

package dream.utils

import dream.serializer.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*

inline fun StringDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.STRING)
inline fun BooleanDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.BOOLEAN)
inline fun CharDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.CHAR)
inline fun ByteDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.BYTE)
inline fun ShortDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.SHORT)
inline fun IntDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.INT)
inline fun LongDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.LONG)
inline fun FloatDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.FLOAT)
inline fun DoubleDesc(name: String) = PrimitiveSerialDescriptor(name, PrimitiveKind.DOUBLE)

/**
 * Build a new class descriptor with [block].
 */
public fun desc(
   name: String,
   vararg typeParameters: SerialDescriptor,
   block: ClassSerialDescriptorBuilder.() -> Unit,
): SerialDescriptor = buildClassSerialDescriptor(name, *typeParameters, builderAction = block)

/**
 * Adds an element with the given [name] with the given [serializer].
 */
public fun <T> ClassSerialDescriptorBuilder.element(
   name: String,
   serializer: KSerializer<T>,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element(name, serializer.descriptor, annotations, isOptional)

/**
 * A string version of [element].
 */
public fun ClassSerialDescriptorBuilder.string(
   name: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<String>(name, annotations, isOptional)

/**
 * A char version of [element].
 */
public fun ClassSerialDescriptorBuilder.char(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<Char>(elementName, annotations, isOptional)

/**
 * A boolean version of [element].
 */
public fun ClassSerialDescriptorBuilder.boolean(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<Boolean>(elementName, annotations, isOptional)

/**
 * A byte version of [element].
 */
public fun ClassSerialDescriptorBuilder.byte(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<Byte>(elementName, annotations, isOptional)

/**
 * A short version of [element].
 */
public fun ClassSerialDescriptorBuilder.short(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<Short>(elementName, annotations, isOptional)

/**
 * An int version of [element].
 */
public fun ClassSerialDescriptorBuilder.int(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<Int>(elementName, annotations, isOptional)

/**
 * A long version of [element].
 */
public fun ClassSerialDescriptorBuilder.long(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<Long>(elementName, annotations, isOptional)

/**
 * A float version of [element].
 */
public fun ClassSerialDescriptorBuilder.float(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<Float>(elementName, annotations, isOptional)

/**
 * A double version of [element].
 */
public fun ClassSerialDescriptorBuilder.double(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<Double>(elementName, annotations, isOptional)

/**
 * An enum version of [element].
 */
public fun ClassSerialDescriptorBuilder.enum(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element<String>(elementName, annotations, isOptional)

/**
 * An uuid version of [element].
 */
public fun ClassSerialDescriptorBuilder.uuid(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element(elementName, UUIDSerializer, annotations, isOptional)

/**
 * An nullable uuid version of [element].
 */
public fun ClassSerialDescriptorBuilder.nullableUUID(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element(elementName, NullableUUIDSerializer, annotations, isOptional)

/**
 * A key version of [element].
 */
public fun ClassSerialDescriptorBuilder.key(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element(elementName, KeySerializer, annotations, isOptional)

/**
 * A pos version of [element].
 */
public fun ClassSerialDescriptorBuilder.pos(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element(elementName, PosSerializer, annotations, isOptional)

/**
 * A mutable pos version of [element].
 */
public fun ClassSerialDescriptorBuilder.mutablePos(
   elementName: String,
   annotations: List<Annotation> = emptyList(),
   isOptional: Boolean = false,
) = element(elementName, MutablePosSerializer, annotations, isOptional)




