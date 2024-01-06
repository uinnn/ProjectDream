@file:Suppress("NOTHING_TO_INLINE")

package dream.utils

import com.soywiz.kmem.*
import net.jafama.FastMath
import kotlin.math.*

val SQRT_2 = sqrt(2.0)

const val PI = Math.PI

inline fun cos(n: Double) = FastMath.cosQuick(n)
inline fun cos(n: Int) = FastMath.cosQuick(n.toDouble())
inline fun cos(n: Float) = FastMath.cosQuick(n.toDouble())

inline fun cosh(n: Double) = FastMath.cosh(n)
inline fun cosh(n: Int) = FastMath.cosh(n.toDouble())
inline fun cosh(n: Float) = FastMath.cosh(n.toDouble())

inline fun acos(n: Double) = FastMath.acos(n)
inline fun acos(n: Int) = FastMath.acos(n.toDouble())
inline fun acos(n: Float) = FastMath.acos(n.toDouble())

inline fun acosh(n: Double) = FastMath.acosh(n)
inline fun acosh(n: Int) = FastMath.acosh(n.toDouble())
inline fun acosh(n: Float) = FastMath.acosh(n.toDouble())

inline fun sin(n: Double) = FastMath.sinQuick(n)
inline fun sin(n: Int) = FastMath.sinQuick(n.toDouble())
inline fun sin(n: Float) = FastMath.sinQuick(n.toDouble())

inline fun asin(n: Double) = FastMath.asin(n)
inline fun asin(n: Int) = FastMath.asin(n.toDouble())
inline fun asin(n: Float) = FastMath.asin(n.toDouble())

inline fun asinh(n: Double) = FastMath.asinh(n)
inline fun asinh(n: Int) = FastMath.asinh(n.toDouble())
inline fun asinh(n: Float) = FastMath.asinh(n.toDouble())

inline fun tan(n: Double) = FastMath.tan(n)
inline fun tan(n: Int) = FastMath.tan(n.toDouble())
inline fun tan(n: Float) = FastMath.tan(n.toDouble())

inline fun atan(n: Double) = FastMath.atan(n)
inline fun atan(n: Int) = FastMath.atan(n.toDouble())
inline fun atan(n: Float) = FastMath.atan(n.toDouble())

inline fun atan2(x: Double, y: Double) = FastMath.atan2(x, y)
inline fun atan2(x: Int, y: Int) = FastMath.atan2(x.toDouble(), y.toDouble())
inline fun atan2(x: Float, y: Float) = FastMath.atan2(x.toDouble(), y.toDouble())

inline fun hypot(x: Double, y: Double) = FastMath.hypot(x, y)
inline fun hypot(x: Int, y: Int) = FastMath.hypot(x.toDouble(), y.toDouble())
inline fun hypot(x: Float, y: Float) = FastMath.hypot(x.toDouble(), y.toDouble())

inline fun hypot(x: Double, y: Double, z: Double) = FastMath.hypot(x, y, z)
inline fun hypot(x: Int, y: Int, z: Double) = FastMath.hypot(x.toDouble(), y.toDouble(), z.toDouble())
inline fun hypot(x: Float, y: Float, z: Double) = FastMath.hypot(x.toDouble(), y.toDouble(), z.toDouble())

inline fun cbrt(n: Double) = FastMath.cbrt(n)
inline fun cbrt(n: Int) = FastMath.cbrt(n.toDouble())
inline fun cbrt(n: Float) = FastMath.cbrt(n.toDouble())

inline fun sqrt(n: Int) = sqrt(n.toDouble())
inline fun sqrt(n: Float) = sqrt(n.toDouble())

inline fun pow(n: Double, power: Double) = FastMath.powQuick(n, power)
inline fun pow(n: Int, power: Double) = FastMath.powQuick(n.toDouble(), power)
inline fun pow(n: Float, power: Double) = FastMath.powQuick(n.toDouble(), power)

inline fun exp(n: Double) = FastMath.expQuick(n)
inline fun exp(n: Int) = FastMath.expQuick(n.toDouble())
inline fun exp(n: Float) = FastMath.expQuick(n.toDouble())

inline fun expm1(n: Double) = FastMath.expm1(n)
inline fun expm1(n: Int) = FastMath.expm1(n.toDouble())
inline fun expm1(n: Float) = FastMath.expm1(n.toDouble())

inline fun log(n: Double) = FastMath.logQuick(n)
inline fun log(n: Int) = FastMath.logQuick(n.toDouble())
inline fun log(n: Float) = FastMath.logQuick(n.toDouble())

inline fun log2(n: Double) = FastMath.log2(n.toInt())
inline fun log2(n: Int) = FastMath.log2(n)
inline fun log2(n: Float) = FastMath.log2(n.toInt())

inline fun log10(n: Double) = FastMath.log10(n)
inline fun log10(n: Int) = FastMath.log10(n.toDouble())
inline fun log10(n: Float) = FastMath.log10(n.toDouble())

inline fun log1p(n: Double) = FastMath.log1p(n)
inline fun log1p(n: Int) = FastMath.log1p(n.toDouble())
inline fun log1p(n: Float) = FastMath.log1p(n.toDouble())

inline fun floorInt(n: Double) = floor(n).toInt()
inline fun floorInt(n: Float) = floor(n).toInt()

inline fun floorLong(n: Double) = floor(n).toLong()
inline fun floorLong(n: Float) = floor(n).toLong()

inline fun absInt(n: Double) = abs(n).toInt()
inline fun absInt(n: Float) = abs(n).toInt()

fun logBaseTwo(value: Int): Int = logBaseTwoDeBruijn(value) - if (value.isPowerOfTwo) 0 else 1

inline fun squared(n: Int) = n * n
inline fun squared(n: Double) = n * n
inline fun squared(n: Float) = n * n

inline fun min(a: Int, b: Int) = if (a <= b) a else b
inline fun min(a: Long, b: Long) = if (a <= b) a else b
inline fun min(a: Float, b: Float) = if (a <= b) a else b
inline fun min(a: Double, b: Double) = if (a <= b) a else b

inline fun max(a: Int, b: Int) = if (a >= b) a else b
inline fun max(a: Long, b: Long) = if (a >= b) a else b
inline fun max(a: Float, b: Float) = if (a >= b) a else b
inline fun max(a: Double, b: Double) = if (a >= b) a else b

/**
 * Though it looks like an array, this is really more like a mapping.
 *
 * Key (index of this array) is the upper 5 bits  of the result of multiplying
 * a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531.
 *
 * Value is the unique index (from the right) of the leftmost one-bit in a
 * 32-bit unsigned integer that can cause the upper 5 bits to get that value.
 *
 * Used for highly optimized "find the log-base-2 of this number" calculations.
 */
internal val MULTIPLY_DE_BRUIJIN_BIT_POSITION = intArrayOf(
  0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8,
  31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
)

private fun logBaseTwoDeBruijn(value: Int): Int {
  val n = if (value.isPowerOfTwo) value else value.nextPowerOfTwo
  return MULTIPLY_DE_BRUIJIN_BIT_POSITION[((n * 125613361L shr 27) and 31).toInt()]
}
