@file:Suppress("NOTHING_TO_INLINE")

package dream.utils

import dream.nbt.*
import dream.nbt.types.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

inline fun Byte.toTag() = ByteTag.of(this)
inline fun Boolean.toTag() = ByteTag.of(this)
inline fun Short.toTag() = ShortTag.of(this)
inline fun Int.toTag() = IntTag.of(this)
inline fun Long.toTag() = LongTag.of(this)
inline fun Float.toTag() = FloatTag.of(this)
inline fun Double.toTag() = DoubleTag.of(this)
inline fun CharSequence.toTag() = StringTag.of(toString())
inline fun ByteArray.toTag() = ByteArrayTag(this)
inline fun IntArray.toTag() = IntArrayTag(this)

fun CollectionTag<ByteTag>.getByte(index: Int) = get(index).value
fun CollectionTag<ShortTag>.getShort(index: Int) = get(index).value
fun CollectionTag<IntTag>.getInt(index: Int) = get(index).value
fun CollectionTag<LongTag>.getLong(index: Int) = get(index).value
fun CollectionTag<FloatTag>.getFloat(index: Int) = get(index).value
fun CollectionTag<DoubleTag>.getDouble(index: Int) = get(index).value
fun CollectionTag<StringTag>.getString(index: Int) = get(index).value
fun CollectionTag<ByteArrayTag>.getArray(index: Int) = get(index).value
fun CollectionTag<IntArrayTag>.getArray(index: Int) = get(index).value

fun CollectionTag<ByteTag>.add(value: Byte) = add(value.toTag())
fun CollectionTag<ShortTag>.add(value: Short) = add(value.toTag())
fun CollectionTag<IntTag>.add(value: Int) = add(value.toTag())
fun CollectionTag<LongTag>.add(value: Long) = add(value.toTag())
fun CollectionTag<FloatTag>.add(value: Float) = add(value.toTag())
fun CollectionTag<DoubleTag>.add(value: Double) = add(value.toTag())
fun CollectionTag<StringTag>.add(value: String) = add(value.toTag())
fun CollectionTag<ByteArrayTag>.add(value: ByteArray) = add(value.toTag())
fun CollectionTag<IntArrayTag>.add(value: IntArray) = add(value.toTag())

@JvmName("wrapByteTag")
fun <E> CollectionTag<ByteTag>.wrap(selector: (Byte) -> E) = mapMutable { selector(it.value) }

@JvmName("wrapShortTag")
fun <E> CollectionTag<ShortTag>.wrap(selector: (Short) -> E) = mapMutable { selector(it.value) }

@JvmName("wrapIntTag")
fun <E> CollectionTag<IntTag>.wrap(selector: (Int) -> E) = mapMutable { selector(it.value) }

@JvmName("wrapLongTag")
fun <E> CollectionTag<LongTag>.wrap(selector: (Long) -> E) = mapMutable { selector(it.value) }

@JvmName("wrapFloatTag")
fun <E> CollectionTag<FloatTag>.wrap(selector: (Float) -> E) = mapMutable { selector(it.value) }

@JvmName("wrapDoubleTag")
fun <E> CollectionTag<DoubleTag>.wrap(selector: (Double) -> E) = mapMutable { selector(it.value) }

@JvmName("wrapStringTag")
fun <E> CollectionTag<StringTag>.wrap(selector: (String) -> E) = mapMutable { selector(it.value) }

inline fun <reified T> CollectionTag<StringTag>.wrapJson(json: Json = Json) =
  mapMutable { it.value.parseJson<T>(json) }

fun <T : Any> CollectionTag<StringTag>.wrapJson(deserializer: DeserializationStrategy<T>, json: Json = Json) =
  mapMutable { it.value.parseJson(deserializer, json) }

@JvmName("wrapByteArrayTag")
fun <E> CollectionTag<ByteArrayTag>.wrap(selector: (ByteArray) -> E) = mapMutable { selector(it.value) }

@JvmName("wrapIntArrayTag")
fun <E> CollectionTag<IntArrayTag>.wrap(selector: (IntArray) -> E) = mapMutable { selector(it.value) }

@JvmName("wrapMutableByte")
fun CollectionTag<ByteTag>.wrap() = mapMutable { it.value }

@JvmName("wrapMutableShort")
fun CollectionTag<ShortTag>.wrap() = mapMutable { it.value }

@JvmName("wrapMutableInt")
fun CollectionTag<IntTag>.wrap() = mapMutable { it.value }

@JvmName("wrapMutableLong")
fun CollectionTag<LongTag>.wrap() = mapMutable { it.value }

@JvmName("wrapMutableFloat")
fun CollectionTag<FloatTag>.wrap() = mapMutable { it.value }

@JvmName("wrapMutableDouble")
fun CollectionTag<DoubleTag>.wrap() = mapMutable { it.value }

@JvmName("wrapMutableString")
fun CollectionTag<StringTag>.wrap() = mapMutable { it.value }

@JvmName("wrapMutableByteArray")
fun CollectionTag<ByteArrayTag>.wrap() = mapMutable { it.value }

@JvmName("wrapMutableIntArray")
fun CollectionTag<IntArrayTag>.wrap() = mapMutable { it.value }

inline fun <T, E : Tag> Iterable<T>.toTag(selector: (T) -> E) = mapTo(ListTag(), selector)
fun <T : CompoundStorable> Iterable<T>.elementsToTag() = mapTo(ListTag()) { it.store() }

@JvmName("toTagString")
inline fun <T> Iterable<T>.toTag(selector: (T) -> String) = mapTo(ListTag()) { selector(it).toTag() }

inline fun <reified T> Iterable<T>.toTagAsJson(json: Json = Json) = mapTo(ListTag()) { toJson(it, json).toTag() }

fun <T> Iterable<T>.toTagAsJson(serializer: SerializationStrategy<T>, json: Json = Json) =
  mapTo(ListTag()) { toJson(serializer, it, json).toTag() }

@JvmName("toTagBoolean")
inline fun <T> Iterable<T>.toTag(selector: (T) -> Boolean) = mapTo(ListTag()) { selector(it).toTag() }

@JvmName("toTagByte")
inline fun <T> Iterable<T>.toTag(selector: (T) -> Byte) = mapTo(ListTag()) { selector(it).toTag() }

@JvmName("toTagShort")
inline fun <T> Iterable<T>.toTag(selector: (T) -> Short) = mapTo(ListTag()) { selector(it).toTag() }

@JvmName("toTagInt")
inline fun <T> Iterable<T>.toTag(selector: (T) -> Int) = mapTo(ListTag()) { selector(it).toTag() }

@JvmName("toTagLong")
inline fun <T> Iterable<T>.toTag(selector: (T) -> Long) = mapTo(ListTag()) { selector(it).toTag() }

@JvmName("toTagFloat")
inline fun <T> Iterable<T>.toTag(selector: (T) -> Float) = mapTo(ListTag()) { selector(it).toTag() }

@JvmName("toTagDouble")
inline fun <T> Iterable<T>.toTag(selector: (T) -> Double) = mapTo(ListTag()) { selector(it).toTag() }

@JvmName("toByteTag")
fun Iterable<Byte>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toShortTag")
fun Iterable<Short>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toIntTag")
fun Iterable<Int>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toLongTag")
fun Iterable<Long>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toFloatTag")
fun Iterable<Float>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toDoubleTag")
fun Iterable<Double>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toStringTag")
fun Iterable<String>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toByteTag")
fun Array<Byte>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toShortTag")
fun Array<Short>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toIntTag")
fun Array<Int>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toLongTag")
fun Array<Long>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toFloatTag")
fun Array<Float>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toDoubleTag")
fun Array<Double>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toStringTag")
fun Array<out String>.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toShortTag")
fun ShortArray.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toLongTag")
fun LongArray.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toFloatTag")
fun FloatArray.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("toDoubleTag")
fun DoubleArray.toTag() = mapTo(ListTag()) { it.toTag() }

@JvmName("wrapByteList")
fun <E : Any> CompoundTag.wrapList(key: String, selector: (Byte) -> E): MutableList<E> {
  return listOrNull<ByteTag>(key)?.wrap(selector) ?: ArrayList(0)
}

@JvmName("wrapShortList")
fun <E : Any> CompoundTag.wrapList(key: String, selector: (Short) -> E): MutableList<E> {
  return listOrNull<ShortTag>(key)?.wrap(selector) ?: ArrayList(0)
}

@JvmName("wrapIntList")
fun <E : Any> CompoundTag.wrapList(key: String, selector: (Int) -> E): MutableList<E> {
  return listOrNull<IntTag>(key)?.wrap(selector) ?: ArrayList(0)
}

@JvmName("wrapLongList")
fun <E : Any> CompoundTag.wrapList(key: String, selector: (Long) -> E): MutableList<E> {
  return listOrNull<LongTag>(key)?.wrap(selector) ?: ArrayList(0)
}

@JvmName("wrapFloatList")
fun <E : Any> CompoundTag.wrapList(key: String, selector: (Float) -> E): MutableList<E> {
  return listOrNull<FloatTag>(key)?.wrap(selector) ?: ArrayList(0)
}

@JvmName("wrapDoubleList")
fun <E : Any> CompoundTag.wrapList(key: String, selector: (Double) -> E): MutableList<E> {
  return listOrNull<DoubleTag>(key)?.wrap(selector) ?: ArrayList(0)
}

@JvmName("wrapStringList")
fun <E : Any> CompoundTag.wrapList(key: String, selector: (String) -> E): MutableList<E> {
  return listOrNull<StringTag>(key)?.wrap(selector) ?: ArrayList(0)
}

inline fun <reified T : Any> CompoundTag.wrapJsonList(key: String, json: Json = Json): MutableList<T> {
  return listOrNull<StringTag>(key)?.wrapJson(json) ?: ArrayList(0)
}

fun <T : Any> CompoundTag.wrapJsonList(
  key: String,
  deserializer: DeserializationStrategy<T>,
  json: Json = Json,
): MutableList<T> {
  return listOrNull<StringTag>(key)?.wrapJson(deserializer, json) ?: ArrayList(0)
}
