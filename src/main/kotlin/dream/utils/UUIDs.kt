@file:Suppress("NOTHING_TO_INLINE")

package dream.utils

import java.util.*

/**
 * Calls [UUID.fromString] to create an uuid from [string].
 */
inline fun uuid(string: String): UUID = UUID.fromString(string)

/**
 * Creates a new random [uuid].
 */
inline fun randomUUID(): UUID = UUID.randomUUID()

/**
 * Calls [UUID.nameUUIDFromBytes] to create an uuid from [bytes].
 */
inline fun uuid(bytes: ByteArray): UUID = UUID.nameUUIDFromBytes(bytes)

fun uuid(most: Long, least: Long) = UUID(most, least)
fun uuid(most: Int, least: Int) = UUID(most.toLong(), least.toLong())

/**
 * Creates a [UUID] from [name] to locate an offline player.
 */
fun OfflineUUID(name: String) = uuid("OfflinePlayer:$name".toByteArray())

/**
 * Creates a random [UUID] used on creating entity ID's.
 */
fun EntityUUID(): UUID {
   val most = randomLong() and -61441L or 16384L
   val least = randomLong() and 4611686018427387903L or Long.MIN_VALUE
   return UUID(most, least)
}

/**
 * Converts this UUID to IntArray.
 */
fun UUID.toIntArray(): IntArray {
   val most = mostSignificantBits.toInt()
   val least = leastSignificantBits.toInt()
   return intArrayOf(most shr 32, most, least shr 32, least)
}

/**
 * Converts this IntArray constructued by [UUID.toIntArray] backs to UUID.
 */
fun IntArray.toUUID(): UUID {
   return uuid(
      get(0).toLong() shl 32 or get(1).toLong() and 4294967295L,
      get(2).toLong() shl 32 or get(4).toLong() and 4294967295L
   )
}
