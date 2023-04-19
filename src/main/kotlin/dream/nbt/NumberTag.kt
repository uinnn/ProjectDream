package dream.nbt

import java.io.*

/**
 * Represents a numerical tag.
 */
interface NumberTag : Tag, Serializable {
   fun toNumber(): Number
   fun toByte(): Byte
   fun toChar(): Char = toInt().toChar()
   fun toShort(): Short
   fun toInt(): Int
   fun toLong(): Long
   fun toFloat(): Float
   fun toDouble(): Double
   
   operator fun compareTo(value: Byte) = toByte().compareTo(value)
   operator fun compareTo(value: Char) = toChar().compareTo(value)
   operator fun compareTo(value: Short) = toShort().compareTo(value)
   operator fun compareTo(value: Int) = toInt().compareTo(value)
   operator fun compareTo(value: Long) = toLong().compareTo(value)
   operator fun compareTo(value: Float) = toFloat().compareTo(value)
   operator fun compareTo(value: Double) = toDouble().compareTo(value)
}
