package dream.storage

import io.netty.buffer.ByteBuf

/**
 * Writes a 56-bit value to the ByteBuf.
 * @param value The 56-bit value to be written.
 */
fun ByteBuf.write56Bits(value: Long) {
  writeByte(((value shr 48) and 0xFF).toInt())
  writeByte(((value shr 40) and 0xFF).toInt())
  writeByte(((value shr 32) and 0xFF).toInt())
  writeByte(((value shr 24) and 0xFF).toInt())
  writeByte(((value shr 16) and 0xFF).toInt())
  writeByte(((value shr 8) and 0xFF).toInt())
  writeByte((value and 0xFF).toInt())
}

/**
 * Reads a 56-bit value from the ByteBuf.
 * @return The 56-bit value read from the ByteBuf.
 */
fun ByteBuf.read56Bits(): Long {
  return ((readByte().toLong() and 0xFF) shl 48) or
      ((readByte().toLong() and 0xFF) shl 40) or
      ((readByte().toLong() and 0xFF) shl 32) or
      ((readByte().toLong() and 0xFF) shl 24) or
      ((readByte().toLong() and 0xFF) shl 16) or
      ((readByte().toLong() and 0xFF) shl 8) or
      (readByte().toLong() and 0xFF)
}

