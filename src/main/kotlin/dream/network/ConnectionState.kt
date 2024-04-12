package dream.network

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import dream.packet.game.CPacketAbilities
import dream.packet.game.CPacketAnimation
import dream.packet.game.CPacketBlockPlace
import dream.packet.game.CPacketChat
import dream.packet.game.CPacketClickWindow
import dream.packet.game.CPacketCloseWindow
import dream.packet.game.CPacketConfirmTransaction
import dream.packet.game.CPacketCreativeAction
import dream.packet.game.CPacketDig
import dream.packet.game.CPacketEnchantItem
import dream.packet.game.CPacketEntityAction
import dream.packet.game.CPacketHeldItemChange
import dream.packet.game.CPacketInput
import dream.packet.game.CPacketKeepAlive
import dream.packet.game.CPacketPayload
import dream.packet.game.CPacketPlayerInfo
import dream.packet.game.CPacketPlayerLook
import dream.packet.game.CPacketPlayerMove
import dream.packet.game.CPacketPlayerMoveLook
import dream.packet.game.CPacketResourcePack
import dream.packet.game.CPacketSettings
import dream.packet.game.CPacketSpectate
import dream.packet.game.CPacketStatus
import dream.packet.game.CPacketTabComplete
import dream.packet.game.CPacketUpdateSign
import dream.packet.game.CPacketUseEntity
import dream.packet.game.SPacketAbilities
import dream.packet.game.SPacketAnimation
import dream.packet.game.SPacketBlockAction
import dream.packet.game.SPacketBlockChange
import dream.packet.game.SPacketBlockDigAnimation
import dream.packet.game.SPacketBorder
import dream.packet.game.SPacketCamera
import dream.packet.game.SPacketChangeGameState
import dream.packet.game.SPacketChat
import dream.packet.game.SPacketChunkBulk
import dream.packet.game.SPacketChunkData
import dream.packet.game.SPacketCloseWindow
import dream.packet.game.SPacketCollectItem
import dream.packet.game.SPacketCombat
import dream.packet.game.SPacketConfirmTransaction
import dream.packet.game.SPacketDestroyEntities
import dream.packet.game.SPacketDisplayScoreboard
import dream.packet.game.SPacketEffect
import dream.packet.game.SPacketEntityAttach
import dream.packet.game.SPacketEntityAttributes
import dream.packet.game.SPacketEntityEquipment
import dream.packet.game.SPacketEntityHeadLook
import dream.packet.game.SPacketEntityLook
import dream.packet.game.SPacketEntityMetadata
import dream.packet.game.SPacketEntityMove
import dream.packet.game.SPacketEntityMoveLook
import dream.packet.game.SPacketEntityMovement
import dream.packet.game.SPacketEntityStatus
import dream.packet.game.SPacketEntityTag
import dream.packet.game.SPacketEntityTeleport
import dream.packet.game.SPacketEntityVelocity
import dream.packet.game.SPacketExplosion
import dream.packet.game.SPacketHeldItemChange
import dream.packet.game.SPacketKeepAlive
import dream.packet.game.SPacketMaps
import dream.packet.game.SPacketMultiBlockChange
import dream.packet.game.SPacketOpenWindow
import dream.packet.game.SPacketParticle
import dream.packet.game.SPacketPayload
import dream.packet.game.SPacketPlayerListInfo
import dream.packet.game.SPacketPlayerListItem
import dream.packet.game.SPacketPosLook
import dream.packet.game.SPacketRemoveEffect
import dream.packet.game.SPacketResourcePack
import dream.packet.game.SPacketRespawn
import dream.packet.game.SPacketScoreboardObjective
import dream.packet.game.SPacketServerDifficulty
import dream.packet.game.SPacketSetExperience
import dream.packet.game.SPacketSetSlot
import dream.packet.game.SPacketSignEditor
import dream.packet.game.SPacketSound
import dream.packet.game.SPacketSpawnEntity
import dream.packet.game.SPacketSpawnExp
import dream.packet.game.SPacketSpawnMob
import dream.packet.game.SPacketSpawnObject
import dream.packet.game.SPacketSpawnPainting
import dream.packet.game.SPacketSpawnPlayer
import dream.packet.game.SPacketSpawnPos
import dream.packet.game.SPacketStatistics
import dream.packet.game.SPacketTabComplete
import dream.packet.game.SPacketTeams
import dream.packet.game.SPacketTimeUpdate
import dream.packet.game.SPacketTitle
import dream.packet.game.SPacketUpdateHealth
import dream.packet.game.SPacketUpdateScore
import dream.packet.game.SPacketUpdateSign
import dream.packet.game.SPacketUpdateTile
import dream.packet.game.SPacketUseBed
import dream.packet.game.SPacketWindowItems
import dream.packet.game.SPacketWindowProperty
import dream.packet.handshaking.PacketHandshake
import dream.packet.login.*
import dream.packet.status.CPacketPing
import dream.packet.status.CPacketServerQuery
import dream.packet.status.SPacketPong
import dream.packet.status.SPacketServerInfo
import dream.utils.enumMap
import dream.utils.newInstance
import kotlin.collections.set
import kotlin.reflect.KClass

typealias HandledPacketClass = KClass<out HandledPacket>
typealias DirectionMap = MutableMap<PacketDirection, BiMap<Int, HandledPacketClass>>

/**
 * Represents various states of a packet connection.
 */
enum class ConnectionState(val id: Int) {

  /**
   * Handshaking connection state.
   *
   * Server direction Packets:
   * * [PacketHandshake]
   */
  HANDSHAKING(-1) {
    init {
      registerPacket(PacketDirection.SERVER, PacketHandshake::class)
    }
  },

  /**
   * Play connection state.
   *
   * Server direction Packets:
   * * [PacketHandshake]
   *
   * Client direction Packets:
   * * [PacketHandshake]
   */
  PLAY(0) {
    init {
      // order is important
      registerPacket(PacketDirection.CLIENT, SPacketKeepAlive::class)
      registerPacket(PacketDirection.CLIENT, SPacketChat::class)
      registerPacket(PacketDirection.CLIENT, SPacketTimeUpdate::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityEquipment::class)
      registerPacket(PacketDirection.CLIENT, SPacketSpawnPos::class)
      registerPacket(PacketDirection.CLIENT, SPacketUpdateHealth::class)
      registerPacket(PacketDirection.CLIENT, SPacketRespawn::class)
      registerPacket(PacketDirection.CLIENT, SPacketPosLook::class)
      registerPacket(PacketDirection.CLIENT, SPacketHeldItemChange::class)
      registerPacket(PacketDirection.CLIENT, SPacketUseBed::class)
      registerPacket(PacketDirection.CLIENT, SPacketAnimation::class)
      registerPacket(PacketDirection.CLIENT, SPacketSpawnPlayer::class)
      registerPacket(PacketDirection.CLIENT, SPacketCollectItem::class)
      registerPacket(PacketDirection.CLIENT, SPacketSpawnObject::class)
      registerPacket(PacketDirection.CLIENT, SPacketSpawnMob::class)
      registerPacket(PacketDirection.CLIENT, SPacketSpawnPainting::class)
      registerPacket(PacketDirection.CLIENT, SPacketSpawnExp::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityVelocity::class)
      registerPacket(PacketDirection.CLIENT, SPacketDestroyEntities::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityMove::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityMovement::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityLook::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityMoveLook::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityTeleport::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityHeadLook::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityStatus::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityAttach::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityMetadata::class)
      registerPacket(PacketDirection.CLIENT, SPacketEffect::class)
      registerPacket(PacketDirection.CLIENT, SPacketRemoveEffect::class)
      registerPacket(PacketDirection.CLIENT, SPacketSetExperience::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityAttributes::class)
      registerPacket(PacketDirection.CLIENT, SPacketChunkData::class)
      registerPacket(PacketDirection.CLIENT, SPacketMultiBlockChange::class)
      registerPacket(PacketDirection.CLIENT, SPacketBlockChange::class)
      registerPacket(PacketDirection.CLIENT, SPacketBlockAction::class)
      registerPacket(PacketDirection.CLIENT, SPacketBlockDigAnimation::class)
      registerPacket(PacketDirection.CLIENT, SPacketChunkBulk::class)
      registerPacket(PacketDirection.CLIENT, SPacketExplosion::class)
      //registerPacket(PacketDirection.CLIENT, SPacketEffect::class)
      registerPacket(PacketDirection.CLIENT, SPacketSound::class)
      registerPacket(PacketDirection.CLIENT, SPacketParticle::class)
      registerPacket(PacketDirection.CLIENT, SPacketChangeGameState::class)
      registerPacket(PacketDirection.CLIENT, SPacketSpawnEntity::class)
      registerPacket(PacketDirection.CLIENT, SPacketOpenWindow::class)
      registerPacket(PacketDirection.CLIENT, SPacketCloseWindow::class)
      registerPacket(PacketDirection.CLIENT, SPacketSetSlot::class)
      registerPacket(PacketDirection.CLIENT, SPacketWindowItems::class)
      registerPacket(PacketDirection.CLIENT, SPacketWindowProperty::class)
      registerPacket(PacketDirection.CLIENT, SPacketConfirmTransaction::class)
      registerPacket(PacketDirection.CLIENT, SPacketUpdateSign::class)
      registerPacket(PacketDirection.CLIENT, SPacketMaps::class)
      registerPacket(PacketDirection.CLIENT, SPacketUpdateTile::class)
      registerPacket(PacketDirection.CLIENT, SPacketSignEditor::class)
      registerPacket(PacketDirection.CLIENT, SPacketStatistics::class)
      registerPacket(PacketDirection.CLIENT, SPacketPlayerListItem::class)
      registerPacket(PacketDirection.CLIENT, SPacketAbilities::class)
      registerPacket(PacketDirection.CLIENT, SPacketTabComplete::class)
      registerPacket(PacketDirection.CLIENT, SPacketScoreboardObjective::class)
      registerPacket(PacketDirection.CLIENT, SPacketUpdateScore::class)
      registerPacket(PacketDirection.CLIENT, SPacketDisplayScoreboard::class)
      registerPacket(PacketDirection.CLIENT, SPacketTeams::class)
      registerPacket(PacketDirection.CLIENT, SPacketPayload::class)
      registerPacket(PacketDirection.CLIENT, SPacketDisconnect::class)
      registerPacket(PacketDirection.CLIENT, SPacketServerDifficulty::class)
      registerPacket(PacketDirection.CLIENT, SPacketCombat::class)
      registerPacket(PacketDirection.CLIENT, SPacketCamera::class)
      registerPacket(PacketDirection.CLIENT, SPacketBorder::class)
      registerPacket(PacketDirection.CLIENT, SPacketTitle::class)
      registerPacket(PacketDirection.CLIENT, SPacketEnableCompression::class)
      registerPacket(PacketDirection.CLIENT, SPacketPlayerListInfo::class)
      registerPacket(PacketDirection.CLIENT, SPacketResourcePack::class)
      registerPacket(PacketDirection.CLIENT, SPacketEntityTag::class)
      registerPacket(PacketDirection.SERVER, CPacketKeepAlive::class)
      registerPacket(PacketDirection.SERVER, CPacketChat::class)
      registerPacket(PacketDirection.SERVER, CPacketUseEntity::class)
      registerPacket(PacketDirection.SERVER, CPacketPlayerInfo::class)
      registerPacket(PacketDirection.SERVER, CPacketPlayerMove::class)
      registerPacket(PacketDirection.SERVER, CPacketPlayerLook::class)
      registerPacket(PacketDirection.SERVER, CPacketPlayerMoveLook::class)
      registerPacket(PacketDirection.SERVER, CPacketDig::class)
      registerPacket(PacketDirection.SERVER, CPacketBlockPlace::class)
      registerPacket(PacketDirection.SERVER, CPacketHeldItemChange::class)
      registerPacket(PacketDirection.SERVER, CPacketAnimation::class)
      registerPacket(PacketDirection.SERVER, CPacketEntityAction::class)
      registerPacket(PacketDirection.SERVER, CPacketInput::class)
      registerPacket(PacketDirection.SERVER, CPacketCloseWindow::class)
      registerPacket(PacketDirection.SERVER, CPacketClickWindow::class)
      registerPacket(PacketDirection.SERVER, CPacketConfirmTransaction::class)
      registerPacket(PacketDirection.SERVER, CPacketCreativeAction::class)
      registerPacket(PacketDirection.SERVER, CPacketEnchantItem::class)
      registerPacket(PacketDirection.SERVER, CPacketUpdateSign::class)
      registerPacket(PacketDirection.SERVER, CPacketAbilities::class)
      registerPacket(PacketDirection.SERVER, CPacketTabComplete::class)
      registerPacket(PacketDirection.SERVER, CPacketSettings::class)
      registerPacket(PacketDirection.SERVER, CPacketStatus::class)
      registerPacket(PacketDirection.SERVER, CPacketPayload::class)
      registerPacket(PacketDirection.SERVER, CPacketSpectate::class)
      registerPacket(PacketDirection.SERVER, CPacketResourcePack::class)
    }
  },

  /**
   * Status connection state.
   *
   * Server direction Packets:
   * * [CPacketServerQuery]. Id: 0
   * * [CPacketPing]. Id: 2
   *
   * Client direction Packets:
   * * [SPacketServerInfo]. Id: 1
   * * [SPacketPong]. Id: 3
   */
  STATUS(1) {
    init {
      // order is important
      registerPacket(PacketDirection.SERVER, CPacketServerQuery::class)
      registerPacket(PacketDirection.CLIENT, SPacketServerInfo::class)
      registerPacket(PacketDirection.SERVER, CPacketPing::class)
      registerPacket(PacketDirection.CLIENT, SPacketPong::class)
    }
  },

  /**
   * Login connection state.
   *
   * Server direction Packets:
   * * [CPacketLoginStart]. Id: 4
   * * [CPacketEncryptionResponse]. Id: 5
   *
   * Client direction Packets:
   * * [SPacketDisconnect]. Id: 0
   * * [SPacketEncryptionRequest]. Id: 1
   * * [SPacketLoginSuccess]. Id: 2
   * * [SPacketEnableCompression]. Id: 3
   */
  LOGIN(2) {
    init {
      // order is important
      registerPacket(PacketDirection.CLIENT, SPacketDisconnect::class)
      registerPacket(PacketDirection.CLIENT, SPacketEncryptionRequest::class)
      registerPacket(PacketDirection.CLIENT, SPacketLoginSuccess::class)
      registerPacket(PacketDirection.CLIENT, SPacketEnableCompression::class)
      registerPacket(PacketDirection.SERVER, CPacketLoginStart::class)
      registerPacket(PacketDirection.SERVER, CPacketEncryptionResponse::class)
    }
  };

  /**
   * All packet directions stored in this connection state.
   */
  val directions: DirectionMap = enumMap()

  /**
   * Gets a packet id from this connection state provided in [direction] and [packetClass].
   */
  fun id(direction: PacketDirection, packetClass: HandledPacketClass): Int? {
    return directions[direction]?.inverse()?.get(packetClass)
  }

  /**
   * Gets a packet id from this connection state provided in [direction] and [packet] class.
   */
  fun id(direction: PacketDirection, packet: HandledPacket): Int? {
    return id(direction, packet::class)
  }

  /**
   * Creates a new packet instance from the given [direction] and [id].
   */
  fun createPacket(direction: PacketDirection, id: Int, buffer: PacketBuffer): HandledPacket {
    val clazz = directions[direction]?.get(id) ?: error("Unable to create packet with id $id")
    return clazz.objectInstance ?: clazz.newInstance(buffer)
  }

  /**
   * Creates a new packet instance from the given [direction].
   */
  fun createPacket(direction: PacketDirection, buffer: PacketBuffer): HandledPacket {
    return createPacket(direction, buffer.readVarInt(), buffer)
  }

  /**
   * Register the specified [packetClass] to [direction] in this connection state.
   *
   * ### Note:
   * Unlike in NMS, this doesn't prohibite you for overriding any packet
   * or registering news packet in the server.
   *
   * Although it's more propend to have an error if not satisfy equals than client packets.
   */
  fun registerPacket(direction: PacketDirection, packetClass: HandledPacketClass) {
    val map = directions.getOrPut(direction) { HashBiMap.create() }
    map[map.size] = packetClass
  }

  companion object {
    val classLookup = HashMap<HandledPacketClass, ConnectionState>()

    init {
      for (state in entries) {
        for (value in state.directions.values) {
          for (classes in value.values) {
            classLookup[classes] = state
          }
        }
      }
    }

    /**
     * Gets a connection state by id.
     */
    fun byId(id: Int) = entries[id % 4]

    /**
     * Gets a connection state by class.
     */
    fun byClass(clazz: HandledPacketClass) = classLookup[clazz] ?: HANDSHAKING

    /**
     * Gets a connection state by packet class.
     */
    fun byPacket(packet: HandledPacket) = byClass(packet::class)
  }
}
