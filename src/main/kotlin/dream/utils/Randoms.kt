@file:Suppress("NOTHING_TO_INLINE")

package dream.utils

import com.soywiz.kds.random.*
import kotlin.random.*

/**
 * The random instance used to generate random numbers.
 */
val Random = FastRandom()

fun randomByte(rnd: Random = Random) = rnd.nextInt().toByte()
fun randomByte(max: Byte, rnd: Random = Random) = randomInt(max.toInt(), rnd).toByte()
fun randomByte(from: Int, until: Int, rnd: Random = Random) = randomInt(from, until, rnd).toByte()

fun randomShort(rnd: Random = Random) = rnd.nextInt().toShort()
fun randomShort(max: Short, rnd: Random = Random) = randomInt(max.toInt(), rnd).toShort()
fun randomShort(from: Int, until: Int, rnd: Random = Random) = randomInt(from, until, rnd).toShort()

fun randomInt(rnd: Random = Random) = rnd.nextInt()
fun randomInt(max: Int, rnd: Random = Random) = rnd.nextInt(max)
fun randomInt(from: Int, until: Int, rnd: Random = Random) = rnd.nextInt(from, until)

fun randomLong(rnd: Random = Random) = rnd.nextLong()
fun randomLong(max: Long, rnd: Random = Random) = rnd.nextLong(max)
fun randomLong(from: Long, until: Long, rnd: Random = Random) = rnd.nextLong(from, until)

fun randomFloat(rnd: Random = Random) = rnd.nextFloat()
fun randomFloat(max: Float, rnd: Random = Random) = randomDouble(max.toDouble(), rnd).toFloat()
fun randomFloat(from: Float, until: Float, rnd: Random = Random) =
   randomDouble(from.toDouble(), until.toDouble(), rnd).toFloat()

fun randomDouble(rnd: Random = Random) = rnd.nextDouble()
fun randomDouble(max: Double, rnd: Random = Random) = rnd.nextDouble(max)
fun randomDouble(from: Double, until: Double, rnd: Random = Random) = rnd.nextDouble(from, until)

fun randomBoolean(rnd: Random = Random) = rnd.nextBoolean()

fun chance(percentage: Double, rnd: Random = Random) = randomDouble(rnd) * 100 <= percentage
fun chance(percentage: Float, rnd: Random = Random) = randomFloat(rnd) * 100 <= percentage
fun chance(percentage: Int, rnd: Random = Random) = randomInt(rnd) * 100 <= percentage
