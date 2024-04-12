package dream.app

import dream.network.ConnectionState
import dream.server.Server
import dream.utils.always
import dream.utils.file
import dream.utils.sleep
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import java.net.InetAddress
import java.net.Proxy
import kotlin.concurrent.thread

suspend fun main() {
  thread {
    println("Initializing Server...")

    //Blocks
    //Tiles
    ConnectionState
    val directory = file("C:\\Users\\Cliente\\Jetbrains\\Servidores\\Dream")
    val server = Server(Proxy.NO_PROXY, directory)

    server.networkSystem.addLanEndpoint(InetAddress.getLocalHost(), 25565)

    println("Server Started!")
  }

  println("Hello!")

  GlobalScope.always {
    delay(1000)
    println("KeepAlive")
  }

  sleep(100000000)

}
