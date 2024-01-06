package dream.server.management

import dream.utils.*
import kotlinx.serialization.json.*
import java.util.*

abstract class BanEntry<T>(
  override var value: T?,
  var startDate: Date = Date(),
  var expirationDate: Date? = null,
  var bannedBy: String = "(Unknown)",
  var reason: String = "Banned by an operator.",
) : UserEntry<T> {

  override val isExpired: Boolean get() = expirationDate != null && expirationDate!!.before(Date())

  /**
   * Gets if this ban entry is permanent.
   */
  val isPermanent get() = expirationDate == null

  constructor(value: T?, json: JsonObject) : this(
    value = value,
    startDate = FORMAT.parseOrNull(json.stringOrNull("created")) ?: Date(),
    expirationDate = FORMAT.parseOrNull(json.stringOrNull("expires")),
    bannedBy = json.string("source", "(Unknown)"),
    reason = json.string("reason", "Banned by an operator.")
  )

  override fun serialize(data: JsonObjectBuilder) {
    data.put("created", FORMAT.format(startDate))
    data.put("expires", if (isPermanent) "forever" else FORMAT.format(expirationDate))
    data.put("source", bannedBy)
    data.put("reason", reason)
  }

  override fun deserialize(data: JsonObject) {
    startDate = FORMAT.parseOrNull(data.stringOrNull("created")) ?: Date()
    expirationDate = FORMAT.parseOrNull(data.stringOrNull("expires"))
    bannedBy = data.string("source", "(Unknown)")
    reason = data.string("reason", "Banned by an operator.")
  }
}
