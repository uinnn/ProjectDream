package dream.app

import dream.block.*
import dream.item.*
import dream.network.*
import dream.server.*
import dream.utils.*
import kotlinx.coroutines.*
import java.net.*

suspend fun main() {

  runBlocking(SupervisorJob()) {
    println("Hello World!")

    // try init connection states
    ConnectionState.HANDSHAKING
    ConnectionState.PLAY
    ConnectionState.LOGIN
    ConnectionState.STATUS

    Items
    Blocks

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
