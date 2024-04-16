package dream.server

import com.mojang.authlib.*
import com.mojang.authlib.minecraft.*
import com.mojang.authlib.yggdrasil.*
import dream.api.*
import dream.app.Console
import dream.chat.*
import dream.coroutines.*
import dream.level.*
import dream.level.provider.*
import dream.level.storage.*
import dream.misc.*
import dream.network.*
import dream.server.management.*
import dream.utils.*
import korlibs.io.async.launch
import kotlinx.coroutines.delay
import java.io.*
import java.net.*
import kotlin.coroutines.*

/**
 * The core execution of a Minecraft server.
 *
 * Any server has one instance of this class, and any server can create
 * own implementation of this class.
 */
@Open
class Server(val proxy: Proxy, val directory: File, profileDirectory: File = directory) : Scope, Tickable {
  companion object {
    /**
     * The instance of server.
     */
    private lateinit var server: Server

    /**
     * Get the instance of the server.
     */
    fun get() = server
  }

  override var coroutineContext: CoroutineContext = MinecraftDispatcher

  var tick = 0

  /**
   * The network system of this server.
   */
  val networkSystem = NetworkSystem(this)

  /**
   * The authentication service of this server.
   */
  val authService = YggdrasilAuthenticationService(proxy, randomUUID().toString())
  val sessionService: MinecraftSessionService = authService.createMinecraftSessionService()
  val profileRepository: GameProfileRepository = authService.createProfileRepository()

  /**
   * The profile cache of this server.
   */
  val profileCache = ProfileCache(this, profileDirectory)

  /**
   * Gets all levels loaded on this server.
   */
  val levels: MutableList<Level> = ArrayList()
  val mainLevel get() = levels[0]

  /**
   * If the server only supports original players.
   */
  var isOnlineMode = false

  /**
   * Gets the server view distance for rendering chunks.
   */
  var viewDistance = 8

  /**
   * The [PlayerList] instance of this server.
   */
  var playerList = PlayerList(this)

  /**
   * The status response of this server.
   */
  var statusResponse = createStatusResponse()

  /**
   * Determinates if the server should use native transport on networks.
   */
  fun shouldUseNativeTransport(): Boolean {
    return true
  }

  /**
   * Sends a message to the server.
   *
   * Defaults go to logging in the console.
   */
  fun sendMessage(component: Component) {
    Console.message(component)
  }

  /**
   * Creates and returns the status response for this server.
   */
  fun createStatusResponse(): ServerStatusResponse {
    return ServerStatusResponse(
      Description("§aPrototype I"),
      ProtocolVersion("Dream", 47),
      PlayerCountData(-1, 0),
      ""
    )
  }

  init {
    val level = DefaultLevel("World", LevelProvider(), SaveHandler(file("world")))
    playerList.initPlayerStorage(level)
    levels += level
  }

  init {
    launch {
      while (true) {
        delay(50)
        tick(tick)
        tick++
      }
    }
  }

  override fun tick(partial: Int) {
    networkSystem.tick(partial)
    for (level in levels) {
      level.tick(partial)
    }
  }

}
