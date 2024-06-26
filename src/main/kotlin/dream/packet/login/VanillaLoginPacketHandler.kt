package dream.packet.login

import dream.api.Tickable
import dream.app.message
import dream.chat.Component
import dream.chat.text
import dream.network.NetworkManager
import dream.server.Server
import dream.utils.OfflineUUID
import dream.utils.Profile

class VanillaLoginPacketHandler(
  val network: NetworkManager,
  val server: Server = Server.get()
) : LoginPacketHandler, Tickable {

  /**
   * The profile trying to login.
   */
  lateinit var profile: Profile

  /**
   * Checks if the profile has been initialized.
   */
  val hasProfile get() = ::profile.isInitialized

  /**
   * The current state of the login.
   */
  var state = LoginState.HELLO

  /**
   * The tracked timer for the login. It will timeout after 600 ticks
   */
  var timer = 0

  /**
   * Gets the connection info for this login based on player profile trying to login.
   */
  val connectionInfo: String
    get() {
      val address = network.address.toString()
      return if (hasProfile) "${profile.name} ($address)" else address
    }

  override fun handleLoginStart(packetIn: CPacketLoginStart) {
    require(state == LoginState.HELLO) { "Unexpected login start packet" }
    profile = packetIn.profile
    state = LoginState.READY_TO_ACCEPT
  }

  override fun handleEncryptionResponse(packetIn: CPacketEncryptionResponse) {
    println("received encryption packet $packetIn")
  }

  override fun onDisconnect(reason: Component) {
    message("$connectionInfo lost connection: ${reason.unformattedText}")
  }

  override fun tick(partial: Int) {
    if (state == LoginState.READY_TO_ACCEPT) {
      tryAccept()
    }

    if (timer++ > 600) {
      val text = text("Login timed out.")
      message(text)
      network.disconnect(text)
    }
  }

  /**
   * Try accepts the login request for the player.
   */
  fun tryAccept() {
    if (!profile.isComplete) {
      profile = Profile(OfflineUUID(profile.name), profile.name)
    }

    // TODO: check for whitelist/ban and others filters

    state = LoginState.ACCEPTED
    network.sendPacket(SPacketLoginSuccess(profile))

    println("accepted login request for $profile")
    val player = server.playerList.createPlayer(profile, network)
    println("player created")

    server.playerList.initConnection(network, player)
  }
}

/**
 * All possible login states for [LoginPacketHandler]
 */
enum class LoginState {
  HELLO,
  KEY,
  AUTHENTICATING,
  READY_TO_ACCEPT,
  DELAY_ACCEPT,
  ACCEPTED
}
