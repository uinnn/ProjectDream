@file:Suppress("NOTHING_TO_INLINE")

package dream.utils

import com.soywiz.kmem.*
import kotlin.reflect.*

// BYTE

infix fun Byte.shl(other: Int) = (toInt() shl other).toByte()
infix fun Byte.shl(other: Number) = shl(other.toInt())
infix fun Byte.shr(other: Int) = (toInt() shr other).toByte()
infix fun Byte.shr(other: Number) = shr(other.toInt())
infix fun Byte.ushr(other: Int) = (toInt() ushr other).toByte()
infix fun Byte.ushr(other: Number) = ushr(other.toInt())
infix fun Byte.or(other: Int) = (toInt() or other).toByte()
infix fun Byte.or(other: Number) = or(other.toInt())
infix fun Byte.xor(other: Int) = (toInt() xor other).toByte()
infix fun Byte.xor(other: Number) = xor(other.toInt())
infix fun Byte.and(other: Int) = (toInt() and other).toByte()
infix fun Byte.and(other: Number) = and(other.toInt())

fun Byte.setBits(bits: Int) = this or bits
fun Byte.unsetBits(bits: Int) = this and bits.inv()
fun Byte.setBitsIf(bits: Int, onlyIf: Boolean) = if (onlyIf) this or bits else this
fun Byte.hasBits(bits: Int, min: Int) = (this and bits) > min
fun Byte.hasBits(bits: Int) = (toInt() and bits) == bits

fun <T : Enum<T>> Byte.setBits(enum: Enum<T>) = setBits(enum.mask)
fun <T : Enum<T>> Byte.setBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.setBits(v.mask) }
fun <T : Enum<T>> Byte.unsetBits(enum: Enum<T>) = unsetBits(enum.mask)
fun <T : Enum<T>> Byte.unsetBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.unsetBits(v.mask) }
fun <T : Enum<T>> Byte.hasBits(enum: Enum<T>) = hasBits(enum.mask)
fun <T : Enum<T>> Byte.hasAnyBits(vararg enum: Enum<T>) = enum.any { hasBits(it) }
fun <T : Enum<T>> Byte.hasAllBits(vararg enum: Enum<T>) = enum.all { hasBits(it) }

fun <T : Enum<T>> Byte.getFlags(clazz: KClass<T>) = clazz.enums.filter { hasBits(it) }
inline fun <reified T : Enum<T>> Byte.getFlags() = getFlags(T::class)

// SHORT

infix fun Short.shl(other: Int) = (toInt() shl other).toShort()
infix fun Short.shl(other: Number) = shl(other.toInt())
infix fun Short.shr(other: Int) = (toInt() shr other).toShort()
infix fun Short.shr(other: Number) = shr(other.toInt())
infix fun Short.ushr(other: Int) = (toInt() ushr other).toShort()
infix fun Short.ushr(other: Number) = ushr(other.toInt())
infix fun Short.or(other: Int) = (toInt() or other).toShort()
infix fun Short.or(other: Number) = or(other.toInt())
infix fun Short.xor(other: Int) = (toInt() xor other).toShort()
infix fun Short.xor(other: Number) = xor(other.toInt())
infix fun Short.and(other: Int) = (toInt() and other).toShort()
infix fun Short.and(other: Number) = and(other.toInt())

fun Short.setBits(bits: Int) = this or bits
fun Short.unsetBits(bits: Int) = this and bits.inv()
fun Short.setBitsIf(bits: Int, onlyIf: Boolean) = if (onlyIf) this or bits else this
fun Short.hasBits(bits: Int, min: Int) = (this and bits) > min
fun Short.hasBits(bits: Int) = (toInt() and bits) == bits

fun <T : Enum<T>> Short.setBits(enum: Enum<T>) = setBits(enum.mask)
fun <T : Enum<T>> Short.setBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.setBits(v.mask) }
fun <T : Enum<T>> Short.unsetBits(enum: Enum<T>) = unsetBits(enum.mask)
fun <T : Enum<T>> Short.unsetBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.unsetBits(v.mask) }
fun <T : Enum<T>> Short.hasBits(enum: Enum<T>) = hasBits(enum.mask)
fun <T : Enum<T>> Short.hasAnyBits(vararg enum: Enum<T>) = enum.any { hasBits(it) }
fun <T : Enum<T>> Short.hasAllBits(vararg enum: Enum<T>) = enum.all { hasBits(it) }

fun <T : Enum<T>> Short.getFlags(clazz: KClass<T>) = clazz.enums.filter { hasBits(it) }
inline fun <reified T : Enum<T>> Short.getFlags() = getFlags(T::class)

// INT

infix fun Int.shl(other: Number) = shl(other.toInt())
infix fun Int.shr(other: Number) = shr(other.toInt())
infix fun Int.ushr(other: Number) = ushr(other.toInt())
infix fun Int.or(other: Number) = or(other.toInt())
infix fun Int.xor(other: Number) = xor(other.toInt())
infix fun Int.and(other: Number) = and(other.toInt())

fun Int.setBitsIf(bits: Int, onlyIf: Boolean) = if (onlyIf) this or bits else this
fun Int.hasBits(bits: Int, min: Int) = (this and bits) > min

fun <T : Enum<T>> Int.setBits(enum: Enum<T>) = setBits(enum.mask)
fun <T : Enum<T>> Int.setBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.setBits(v.mask) }
fun <T : Enum<T>> Int.unsetBits(enum: Enum<T>) = unsetBits(enum.mask)
fun <T : Enum<T>> Int.unsetBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.unsetBits(v.mask) }
fun <T : Enum<T>> Int.hasBits(enum: Enum<T>) = hasBits(enum.mask)
fun <T : Enum<T>> Int.hasAnyBits(vararg enum: Enum<T>) = enum.any { hasBits(it) }
fun <T : Enum<T>> Int.hasAllBits(vararg enum: Enum<T>) = enum.all { hasBits(it) }

fun <T : Enum<T>> Int.getFlags(clazz: KClass<T>) = clazz.enums.filter { hasBits(it) }
inline fun <reified T : Enum<T>> Int.getFlags() = getFlags(T::class)

fun <T : Enum<T>> Iterable<T>.masked() = calculate(0) { value, result -> result.setBits(value) }

// LONG

infix fun Long.shl(other: Number) = shl(other.toInt())
infix fun Long.shr(other: Number) = shr(other.toInt())
infix fun Long.ushr(other: Number) = ushr(other.toInt())
infix fun Long.or(other: Int) = (toInt() or other).toLong()
infix fun Long.or(other: Number) = or(other.toInt())
infix fun Long.xor(other: Int) = (toInt() xor other).toLong()
infix fun Long.xor(other: Number) = xor(other.toInt())
infix fun Long.and(other: Int) = (toInt() and other).toLong()
infix fun Long.and(other: Number) = and(other.toInt())

fun Long.setBits(bits: Int) = this or bits
fun Long.unsetBits(bits: Int) = this and bits.inv()
fun Long.setBitsIf(bits: Int, onlyIf: Boolean) = if (onlyIf) this or bits else this
fun Long.hasBits(bits: Int, min: Int) = (this and bits) > min
fun Long.hasBits(bits: Int) = (toInt() and bits) == bits

fun <T : Enum<T>> Long.setBits(enum: Enum<T>) = setBits(enum.mask)
fun <T : Enum<T>> Long.setBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.setBits(v.mask) }
fun <T : Enum<T>> Long.unsetBits(enum: Enum<T>) = unsetBits(enum.mask)
fun <T : Enum<T>> Long.unsetBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.unsetBits(v.mask) }
fun <T : Enum<T>> Long.hasBits(enum: Enum<T>) = hasBits(enum.mask)
fun <T : Enum<T>> Long.hasBits(vararg enum: Enum<T>) = enum.any { hasBits(it) }
fun <T : Enum<T>> Long.hasAllBits(vararg enum: Enum<T>) = enum.all { hasBits(it) }

fun <T : Enum<T>> Long.getFlags(clazz: KClass<T>) = clazz.enums.filter { hasBits(it) }
inline fun <reified T : Enum<T>> Long.getFlags() = getFlags(T::class)

// FLOAT

infix fun Float.shl(other: Int) = (toInt() shl other).toFloat()
infix fun Float.shl(other: Number) = shl(other.toInt())
infix fun Float.shr(other: Int) = (toInt() shr other).toFloat()
infix fun Float.shr(other: Number) = shr(other.toInt())
infix fun Float.ushr(other: Int) = (toInt() ushr other).toFloat()
infix fun Float.ushr(other: Number) = ushr(other.toInt())
infix fun Float.or(other: Int) = (toInt() or other).toFloat()
infix fun Float.or(other: Number) = or(other.toInt())
infix fun Float.xor(other: Int) = (toInt() xor other).toFloat()
infix fun Float.xor(other: Number) = xor(other.toInt())
infix fun Float.and(other: Int) = (toInt() and other).toFloat()
infix fun Float.and(other: Number) = and(other.toInt())

fun Float.setBits(bits: Int) = this or bits
fun Float.unsetBits(bits: Int) = this and bits.inv()
fun Float.setBitsIf(bits: Int, onlyIf: Boolean) = if (onlyIf) this or bits else this
fun Float.hasBits(bits: Int, min: Int) = (this and bits) > min
fun Float.hasBits(bits: Int) = (toInt() and bits) == bits

fun <T : Enum<T>> Float.setBits(enum: Enum<T>) = setBits(enum.mask)
fun <T : Enum<T>> Float.setBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.setBits(v.mask) }
fun <T : Enum<T>> Float.unsetBits(enum: Enum<T>) = unsetBits(enum.mask)
fun <T : Enum<T>> Float.unsetBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.unsetBits(v.mask) }
fun <T : Enum<T>> Float.hasBits(enum: Enum<T>) = hasBits(enum.mask)
fun <T : Enum<T>> Float.hasBits(vararg enum: Enum<T>) = enum.any { hasBits(it) }
fun <T : Enum<T>> Float.hasAllBits(vararg enum: Enum<T>) = enum.all { hasBits(it) }

fun <T : Enum<T>> Float.getFlags(clazz: KClass<T>) = clazz.enums.filter { hasBits(it) }
inline fun <reified T : Enum<T>> Float.getFlags() = getFlags(T::class)

// DOUBLE

infix fun Double.shl(other: Int) = (toInt() shl other).toDouble()
infix fun Double.shl(other: Number) = shl(other.toInt())
infix fun Double.shr(other: Int) = (toInt() shr other).toDouble()
infix fun Double.shr(other: Number) = shr(other.toInt())
infix fun Double.ushr(other: Int) = (toInt() ushr other).toDouble()
infix fun Double.ushr(other: Number) = ushr(other.toInt())
infix fun Double.or(other: Int) = (toInt() or other).toDouble()
infix fun Double.or(other: Number) = or(other.toInt())
infix fun Double.xor(other: Int) = (toInt() xor other).toDouble()
infix fun Double.xor(other: Number) = xor(other.toInt())
infix fun Double.and(other: Int) = (toInt() and other).toDouble()
infix fun Double.and(other: Number) = and(other.toInt())

fun Double.setBits(bits: Int) = this or bits
fun Double.unsetBits(bits: Int) = this and bits.inv()
fun Double.setBitsIf(bits: Int, onlyIf: Boolean) = if (onlyIf) this or bits else this
fun Double.hasBits(bits: Int, min: Int) = (this and bits) > min
fun Double.hasBits(bits: Int) = (toInt() and bits) == bits

fun <T : Enum<T>> Double.setBits(enum: Enum<T>) = setBits(enum.mask)
fun <T : Enum<T>> Double.setBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.setBits(v.mask) }
fun <T : Enum<T>> Double.unsetBits(enum: Enum<T>) = unsetBits(enum.mask)
fun <T : Enum<T>> Double.unsetBits(vararg enum: Enum<T>) = enum.calculate { v, t -> t.unsetBits(v.mask) }
fun <T : Enum<T>> Double.hasBits(enum: Enum<T>) = hasBits(enum.mask)
fun <T : Enum<T>> Double.hasBits(vararg enum: Enum<T>) = enum.any { hasBits(it) }
fun <T : Enum<T>> Double.hasAllBits(vararg enum: Enum<T>) = enum.all { hasBits(it) }

fun <T : Enum<T>> Double.getFlags(clazz: KClass<T>) = clazz.enums.filter { hasBits(it) }
inline fun <reified T : Enum<T>> Double.getFlags() = getFlags(T::class)
