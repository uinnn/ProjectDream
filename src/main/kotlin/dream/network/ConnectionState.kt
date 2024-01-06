package dream.network

import com.google.common.collect.*
import dream.packet.handshaking.*
import dream.packet.login.*
import dream.packet.status.*
import dream.utils.*
import kotlin.reflect.*

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
   * * [SPacketHandshake]
   */
  HANDSHAKING(-1) {
    init {
      registerPacket(PacketDirection.SERVER, SPacketHandshake::class)
    }
  },

  /**
   * Play connection state.
   *
   * Server direction Packets:
   * * [SPacketHandshake]
   *
   * Client direction Packets:
   * * [SPacketHandshake]
   */
  PLAY(0) {
    init {
      // order is important
      registerPacket(PacketDirection.SERVER, SPacketHandshake::class)
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
    return clazz.newInstance(buffer)
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
