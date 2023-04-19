@file:Suppress("NOTHING_TO_INLINE")

package dream.nbt.types

import com.soywiz.kmem.*
import com.soywiz.korio.lang.*
import dream.enchantment.*
import dream.errors.*
import dream.item.*
import dream.nbt.*
import dream.nbt.adapter.*
import dream.serializer.*
import dream.utils.*
import kotlinx.serialization.Serializable
import kotlin.contracts.*
import kotlin.experimental.*
import kotlin.reflect.*
import java.io.*
import java.util.*

/**
 * An implementation of [MapTag] as compound.
 */
@Serializable(CompoundTagSerializer::class)
class CompoundTag : MapTag<String, Tag>, java.io.Serializable {
   
   override val type
      get() = Type
   
   constructor()
   constructor(size: Int) : super(size)
   constructor(values: Map<String, Tag>) : super(values)
   constructor(vararg values: Pair<String, Tag>) : super(*values)
   
   override fun writeEntry(key: String, value: Tag, data: ObjectOutput) {
      data.writeByte(value.id)
      if (!value.isEnd) {
         data.writeUTF(key)
         value.write(data)
      }
   }
   
   override fun copy() = CompoundTag(this)
   
   override fun toString(): String = buildString {
      append('{')
      for (entry in this@CompoundTag) {
         if (length != 1) append(", ")
         append(entry.key).append(':').append(entry.value)
      }
      append('}')
   }
   
   @PublishedApi
   internal fun tagNotFound(key: String) {
      error("No NBT element in CompoundTag found with key $key")
   }
   
   /**
    * Gets a type-safe tag [T] in the given [key]
    */
   final inline fun <reified T : Tag> getTag(key: String): T {
      val tag = get(key) ?: tagNotFound(key)
      return tag as T
   }
   
   /**
    * Gets a type-safe tag [T] in the given [key] or returns null
    */
   final inline fun <reified T : Tag> getTagOrNull(key: String): T? {
      return get(key) as? T
   }
   
   /**
    * Gets a tag if existent or puts the given [value].
    */
   final inline fun <reified T : Tag> getOrAdd(key: String, value: (CompoundTag) -> T): T {
      return if (key in this) {
         get(key) as T
      } else {
         val tag = value(this)
         put(key, tag)
         tag
      }
   }
   
   fun has(key: String): Boolean {
      return containsKey(key)
   }
   
   fun has(key: String, id: Int): Boolean {
      return (get(key)?.type ?: return false).id == id.toByte()
   }
   
   fun has(key: String, type: TagType<out Tag>): Boolean {
      return (get(key)?.type ?: return false) === type
   }
   
   fun typeOf(key: String): TagType<out Tag> {
      return get(key)?.type ?: EmptyType
   }
   
   fun typeIdOf(key: String): Int {
      return typeOf(key).id.toInt()
   }
   
   fun isEmptyType(key: String): Boolean {
      return typeOf(key) == EmptyType
   }
   
   fun isEmpty(key: String): Boolean {
      val tag = get(key) ?: return true
      return tag.isEnd
   }
   
   fun isNumber(key: String): Boolean {
      return get(key) is NumberTag
   }
   
   fun isArray(key: String): Boolean {
      return get(key) is ArrayTag
   }
   
   fun isCollection(key: String): Boolean {
      return get(key) is CollectionTag<*>
   }
   
   fun isIterable(key: String): Boolean {
      val value = get(key) ?: return false
      return value is CollectionTag<*> || value is ArrayTag
   }
   
   fun isMap(key: String): Boolean {
      return get(key) is MapTag<*, *>
   }
   
   fun isUUID(key: String): Boolean {
      val data = intArrayOrNull(key) ?: return false
      return data.size == 4
   }
   
   fun getBits(key: String): Int {
      return int(key)
   }
   
   fun setInitialBits(key: String, bits: Int) {
      set(key, bits)
   }
   
   fun setBits(key: String, bits: Int) {
      set(key, int(key).setBits(bits))
   }
   
   fun <T : Enum<T>> setBits(key: String, enum: Enum<T>) {
      set(key, int(key).setBits(enum))
   }
   
   fun <T : Enum<T>> setBits(key: String, vararg enum: Enum<T>) {
      set(key, int(key).setBits(*enum))
   }
   
   fun unsetBits(key: String, bits: Int) {
      val flag = intOrNull(key) ?: return
      set(key, flag.unsetBits(bits))
   }
   
   fun <T : Enum<T>> unsetBits(key: String, enum: Enum<T>) {
      unsetBits(key, enum.mask)
   }
   
   fun <T : Enum<T>> unsetBits(key: String, vararg enum: Enum<T>) {
      set(key, int(key).unsetBits(*enum))
   }
   
   fun hasBits(key: String, bits: Int): Boolean {
      return int(key).hasBits(bits)
   }
   
   fun <T : Enum<T>> hasBits(key: String, enum: Enum<T>): Boolean {
      return int(key).hasBits(enum)
   }
   
   fun <T : Enum<T>> hasBits(key: String, vararg enum: Enum<T>): Boolean {
      return int(key).hasAnyBits(*enum)
   }
   
   fun number(key: String, default: Number = 0): Number {
      val tag = get(key) ?: return default
      return if (tag is NumberTag) tag.toNumber() else default
   }
   
   fun numberOrNull(key: String): Number? {
      val tag = get(key) ?: return null
      return if (tag is NumberTag) tag.toNumber() else null
   }
   
   fun byte(key: String, default: Byte = 0): Byte {
      return (getTagOrNull<ByteTag>(key) ?: return default).value
   }
   
   fun byteOrNull(key: String): Byte? {
      return (getTagOrNull<ByteTag>(key) ?: return null).value
   }
   
   fun boolean(key: String, default: Boolean = false): Boolean {
      val value = byteOrNull(key) ?: return default
      return value == 1.toByte()
   }
   
   fun booleanOrNull(key: String): Boolean? {
      val value = byteOrNull(key) ?: return null
      return value == 1.toByte()
   }
   
   fun short(key: String, default: Short = 0): Short {
      return (getTagOrNull<ShortTag>(key) ?: return default).value
   }
   
   fun shortOrNull(key: String): Short? {
      return (getTagOrNull<ShortTag>(key) ?: return null).value
   }
   
   fun int(key: String, default: Int = 0): Int {
      return (getTagOrNull<IntTag>(key) ?: return default).value
   }
   
   fun intOrNull(key: String): Int? {
      return (getTagOrNull<IntTag>(key) ?: return null).value
   }
   
   fun long(key: String, default: Long = 0): Long {
      return (getTagOrNull<LongTag>(key) ?: return default).value
   }
   
   fun longOrNull(key: String): Long? {
      return (getTagOrNull<LongTag>(key) ?: return null).value
   }
   
   fun float(key: String, default: Float = 0f): Float {
      return (getTagOrNull<FloatTag>(key) ?: return default).value
   }
   
   fun floatOrNull(key: String): Float? {
      return (getTagOrNull<FloatTag>(key) ?: return null).value
   }
   
   fun double(key: String, default: Double = 0.0): Double {
      return (getTagOrNull<DoubleTag>(key) ?: return default).value
   }
   
   fun doubleOrNull(key: String): Double? {
      return (getTagOrNull<DoubleTag>(key) ?: return null).value
   }
   
   fun byteArray(key: String, default: ByteArray? = null): ByteArray {
      return (getTagOrNull<ByteArrayTag>(key) ?: return default ?: ByteArray(0)).value
   }
   
   fun byteArrayOrNull(key: String): ByteArray? {
      return (getTagOrNull<ByteArrayTag>(key) ?: return null).value
   }
   
   fun string(key: String, default: String = ""): String {
      return (getTagOrNull<StringTag>(key) ?: return default).value
   }
   
   fun stringOrNull(key: String): String? {
      return (getTagOrNull<StringTag>(key) ?: return null).value
   }
   
   fun <T : Tag> list(key: String, default: ListTag<T>? = null): ListTag<T> {
      return getTagOrNull(key) ?: return default ?: ListTag()
   }
   
   fun <T : Tag> listOrNull(key: String): ListTag<T>? {
      return getTagOrNull(key)
   }
   
   fun stringList(key: String, default: ListTag<StringTag>? = null): ListTag<StringTag> {
      return getTagOrNull(key) ?: return default ?: ListTag()
   }
   
   fun stringListOrNull(key: String): ListTag<StringTag>? {
      return getTagOrNull(key)
   }
   
   fun compoundList(key: String, default: ListTag<CompoundTag>? = null): ListTag<CompoundTag> {
      return getTagOrNull(key) ?: return default ?: ListTag()
   }
   
   fun compoundListOrNull(key: String): ListTag<CompoundTag>? {
      return getTagOrNull(key)
   }
   
   fun compound(key: String, default: CompoundTag? = null): CompoundTag {
      return getTagOrNull(key) ?: return default ?: CompoundTag()
   }
   
   fun compoundOrNull(key: String): CompoundTag? {
      return getTagOrNull(key)
   }
   
   fun intArray(key: String, default: IntArray? = null): IntArray {
      return (getTagOrNull<IntArrayTag>(key) ?: return default ?: IntArray(0)).value
   }
   
   fun intArrayOrNull(key: String): IntArray? {
      return (getTagOrNull<IntArrayTag>(key) ?: return null).value
   }
   
   fun uuid(key: String, default: UUID? = null): UUID {
      return intArrayOrNull(key)?.toUUID() ?: default ?: randomUUID()
   }
   
   fun uuidOrNull(key: String): UUID? {
      return intArrayOrNull(key)?.toUUID()
   }
   
   final inline fun <reified T : Any> complex(key: String): T {
      return getTag<ComplexTag>(key).value as T
   }
   
   final inline fun <reified T : Any> complexOrNull(key: String): T? {
      return getTagOrNull<ComplexTag>(key)?.value as? T
   }
   
   fun <T : Any> complex(key: String, klass: KClass<T>): T {
      val tag = getTag<ComplexTag>(key).value
      return klass.cast(tag)
   }
   
   fun <T : Any> complexOrNull(key: String, klass: KClass<T>): T? {
      val tag = getTagOrNull<ComplexTag>(key)?.value ?: return null
      return klass.safeCast(tag)
   }
   
   fun hasEnchantment(enchantment: Enchantment): Boolean {
      return short("id", -1) == enchantment.id.toShort()
   }
   
   /**
    * Gets an adapted value in the given [key] of this tag compound.
    *
    * @return the adapted value in [key] or [default]
    */
   fun <T : Any> adapted(key: String, adapter: TagAdapter<T>, default: T? = null): T {
      return try {
         adapter.read(key, this, default)
      } catch (_: Exception) {
         default ?: error("No default NBT found while getting adapted value")
      }
   }
   
   /**
    * Gets an adapted value in the given [key] of this tag compound.
    *
    * @return the adapted value in [key] or null
    */
   fun <T : Any> adaptedOrNull(key: String, adapter: TagAdapter<T>): T? {
      return catchingOrNull(null) {
         adapter.read(key, this, null)
      }
   }
   
   /**
    * Merges this compound tag to another [tag].
    */
   fun merge(tag: CompoundTag): CompoundTag {
      for ((key, value) in tag) {
         if (value is CompoundTag) {
            if (key in this) {
               compound(key).merge(value)
            } else {
               put(key, value.copy())
            }
         } else {
            put(key, value.copy())
         }
      }
      
      return this
   }
   
   operator fun <T : Any> set(key: String, adapter: TagAdapter<T>, value: T) = adapter.write(key, this, value)
   
   @OptIn(ExperimentalTypeInference::class)
   @OverloadResolutionByLambdaReturnType
   operator fun set(key: String, value: Tag) = put(key, value)
   operator fun set(key: String, value: Boolean) = put(key, ByteTag.of(value))
   operator fun set(key: String, value: Byte) = put(key, ByteTag.of(value))
   operator fun set(key: String, value: Short) = put(key, ShortTag.of(value))
   operator fun set(key: String, value: Int) = put(key, IntTag.of(value))
   operator fun set(key: String, value: Long) = put(key, LongTag.of(value))
   operator fun set(key: String, value: Float) = put(key, FloatTag.of(value))
   operator fun set(key: String, value: Double) = put(key, DoubleTag.of(value))
   operator fun set(key: String, value: ByteArray) = put(key, ByteArrayTag(value))
   operator fun set(key: String, value: String) = put(key, StringTag.of(value))
   operator fun set(key: String, value: IntArray) = put(key, IntArrayTag(value))
   operator fun set(key: String, value: LongArray) = put(key, LongArrayTag(value))
   operator fun set(key: String, value: java.io.Serializable) = put(key, ComplexTag(value))
   operator fun set(key: String, value: Any) {
      if (value is java.io.Serializable) put(key, ComplexTag(value))
   }
   
   private fun writeObject(stream: ObjectOutputStream) {
      write(stream)
   }
   
   private fun readObject(stream: ObjectInputStream) {
      while (true) {
         val id = stream.readByte()
         if (id == EMPTY_TAG_ID) {
            break
         }
         
         readEntry(TagRegistry[id], stream.readUTF(), stream)
      }
   }
   
   // nothing to do
   private fun readObjectNoData() {
   }
   
   /**
    * Tag type of [CompoundTag].
    */
   object Type : TagType<CompoundTag> {
      override val id: Byte get() = 10
      override val supportItems: Boolean get() = true
      
      override fun load(data: ObjectInput): CompoundTag {
         val tag = CompoundTag()
         
         while (true) {
            val id = data.readByte()
            if (id == EMPTY_TAG_ID) {
               break
            }
            
            tag.readEntry(TagRegistry[id], data.readUTF(), data)
         }
         
         return tag
      }
   }
   
   companion object {
      
      /**
       * Converts [data] in a NBT compound.
       */
      fun fromByteArray(data: ByteArray): CompoundTag {
         return data.decodeCompound()
      }
   }
}

/**
 * Creates an empty [CompoundTag].
 */
inline fun compoundOf() = CompoundTag()

/**
 * Creates a [CompoundTag] with all [values].
 */
fun compoundOf(vararg values: Pair<String, Tag>) = CompoundTag(*values)

fun compoundOf(storable: CompoundStorable): CompoundTag {
   val tag = CompoundTag()
   storable.save(tag)
   return tag
}

/**
 * Populates a newly created [CompoundTag].
 */
inline fun compound(builder: CompoundTag.() -> Unit): CompoundTag {
   contract { callsInPlace(builder, InvocationKind.EXACTLY_ONCE) }
   return CompoundTag().apply(builder)
}

/**
 * Creates a [ItemStack] from this tag.
 */
inline fun CompoundTag.createItem() = ItemStack(this)

/**
 * A global compound tag.
 *
 * This is used for some implementations that's uses [CompoundTag] as storage
 * and optionally their can have or not a storage, such as [ItemStack.tag].
 *
 * To avoid creating a new [CompoundTag] for each ItemStack for example and occuping more memory
 * we create and will use this GlobalCompound. Also this avoids the nullability for the tag implementation.
 */
object GlobalCompound : CompoundTag()
