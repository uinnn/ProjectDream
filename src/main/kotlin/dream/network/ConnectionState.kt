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
    * * [ServerPacketHandshake]
    */
   HANDSHAKING(-1) {
      init {
         registerPacket(PacketDirection.SERVER, ServerPacketHandshake::class)
      }
   },
   
   /**
    * Play connection state.
    *
    * Server direction Packets:
    * * [ServerPacketHandshake]
    *
    * Client direction Packets:
    * * [ServerPacketHandshake]
    */
   PLAY(0) {
      init {
         // order is important
         registerPacket(PacketDirection.SERVER, ServerPacketHandshake::class)
      }
   },
   
   /**
    * Status connection state.
    *
    * Server direction Packets:
    * * [ClientPacketServerQuery]. Id: 0
    * * [ClientPacketPing]. Id: 2
    *
    * Client direction Packets:
    * * [ServerPacketServerInfo]. Id: 1
    * * [ServerPacketPong]. Id: 3
    */
   STATUS(1) {
      init {
         // order is important
         registerPacket(PacketDirection.SERVER, ClientPacketServerQuery::class)
         registerPacket(PacketDirection.CLIENT, ServerPacketServerInfo::class)
         registerPacket(PacketDirection.SERVER, ClientPacketPing::class)
         registerPacket(PacketDirection.CLIENT, ServerPacketPong::class)
      }
   },
   
   /**
    * Login connection state.
    *
    * Server direction Packets:
    * * [ClientPacketLoginStart]. Id: 4
    * * [ClientPacketEncryptionResponse]. Id: 5
    *
    * Client direction Packets:
    * * [ServerPacketDisconnect]. Id: 0
    * * [ServerPacketEncryptionRequest]. Id: 1
    * * [ServerPacketLoginSuccess]. Id: 2
    * * [ServerPacketEnableCompression]. Id: 3
    */
   LOGIN(2) {
      init {
         // order is important
         registerPacket(PacketDirection.CLIENT, ServerPacketDisconnect::class)
         registerPacket(PacketDirection.CLIENT, ServerPacketEncryptionRequest::class)
         registerPacket(PacketDirection.CLIENT, ServerPacketLoginSuccess::class)
         registerPacket(PacketDirection.CLIENT, ServerPacketEnableCompression::class)
         registerPacket(PacketDirection.SERVER, ClientPacketLoginStart::class)
         registerPacket(PacketDirection.SERVER, ClientPacketEncryptionResponse::class)
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
      val map = directions[direction] ?: directions.put(direction, HashBiMap.create())!!
      map[map.size] = packetClass
   }
   
   companion object {
      private val ID_LOOKUP = IntObjectMap<ConnectionState>(4)
      private val CLASS_LOOKUP = HashMap<HandledPacketClass, ConnectionState>()
      
      /**
       * Gets the minimum possible id for connection state.
       */
      const val MIN_ID = -1
      
      /**
       * Gets the maximum possible id for connection state.
       */
      const val MAX_ID = 2
      
      init {
         for (state in values()) {
            ID_LOOKUP[state.id - MIN_ID] = state
            for (value in state.directions.values) {
               for (classes in value.values) {
                  CLASS_LOOKUP[classes] = state
               }
            }
         }
      }
      
      /**
       * Gets a connection state by id.
       */
      fun byId(id: Int) = ID_LOOKUP[id] ?: HANDSHAKING
      
      /**
       * Gets a connection state by class.
       */
      fun byClass(clazz: HandledPacketClass) = CLASS_LOOKUP[clazz] ?: HANDSHAKING
      
      /**
       * Gets a connection state by packet class.
       */
      fun byPacket(packet: HandledPacket) = byClass(packet::class)
   }
}
