package dream.entity.player

import dream.api.*
import dream.chat.*
import dream.errors.*
import dream.misc.*
import dream.network.*
import dream.packet.game.*
import dream.packet.login.*
import dream.server.*
import dream.utils.*
import io.netty.channel.*

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
  
  constructor(original: PlayerConnection) : this(original.server, original.network, original.player)
  
  /**
   * Ticks amount of this connection.
   */
  var ticks = 0
  
  override fun tick(partial: Int) {
    ++ticks
    
  }
  
  /**
   * Called when any packet is receivied from client.
   */
  override fun onReceivePacket(packet: HandledPacket, manager: NetworkManager, context: ChannelHandlerContext) {
    if (packet.canReceive(player)) {
      packet.process(this)
    }
  }
  
  /**
   * Simulates a receive packet action from client/server packet types.
   *
   * This can be used to process packet without extra data as on [onReceivePacket].
   */
  override fun simulateReceivePacket(packet: HandledPacket) {
    packet.process(this)
  }
  
  /**
   * Sends the given packet to the client connection of [player].
   */
  fun sendPacket(packet: HandledPacket) {
    if (packet.canSend(player)) {
      try {
        network.sendPacket(packet)
      } catch (e: Exception) {
        throw getSendPacketException(packet, e)
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
        throw getSendPacketException(
          packet, e
        ).categories(*listeners.mapArrayIndexed { index, listener -> "Listener #$index" to listener })
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
        throw getSendPacketException(packet, e).category("Listener", listener)
      }
    }
  }
  
  /**
   * Disconnects a player with a reason.
   */
  fun disconnect(reason: String) {
    val text = text(reason)
    sendPacket(SPacketDisconnect(text)) { network.closeChannel(text) }
    network.disableAutoRead()
    //Futures.getUnchecked(server.addScheduledTask(this.netManager::checkDisconnected))
  }
  
  override fun handleAbilities(packet: CPacketAbilities) {
    TODO("Not yet implemented")
  }
  
  override fun handleAnimation(packet: CPacketAnimation) {
    TODO("Not yet implemented")
  }
  
  override fun handleBlockPlace(packet: CPacketBlockPlace) {
    TODO("Not yet implemented")
  }
  
  override fun handleChat(packet: CPacketChat) {
    TODO("Not yet implemented")
  }
  
  override fun handleWindowClick(packet: CPacketClickWindow) {
    TODO("Not yet implemented")
  }
  
  override fun handleCloseWindow(packet: CPacketCloseWindow) {
    TODO("Not yet implemented")
  }
  
  override fun handleConfirmTransaction(packet: CPacketConfirmTransaction) {
    TODO("Not yet implemented")
  }
  
  override fun handleCreativeAction(packet: CPacketCreativeAction) {
    TODO("Not yet implemented")
  }
  
  override fun handleDig(packet: CPacketDig) {
    TODO("Not yet implemented")
  }
  
  override fun handleEnchant(packet: CPacketEnchantItem) {
    TODO("Not yet implemented")
  }
  
  override fun handleAction(packet: CPacketEntityAction) {
    TODO("Not yet implemented")
  }
  
  override fun handleHeldItemChange(packet: CPacketHeldItemChange) {
    TODO("Not yet implemented")
  }
  
  override fun handleInput(packet: CPacketInput) {
    TODO("Not yet implemented")
  }
  
  override fun handleKeepAlive(packet: CPacketKeepAlive) {
    TODO("Not yet implemented")
  }
  
  override fun handlePayload(packet: CPacketPayload) {
    TODO("Not yet implemented")
  }
  
  override fun handlePlayerInfo(packet: CPacketPlayerInfo) {
    TODO("Not yet implemented")
  }
  
  override fun handleResourcePack(packet: CPacketResourcePack) {
    TODO("Not yet implemented")
  }
  
  override fun handleSettings(packet: CPacketSettings) {
    TODO("Not yet implemented")
  }
  
  override fun handleSpectate(packet: CPacketSpectate) {
    TODO("Not yet implemented")
  }
  
  override fun handleStatus(packet: CPacketStatus) {
    TODO("Not yet implemented")
  }
  
  override fun handleTabComplete(packet: CPacketTabComplete) {
    TODO("Not yet implemented")
  }
  
  override fun handleUpdateSign(packet: CPacketUpdateSign) {
    TODO("Not yet implemented")
  }
  
  override fun handleUseEntity(packet: CPacketUseEntity) {
    TODO("Not yet implemented")
  }
  
  override fun onDisconnect(reason: Component) {
  
  }
  
  private fun getSendPacketException(packet: HandledPacket, cause: Exception): MinecraftException {
    return MinecraftException("Error while sending packet to player", cause).category("Player", player)
      .category("Packet", packet)
  }
  
}
