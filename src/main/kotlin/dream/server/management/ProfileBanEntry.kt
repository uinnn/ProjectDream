package dream.server.management

import com.mojang.authlib.GameProfile
import dream.serializer.JsonLikeSerializer
import dream.utils.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

@Serializable(ProfileBanEntry.Serializer::class)
class ProfileBanEntry : BanEntry<GameProfile> {
  constructor(data: JsonObject) : super(profileOf(data), data)

  constructor(
    value: GameProfile?,
    startDate: Date = Date(),
    expirationDate: Date? = null,
    bannedBy: String = "(Unknown)",
    reason: String = "Banned by an operator.",
  ) : super(value, startDate, expirationDate, bannedBy, reason)

  override fun serialize(data: JsonObjectBuilder) {
    if (value != null) {
      value!!.storeAt(data)
      super.serialize(data)
    }
  }

  override fun deserialize(data: JsonObject) {
    value = profileOf(data)
    super.deserialize(data)
  }

  object Serializer : JsonLikeSerializer<ProfileBanEntry>() {
    override fun createObject(data: JsonObject) = ProfileBanEntry(data)
  }
}
