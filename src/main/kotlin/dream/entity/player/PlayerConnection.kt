package dream.entity.player

import dream.chat.*
import dream.interfaces.*
import dream.misc.*
import dream.network.*
import dream.packet.game.*
import dream.server.*

/**
 * Represents a player connection.
 *
 * Responsable to listen game packets.
 */
@Open
class PlayerConnection(
   val server: Server,
   val network: NetworkManager,
   var player: Player,
) : GamePacketHandler, Tickable {
   
   /**
    * Ticks amount of this connection.
    */
   var ticks = 0
   
   override fun tick() {
      ++ticks
      
   }
   
   /**
    * Sends the given packet to the client connection of [player].
    */
   fun sendPacket(packet: HandledPacket) {
      if (packet.canSend(player)) {
         try {
            network.sendPacket(packet)
         } catch (e: Exception) {
            println("An error happened when trying to send packet to \n $player")
            e.printStackTrace()
         }
      }
   }
   
   /**
    * Sends the given packet to the client connection of [player].
    */
   fun sendPacket(packet: HandledPacket, vararg listeners: PacketListener) {
      if (packet.canSend(player)) {
         try {
            network.sendPacket(packet, *listeners)
         } catch (e: Exception) {
            println("An error happened when trying to send packet to \n $player")
            e.printStackTrace()
         }
      }
   }
   
   /**
    * Sends the given packet to the client connection of [player].
    */
   fun sendPacket(packet: HandledPacket, listener: PacketListener) {
      if (packet.canSend(player)) {
         try {
            network.sendPacket(packet, listener)
         } catch (e: Exception) {
            println("An error happened when trying to send packet to \n $player")
            e.printStackTrace()
         }
      }
   }
   
   override fun onDisconnect(reason: Component) {
   
   }
   
   
}
