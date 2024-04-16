package dream.app

import dream.network.ConnectionState
import dream.server.Server
import dream.utils.file
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import java.net.Proxy

suspend fun main() {

  runBlocking(SupervisorJob()) {
    println("Hello World!")

    // try init connection states
    ConnectionState.HANDSHAKING
    ConnectionState.PLAY
    ConnectionState.LOGIN
    ConnectionState.STATUS

    val directory = file("C:\\Users\\Cliente\\Jetbrains\\Servidores\\Dream")
    val server = Server(Proxy.NO_PROXY, directory)
    val system = server.networkSystem

    system.start()

    println("Server Started!")

  }

/*
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

  sleep(100000000)*/

}
