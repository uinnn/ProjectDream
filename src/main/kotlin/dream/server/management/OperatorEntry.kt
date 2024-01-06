package dream.server.management

import com.mojang.authlib.GameProfile
import dream.serializer.JsonLikeSerializer
import dream.utils.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable(OperatorEntry.Serializer::class)
class OperatorEntry(
  override var value: GameProfile?,
  var permissionLevel: Int = 0,
  var bypassLimit: Boolean = false,
) : UserEntry<GameProfile> {

  constructor(data: JsonObject) :
    this(profileOf(data), data.int("level", 0), data.boolean("bypassesPlayerLimit", false))

  override fun serialize(data: JsonObjectBuilder) {
    value?.let { profile ->
      profile.storeAt(data)
      data.put("level", permissionLevel)
      data.put("bypassesPlayerLimit", bypassLimit)
    }
  }

  override fun deserialize(data: JsonObject) {
    value = profileOf(data)
    permissionLevel = data.int("level", 0)
    bypassLimit = data.boolean("bypassesPlayerLimit", false)
  }

  object Serializer : JsonLikeSerializer<OperatorEntry>() {
    override fun createObject(data: JsonObject) = OperatorEntry(data)
  }
}
