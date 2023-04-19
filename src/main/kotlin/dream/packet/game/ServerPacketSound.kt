package dream.packet.game

import dream.*
import dream.network.*

/**
 * Serverbound Sound packet.
 */
class ServerPacketSound(
   var sound: Sound,
   var x: Int,
   var y: Int,
   var z: Int,
   var volume: Float,
   var pitch: Float,
) : ServerGamePacket {
   
   constructor(sound: Sound, x: Int, y: Int, z: Int, volume: Float, pitch: Int) : this(
      sound,
      x * 8,
      y * 8,
      z * 8,
      volume,
      pitch * 63f
   )
   
   constructor(buf: PacketBuffer) : this(
      buf.readSound(),
      buf.readInt(),
      buf.readInt(),
      buf.readInt(),
      buf.readFloat(),
      buf.readUnsignedByte().toInt()
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeString(sound.name)
      buf.writeInt(x)
      buf.writeInt(y)
      buf.writeInt(z)
      buf.writeFloat(volume)
      buf.writeByte(pitch.toInt())
   }
   
   override fun process(handler: GamePacketHandler) {
      TODO("Not yet implemented")
   }
   
}
