package dream.utils

import com.mojang.authlib.*
import kotlinx.serialization.json.*
import java.util.*

fun profileOf(data: JsonObject): GameProfile? {
  val id = data.stringOrNull("uuid")?.toUUID() ?: return null
  val name = data.stringOrNull("name") ?: return null
  return GameProfile(id, name)
}

fun GameProfile.storeAt(data: JsonObjectBuilder) {
  data.put("uuid", id?.toString() ?: "")
  data.put("name", name)
}

fun GameProfile.toJson(): JsonObject {
  return buildJsonObject {
    storeAt(this)
  }
}

fun GameProfile.getIdOrOffline(): UUID {
  return id ?: OfflineUUID(name)
}
