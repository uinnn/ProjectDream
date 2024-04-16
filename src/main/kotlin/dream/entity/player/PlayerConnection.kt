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
) : GamePacketHandler, Tickable {

  constructor(original: PlayerConnection) : this(original.server, original.network) {
    this.player = original.player
  }

  /**
   * The player associated with this connection.
   */
  lateinit var player: Player

  /**
   * Checks if the player of this connection has been initialized.
   */
  val hasPlayer get() = ::player.isInitialized

  /**
   * Ticks amount of this connection.
   */
  var ticks = 0

  override fun tick(partial: Int) {
    if (ticks % 20 == 0) {
      sendPacket(SPacketKeepAlive(player))
    }
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
        throw getSendPacketException(packet, e)
          .categories(*listeners.mapArrayIndexed { index, listener -> "Listener #$index" to listener })
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
  }

  override fun handleAbilities(packet: CPacketAbilities) {
    println("Received abilities packet $packet")
  }

  override fun handleAnimation(packet: CPacketAnimation) {
    println("Received animation packet $packet")
  }

  override fun handleBlockPlace(packet: CPacketBlockPlace) {
    println("Received block place packet $packet")
  }

  override fun handleChat(packet: CPacketChat) {
    println("Received chat packet $packet")
  }

  override fun handleWindowClick(packet: CPacketClickWindow) {
    println("Received click window packet $packet")
  }

  override fun handleCloseWindow(packet: CPacketCloseWindow) {
    println("Received close window packet $packet")
  }

  override fun handleConfirmTransaction(packet: CPacketConfirmTransaction) {
    println("Received confirm transaction packet $packet")
  }

  override fun handleCreativeAction(packet: CPacketCreativeAction) {
    println("Received creative action packet $packet")
  }

  override fun handleDig(packet: CPacketDig) {
    println("Received dig packet $packet")
  }

  override fun handleEnchant(packet: CPacketEnchantItem) {
    println("Received enchant packet $packet")
  }

  override fun handleAction(packet: CPacketEntityAction) {
    println("Received entity action packet $packet")
  }

  override fun handleHeldItemChange(packet: CPacketHeldItemChange) {
    println("Received held item change packet $packet")
  }

  override fun handleInput(packet: CPacketInput) {
    println("Received input packet $packet")
  }

  override fun handleKeepAlive(packet: CPacketKeepAlive) {
    println("Received keep alive packet $packet")
  }

  override fun handlePayload(packet: CPacketPayload) {
    println("Received payload packet $packet")
  }

  override fun handlePlayerInfo(packet: CPacketPlayerInfo) {
    println("Received player info packet $packet")
  }

  override fun handleResourcePack(packet: CPacketResourcePack) {
    println("Received resource pack packet $packet")
  }

  override fun handleSettings(packet: CPacketSettings) {
    println("Received settings packet $packet")
  }

  override fun handleSpectate(packet: CPacketSpectate) {
    println("Received spectate packet $packet")
  }

  override fun handleStatus(packet: CPacketStatus) {
    println("Received status packet $packet")
  }

  override fun handleTabComplete(packet: CPacketTabComplete) {
    println("Received tab complete packet $packet")
  }

  override fun handleUpdateSign(packet: CPacketUpdateSign) {
    println("Received update sign packet $packet")
  }

  override fun handleUseEntity(packet: CPacketUseEntity) {
    println("Received use entity packet $packet")
  }

  override fun onDisconnect(reason: Component) {
    println("${player.name} lost connection: ${reason.unformattedText}")
    server.playerList.onLoggout(player)
  }

  private fun getSendPacketException(packet: HandledPacket, cause: Exception): MinecraftException {
    return MinecraftException("Error while sending packet to player", cause)
      .category("Player", player)
      .category("Packet", packet)
  }

}
