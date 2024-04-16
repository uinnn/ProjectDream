package dream.packet.game

import dream.block.Blocks
import dream.block.state.IState
import dream.level.chunk.Chunk
import dream.level.chunk.ChunkCoordinate
import dream.network.PacketBuffer
import dream.pos.Pos

/**
 * Serverbound packet used to change multiple blocks in a chunk at once.
 */
data class SPacketMultiBlockChange(
  var coordinate: ChunkCoordinate,
  var changes: Array<ExtractedBlock>
) : ServerGamePacket {

  companion object {

    /**
     * Constructor-like function for [SPacketMultiBlockChange].
     */
    operator fun invoke(chunk: Chunk, count: Int, positions: ShortArray): SPacketMultiBlockChange {
      val coordinate = ChunkCoordinate(chunk.x, chunk.z)
      val changes = Array(count) {
        val pos = positions[it]
        ExtractedBlock(pos, chunk.getState(getPos(pos.toInt(), coordinate)))
      }
      return SPacketMultiBlockChange(coordinate, changes)
    }
  }

  constructor(buf: PacketBuffer) : this(
    ChunkCoordinate(buf.readInt(), buf.readInt()),
    Array(buf.readVarInt()) {
      ExtractedBlock(buf)
    }
  )

  override fun write(buf: PacketBuffer) {
    buf.writeInt(coordinate.x)
    buf.writeInt(coordinate.z)
    buf.writeVarInt(changes.size)
    for (change in changes) {
      buf.writeShort(change.crammedPos)
      buf.writeState(change.state)
    }
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }

  fun getPos(crammedPos: Short): Pos = getPos(crammedPos.toInt(), coordinate)
}

/**
 * Multi block change data used in [SPacketMultiBlockChange].
 */
data class ExtractedBlock(var crammedPos: Short, var state: IState = Blocks.AIR.state) {
  constructor(buf: PacketBuffer) : this(buf.readShort(), buf.readState())

  /**
   * Gets the position of the block in the chunk with the given coordinates.
   */
  fun getPos(coordinate: ChunkCoordinate): Pos = getPos(crammedPos.toInt(), coordinate)
}

/**
 * Gets a relative position from the given [crammedPos] and [coordinate].
 */
internal fun getPos(crammedPos: Int, coordinate: ChunkCoordinate): Pos {
  return coordinate.getPos(crammedPos shr 12 and 15, crammedPos and 255, crammedPos shr 8 and 15)
}
