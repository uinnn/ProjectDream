package dream.server.management

import dream.serializer.JsonLikeSerializer
import dream.utils.stringOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

@Serializable(IPBanEntry.Serializer::class)
class IPBanEntry : BanEntry<String> {
  constructor(data: JsonObject) : super(data.stringOrNull("ip"), data)

  constructor(
    value: String?,
    startDate: Date = Date(),
    expirationDate: Date? = null,
    bannedBy: String = "(Unknown)",
    reason: String = "Banned by an operator.",
  ) : super(value, startDate, expirationDate, bannedBy, reason)

  override fun serialize(data: JsonObjectBuilder) {
    if (value != null) {
      data.put("ip", value)
      super.serialize(data)
    }
  }

  override fun deserialize(data: JsonObject) {
    value = data.stringOrNull("ip")
    super.deserialize(data)
  }

  object Serializer : JsonLikeSerializer<IPBanEntry>() {
    override fun createObject(data: JsonObject) = IPBanEntry(data)
  }
}
