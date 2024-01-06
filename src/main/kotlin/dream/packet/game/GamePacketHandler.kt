package dream.packet.game

import dream.packet.*

typealias GamePacket = Packet<GamePacketHandler>
typealias ServerGamePacket = ServerPacket<GamePacketHandler>
typealias ClientGamePacket = ClientPacket<GamePacketHandler>

/**
 * Processor handler for game packets.
 */
interface GamePacketHandler : PacketHandler {
  
  fun handleAbilities(packet: CPacketAbilities)
  fun handleAnimation(packet: CPacketAnimation)
  fun handleBlockPlace(packet: CPacketBlockPlace)
  fun handleChat(packet: CPacketChat)
  fun handleWindowClick(packet: CPacketClickWindow)
  fun handleCloseWindow(packet: CPacketCloseWindow)
  fun handleConfirmTransaction(packet: CPacketConfirmTransaction)
  fun handleCreativeAction(packet: CPacketCreativeAction)
  fun handleDig(packet: CPacketDig)
  fun handleEnchant(packet: CPacketEnchantItem)
  fun handleAction(packet: CPacketEntityAction)
  fun handleHeldItemChange(packet: CPacketHeldItemChange)
  fun handleInput(packet: CPacketInput)
  fun handleKeepAlive(packet: CPacketKeepAlive)
  fun handlePayload(packet: CPacketPayload)
  fun handlePlayerInfo(packet: CPacketPlayerInfo)
  fun handleResourcePack(packet: CPacketResourcePack)
  fun handleSettings(packet: CPacketSettings)
  fun handleSpectate(packet: CPacketSpectate)
  fun handleStatus(packet: CPacketStatus)
  fun handleTabComplete(packet: CPacketTabComplete)
  fun handleUpdateSign(packet: CPacketUpdateSign)
  fun handleUseEntity(packet: CPacketUseEntity)

}
