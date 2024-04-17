package dream.network

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import dream.packet.game.*
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
      registerPacket(PacketDirection.SERVER, 0, PacketHandshake::class)
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
      registerPacket(PacketDirection.CLIENT, 0, SPacketKeepAlive::class)
      registerPacket(PacketDirection.CLIENT, 1, SPacketJoin::class)
      registerPacket(PacketDirection.CLIENT, 2, SPacketChat::class)
      registerPacket(PacketDirection.CLIENT, 3, SPacketTimeUpdate::class)
      registerPacket(PacketDirection.CLIENT, 4, SPacketEntityEquipment::class)
      registerPacket(PacketDirection.CLIENT, 5, SPacketSpawnPos::class)
      registerPacket(PacketDirection.CLIENT, 6, SPacketUpdateHealth::class)
      registerPacket(PacketDirection.CLIENT, 7, SPacketRespawn::class)
      registerPacket(PacketDirection.CLIENT, 8, SPacketPosLook::class)
      registerPacket(PacketDirection.CLIENT, 9, SPacketHeldItemChange::class)
      registerPacket(PacketDirection.CLIENT, 10, SPacketUseBed::class)
      registerPacket(PacketDirection.CLIENT, 11, SPacketAnimation::class)
      registerPacket(PacketDirection.CLIENT, 12, SPacketSpawnPlayer::class)
      registerPacket(PacketDirection.CLIENT, 13, SPacketCollectItem::class)
      registerPacket(PacketDirection.CLIENT, 14, SPacketSpawnObject::class)
      registerPacket(PacketDirection.CLIENT, 15, SPacketSpawnMob::class)
      registerPacket(PacketDirection.CLIENT, 16, SPacketSpawnPainting::class)
      registerPacket(PacketDirection.CLIENT, 17, SPacketSpawnExp::class)
      registerPacket(PacketDirection.CLIENT, 18, SPacketEntityVelocity::class)
      registerPacket(PacketDirection.CLIENT, 19, SPacketDestroyEntities::class)
      registerPacket(PacketDirection.CLIENT, 20, SPacketEntityMove::class)
      registerPacket(PacketDirection.CLIENT, 21, SPacketEntityMovement::class)
      registerPacket(PacketDirection.CLIENT, 22, SPacketEntityLook::class)
      registerPacket(PacketDirection.CLIENT, 23, SPacketEntityMoveLook::class)
      registerPacket(PacketDirection.CLIENT, 24, SPacketEntityTeleport::class)
      registerPacket(PacketDirection.CLIENT, 25, SPacketEntityHeadLook::class)
      registerPacket(PacketDirection.CLIENT, 26, SPacketEntityStatus::class)
      registerPacket(PacketDirection.CLIENT, 27, SPacketEntityAttach::class)
      registerPacket(PacketDirection.CLIENT, 28, SPacketEntityMetadata::class)
      registerPacket(PacketDirection.CLIENT, 29, SPacketEntityEffect::class)
      registerPacket(PacketDirection.CLIENT, 30, SPacketRemoveEffect::class)
      registerPacket(PacketDirection.CLIENT, 31, SPacketSetExperience::class)
      registerPacket(PacketDirection.CLIENT, 32, SPacketEntityAttributes::class)
      registerPacket(PacketDirection.CLIENT, 33, SPacketChunkData::class)
      registerPacket(PacketDirection.CLIENT, 34, SPacketMultiBlockChange::class)
      registerPacket(PacketDirection.CLIENT, 35, SPacketBlockChange::class)
      registerPacket(PacketDirection.CLIENT, 36, SPacketBlockAction::class)
      registerPacket(PacketDirection.CLIENT, 37, SPacketBlockDigAnimation::class)
      registerPacket(PacketDirection.CLIENT, 38, SPacketChunkBulk::class)
      registerPacket(PacketDirection.CLIENT, 39, SPacketExplosion::class)
      registerPacket(PacketDirection.CLIENT, 40, SPacketEffect::class)
      registerPacket(PacketDirection.CLIENT, 41, SPacketSound::class)
      registerPacket(PacketDirection.CLIENT, 42, SPacketParticle::class)
      registerPacket(PacketDirection.CLIENT, 43, SPacketChangeGameState::class)
      registerPacket(PacketDirection.CLIENT, 44, SPacketSpawnEntity::class)
      registerPacket(PacketDirection.CLIENT, 45, SPacketOpenWindow::class)
      registerPacket(PacketDirection.CLIENT, 46, SPacketCloseWindow::class)
      registerPacket(PacketDirection.CLIENT, 47, SPacketSetSlot::class)
      registerPacket(PacketDirection.CLIENT, 48, SPacketWindowItems::class)
      registerPacket(PacketDirection.CLIENT, 49, SPacketWindowProperty::class)
      registerPacket(PacketDirection.CLIENT, 50, SPacketConfirmTransaction::class)
      registerPacket(PacketDirection.CLIENT, 51, SPacketUpdateSign::class)
      registerPacket(PacketDirection.CLIENT, 52, SPacketMaps::class)
      registerPacket(PacketDirection.CLIENT, 53, SPacketUpdateTile::class)
      registerPacket(PacketDirection.CLIENT, 54, SPacketSignEditor::class)
      registerPacket(PacketDirection.CLIENT, 55, SPacketStatistics::class)
      registerPacket(PacketDirection.CLIENT, 56, SPacketPlayerListItem::class)
      registerPacket(PacketDirection.CLIENT, 57, SPacketAbilities::class)
      registerPacket(PacketDirection.CLIENT, 58, SPacketTabComplete::class)
      registerPacket(PacketDirection.CLIENT, 59, SPacketScoreboardObjective::class)
      registerPacket(PacketDirection.CLIENT, 60, SPacketUpdateScore::class)
      registerPacket(PacketDirection.CLIENT, 61, SPacketDisplayScoreboard::class)
      registerPacket(PacketDirection.CLIENT, 62, SPacketTeams::class)
      registerPacket(PacketDirection.CLIENT, 63, SPacketPayload::class)
      registerPacket(PacketDirection.CLIENT, 64, SPacketDisconnect::class)
      registerPacket(PacketDirection.CLIENT, 65, SPacketServerDifficulty::class)
      registerPacket(PacketDirection.CLIENT, 66, SPacketCombat::class)
      registerPacket(PacketDirection.CLIENT, 67, SPacketCamera::class)
      registerPacket(PacketDirection.CLIENT, 68, SPacketBorder::class)
      registerPacket(PacketDirection.CLIENT, 69, SPacketTitle::class)
      registerPacket(PacketDirection.CLIENT, 70, SPacketEnableCompression::class)
      registerPacket(PacketDirection.CLIENT, 71, SPacketPlayerListInfo::class)
      registerPacket(PacketDirection.CLIENT, 72, SPacketResourcePack::class)
      registerPacket(PacketDirection.CLIENT, 73, SPacketEntityTag::class)
      registerPacket(PacketDirection.SERVER, 0, CPacketKeepAlive::class)
      registerPacket(PacketDirection.SERVER, 1, CPacketChat::class)
      registerPacket(PacketDirection.SERVER, 2, CPacketUseEntity::class)
      registerPacket(PacketDirection.SERVER, 3, CPacketPlayerMovement::class)
      registerPacket(PacketDirection.SERVER, 4, CPacketPlayerMove::class)
      registerPacket(PacketDirection.SERVER, 5, CPacketPlayerLook::class)
      registerPacket(PacketDirection.SERVER, 6, CPacketPlayerMoveLook::class)
      registerPacket(PacketDirection.SERVER, 7, CPacketDig::class)
      registerPacket(PacketDirection.SERVER, 8, CPacketBlockPlace::class)
      registerPacket(PacketDirection.SERVER, 9, CPacketHeldItemChange::class)
      registerPacket(PacketDirection.SERVER, 10, CPacketAnimation::class)
      registerPacket(PacketDirection.SERVER, 11, CPacketEntityAction::class)
      registerPacket(PacketDirection.SERVER, 12, CPacketInput::class)
      registerPacket(PacketDirection.SERVER, 13, CPacketCloseWindow::class)
      registerPacket(PacketDirection.SERVER, 14, CPacketClickWindow::class)
      registerPacket(PacketDirection.SERVER, 15, CPacketConfirmTransaction::class)
      registerPacket(PacketDirection.SERVER, 16, CPacketCreativeAction::class)
      registerPacket(PacketDirection.SERVER, 17, CPacketEnchantItem::class)
      registerPacket(PacketDirection.SERVER, 18, CPacketUpdateSign::class)
      registerPacket(PacketDirection.SERVER, 19, CPacketAbilities::class)
      registerPacket(PacketDirection.SERVER, 20, CPacketTabComplete::class)
      registerPacket(PacketDirection.SERVER, 21, CPacketSettings::class)
      registerPacket(PacketDirection.SERVER, 22, CPacketStatus::class)
      registerPacket(PacketDirection.SERVER, 23, CPacketPayload::class)
      registerPacket(PacketDirection.SERVER, 24, CPacketSpectate::class)
      registerPacket(PacketDirection.SERVER, 25, CPacketResourcePack::class)
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
      registerPacket(PacketDirection.CLIENT, 0, SPacketServerInfo::class)
      registerPacket(PacketDirection.CLIENT, 1, SPacketPong::class)
      registerPacket(PacketDirection.SERVER, 0, CPacketServerQuery::class)
      registerPacket(PacketDirection.SERVER, 1, CPacketPing::class)
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
      registerPacket(PacketDirection.CLIENT, 0, SPacketDisconnect::class)
      registerPacket(PacketDirection.CLIENT, 1, SPacketEncryptionRequest::class)
      registerPacket(PacketDirection.CLIENT, 2, SPacketLoginSuccess::class)
      registerPacket(PacketDirection.CLIENT, 3, SPacketEnableCompression::class)
      registerPacket(PacketDirection.SERVER, 0, CPacketLoginStart::class)
      registerPacket(PacketDirection.SERVER, 1, CPacketEncryptionResponse::class)
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
   * Creates a new packet instance from the given [direction] and [packetId].
   */
  fun createPacket(direction: PacketDirection, packetId: Int, buffer: PacketBuffer): HandledPacket {
    val clazz = directions[direction]?.get(packetId) ?: error("Unable to create packet with id $packetId")
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
  fun registerPacket(direction: PacketDirection, packetId: Int, packetClass: HandledPacketClass) {
    val map = directions.getOrPut(direction) { HashBiMap.create() }
    map[packetId] = packetClass
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

    const val MIN = -1
    const val MAX = 2

    /**
     * Gets a connection state by id.
     */
    fun byId(id: Int) = entries[id - MIN]

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
