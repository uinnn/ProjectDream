package dream.network

import com.mojang.authlib.GameProfile
import dream.serializer.ProfileSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ProfileList = List<@Serializable(ProfileSerializer::class) GameProfile>

/**
 * Represents a server status response.
 *
 * This is sended to player when he's in server list.
 *
 * @see ProtocolVersion
 * @see PlayerCountData
 */
@Serializable
data class ServerStatusResponse(
  @SerialName("description") var motd: Description,
  @SerialName("version") var protocol: ProtocolVersion,
  @SerialName("players") var playerCount: PlayerCountData,
  @SerialName("favicon") var icon: String? = null,
)

@Serializable
data class Description(var text: String)

/**
 * Data stats for protocol version of a server.
 *
 * Used in [ServerStatusResponse].
 */
@Serializable
data class ProtocolVersion(var name: String, var protocol: Int)

/**
 * Data stats for player count of a server.
 *
 * Used in [ServerStatusResponse].
 */
@Serializable
data class PlayerCountData(
  @SerialName("max") var maxPlayers: Int,
  @SerialName("online") var onlinePlayers: Int,
  @SerialName("sample") var players: ProfileList = emptyList(),
)
