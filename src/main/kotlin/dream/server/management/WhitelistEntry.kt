package dream.server.management

import com.mojang.authlib.GameProfile
import dream.serializer.JsonLikeSerializer
import dream.utils.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable(WhitelistEntry.Serializer::class)
class WhitelistEntry(override var value: GameProfile?) : UserEntry<GameProfile> {
  constructor(data: JsonObject) : this(profileOf(data))

  override fun serialize(data: JsonObjectBuilder) {
    value?.storeAt(data)
  }

  override fun deserialize(data: JsonObject) {
    value = profileOf(data)
  }

  object Serializer : JsonLikeSerializer<WhitelistEntry>() {
    override fun createObject(data: JsonObject) = WhitelistEntry(data)
  }
}
