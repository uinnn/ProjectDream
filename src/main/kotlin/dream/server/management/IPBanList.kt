package dream.server.management

import kotlinx.serialization.json.JsonObject
import java.io.File
import java.net.SocketAddress

class IPBanList(file: File) : UserList<String, IPBanEntry>(file) {
  override fun createEntry(data: JsonObject): IPBanEntry {
    return IPBanEntry(data)
  }

  fun isBanned(address: SocketAddress): Boolean {
    return has(addressToString(address))
  }

  fun getEntry(address: SocketAddress): IPBanEntry? {
    return getEntry(addressToString(address))
  }

  fun addressToString(address: SocketAddress): String {
    var string = address.toString()
    if (string.contains('/')) {
      string = string.substring(string.indexOf('/' + 1))
    }
    if (string.contains(':')) {
      string = string.substring(0, string.indexOf(':'))
    }
    return string
  }

}
