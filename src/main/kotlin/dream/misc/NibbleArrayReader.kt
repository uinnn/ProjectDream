package dream.misc

class NibbleArrayReader(val data: ByteArray, val depthBits: Int) {
    private val depthBitsPlusFour = depthBits + 4

    operator fun get(x: Int, y: Int, z: Int): Int {
        val i = x shl depthBitsPlusFour or (z shl depthBits) or y
        val j = i shr 1
        return if ((i and 1) == 0) data[j].toInt() and 15 else data[j].toInt() shr 4 and 15
    }
}
