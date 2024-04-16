package dream.server

import dream.api.*
import dream.chat.*
import dream.entity.player.*
import dream.level.*
import dream.level.storage.*
import dream.misc.*
import dream.nbt.types.*
import dream.network.*
import dream.packet.game.*
import dream.pos.*
import dream.server.management.*
import dream.stats.*
import dream.utils.*
import java.io.*
import java.util.*
import kotlin.collections.set

/**
 * A list for players.
 *
 * Any server has an instance of this class.
 *
 * Player list is used to store players in the server.
 */
@Open
class PlayerList(val server: Server) : Tickable, PlayerStorage {
  companion object {
    val BANNED_PLAYERS_FILE = file("banned-players.json")
    val BANNED_IPS_FILE = file("banned-ips.json")
    val OPS_FILE = file("ops.json")
    val WHITELIST_FILE = file("whitelist.json")
  }

  /**
   * All players stored by their id.
   */
  val players = HashMap<UUID, Player>()

  /**
   * Gets all players actually connected.
   */
  val connectedPlayers get() = players.values

  /**
   * A storage for player with their stats.
   */
  val playerStats = HashMap<UUID, StatsFile>()

  /**
   * The [PlayerStorage] reference for this.
   */
  lateinit var playerStorage: PlayerStorage

  /**
   * Gets all players actually connected by their id.
   */
  val ids = players.keys

  /**
   * Gets all players actually connected by name.
   */
  val usernames get() = connectedPlayers.map { it.name }
  override val availablePlayerDatas get() = usernames

  /**
   * Gets all players actually connected by profile.
   */
  val profiles get() = connectedPlayers.map { it.profile }

  /**
   * Gets the banned players list.
   */
  val bannedPlayers = ProfileBanList(BANNED_PLAYERS_FILE)

  /**
   * Gets the banned IP's list.
   */
  val bannedIps = IPBanList(BANNED_IPS_FILE)

  /**
   * Gets the operators list.
   */
  val operators = OperatorsList(OPS_FILE)

  /**
   * Gets the whitelist.
   */
  val whitelist = Whitelist(WHITELIST_FILE)

  /**
   * If the whitelist is enabled or not.
   */
  var isWhitelistEnabled by whitelist::isEnabled

  /**
   * Gets the max players connected at the same time.
   */
  var maxPlayers = 8

  /**
   * Gets the current connected players size.
   */
  val playersCount get() = players.size

  /**
   * Gets the delay between the update latency packet for all players.
   *
   * Defaults go to 600 ticks (30 seconds)
   */
  var pingDelay = 600

  /**
   * Ticks this player list sending latency packet for players.
   */
  override fun tick(partial: Int) {
    if (partial % pingDelay == 0) {
      //sendPacketToAll(serverpacketp)
    }
  }

  /**
   * Writes the player data to disk.
   */
  override fun write(player: Player) {
    playerStorage.write(player)
  }

  /**
   * Reads the player data from disk
   */
  override fun read(player: Player): CompoundTag {
    return playerStorage.read(player)
  }

  /**
   * Gets the player file for [player].
   */
  override fun getPlayerFile(player: Player): File {
    return playerStorage.getPlayerFile(player)
  }

  /**
   * Checks if [profile] is already connected and kicks all.
   */
  fun checkMultipleLogins(profile: Profile, disconnectReason: String = "You logged in from another location"): Boolean {
    var hasMultipleLogin = false
    val id = profile.id ?: OfflineUUID(profile.name)

    forEachPlayer {
      if (it.id == id) {
        it.disconnect(disconnectReason)
        hasMultipleLogin = true
      }
    }

    find(id)?.let {
      it.disconnect(disconnectReason)
      hasMultipleLogin = true
    }

    return hasMultipleLogin
  }

  /**
   * Creates a [Player] instance for the specified [profile].
   */
  fun createPlayer(profile: Profile, network: NetworkManager): Player {
    println("trying to create player...")
    val connection = PlayerConnection(server, network)
    println("created connection")
    val interaction = PlayerInteraction(server.mainLevel)
    println("created interaction")
    val player = Player(profile, server, connection, interaction)
    println("created player")
    connection.player = player
    network.handler = connection
    interaction.player = player
    println("setted everything")
    return player
  }

  /**
   * Initializes the connection for [player].
   */
  fun initConnection(network: NetworkManager, player: Player) {
    println("trying to init player connection...")
    //server.profileCache.addEntry(player.profile)
    //read(player)

    val world = server.mainLevel

    println("initializing ${player.name} to the server")

    player.apply {
      level = world
      interaction.level = world

      sendPacket(SPacketJoin(player.serialId, false, Gamemode.CREATIVE, 0, Difficulty.EASY, 3, LevelType.DEFAULT, true))
      sendPacket(SPacketPayload("MC|Brand", packetBuffer { writeString("ProjectDream") }))
      sendPacket(SPacketSpawnPos(Pos(0, 120, 0)))
      sendPacket(SPacketAbilities(false, false, true, false, 0.25f, 0.25f))
      sendPacket(SPacketHeldItemChange(heldSlot))

      openContainer.addListener(this)
    }

    println("joined ${player.name} to the server")

    onLogin(player)
  }

  /**
   * Called when [player] logins in the server.
   */
  fun onLogin(player: Player) {
    println("adding player to server list...")
    players[player.id] = player
    val level = player.level
    level.spawn(player)
    println("added player to server list")
  }

  /**
   * Called when [player] loggout from the server.
   */
  fun onLoggout(player: Player) {
    write(player)
    player.level.removeEntity(player)
    players -= player.id
  }

  /**
   * Find a player by [id].
   */
  fun find(id: UUID) = players[id]

  /**
   * Find a player by [name].
   */
  fun find(name: String) = players.findValue { it.name.equals(name, true) }

  /**
   * Find a player by [profile].
   */
  fun find(profile: Profile) = if (profile.id != null) find(profile.id) else find(profile.name)

  /**
   * Initializes [playerStorage] based on [level].
   */
  fun initPlayerStorage(level: Level) {
    playerStorage = level.playerStorage
  }

  /**
   * Gets a list of players that address is matching with [address].
   */
  fun getPlayersMatchingAddress(address: String) = connectedPlayers.filter { it.ip == address }

  /**
   * Executes [action] for every player connected.
   */
  final inline fun forEachPlayer(action: (player: Player) -> Unit) = connectedPlayers.forEach(action)

  /**
   * Send [packet] to all players connected.
   */
  fun sendPacketToAll(packet: HandledPacket) = forEachPlayer { it.sendPacket(packet) }

  /**
   * Send [packet] to all nearby players connected.
   */
  fun sendPacketToAllNear(packet: HandledPacket, x: Double, y: Double, z: Double, radius: Double, level: Level) {
    forEachPlayer { player ->
      if (player.level != level && !player.isNear(x, y, z, radius)) {
        player.sendPacket(packet)
      }
    }
  }

  /**
   * Send [packet] to all nearby players connected except the given player [except].
   */
  fun sendPacketToAllNearExcept(
    except: Player, packet: HandledPacket, x: Double, y: Double, z: Double, radius: Double, level: Level
  ) = forEachPlayer { player ->
    if (player == except && player.level != level && !player.isNear(x, y, z, radius)) {
      player.sendPacket(packet)
    }
  }

  /**
   * Sends [message] to all players connected.
   */
  fun sendChat(message: ComponentText, isChat: Boolean = true) {
    server.sendMessage(message)
    sendPacketToAll(SPacketChat(message, if (isChat) 1 else 0))
  }

  /**
   * Save all players data.
   */
  fun savePlayers() = forEachPlayer(::write)

  /**
   * Disconnect all players with a reason.
   */
  fun disconnectPlayers(reason: String = "Server Closed") = forEachPlayer { it.disconnect(reason) }

  fun setWhitelisted(profile: Profile) = whitelist.addEntry(WhitelistEntry(profile))
  fun setWhitelisted(player: Player) = setWhitelisted(player.profile)
  fun removeWhitelisted(profile: Profile) = whitelist.removeEntry(profile)
  fun removeWhitelisted(player: Player) = removeWhitelisted(player.profile)

  fun setOp(profile: Profile) = operators.addEntry(OperatorEntry(profile))
  fun setOp(player: Player) = setOp(player.profile)
  fun removeOp(profile: Profile) = operators.removeEntry(profile)
  fun removeOp(player: Player) = removeOp(player.profile)

}
