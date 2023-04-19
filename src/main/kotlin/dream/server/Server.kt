package dream.server

import dream.coroutines.*
import dream.interfaces.*
import dream.level.*
import dream.misc.*
import dream.network.*
import kotlin.coroutines.*
import java.io.*
import java.net.*

/**
 * The core execution of a Minecraft server.
 *
 * Any server has one instance of this class, and any server can create
 * own implementation of this class.
 */
@Open
abstract class Server(val proxy: Proxy, val directory: File, profileDirectory: File = directory) : Scope {
   companion object {
      /**
       * The instance of server.
       */
      internal lateinit var server: Server
   
      /**
       * Get the instance of the server.
       */
      fun get() = server
   }
   
   override var coroutineContext: CoroutineContext = MinecraftDispatcher
   
   /**
    * The network system of this server.
    */
   val networkSystem = NetworkSystem(this)
   
   /**
    * Gets all levels loaded on this server.
    */
   val levels: MutableList<Level> = ArrayList()
   
   /**
    * Determinates if the server should use native transport on networks.
    */
   fun shouldUseNativeTransport(): Boolean {
      return true
   }
}
