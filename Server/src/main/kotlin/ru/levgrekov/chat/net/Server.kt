package ru.levgrekov.chat.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousServerSocketChannel
import kotlin.coroutines.suspendCoroutine


class Server(val port: Int = 5106) {
    private val serverSocket = AsynchronousServerSocketChannel.open()

    private val serverScope = CoroutineScope(Dispatchers.IO)

    private var stop = false

    fun start(){
        serverSocket.bind(InetSocketAddress(port))

        serverScope.launch {
            try {
                while (!stop) {
                    val cSocket = suspendCoroutine{
                        serverSocket.accept(
                            null,
                            ActionCompletionHandler(it)
                        )
                    }
                    ConnectedClient(cSocket).start()
                }
            } catch (_: Throwable) {
                println("Работа сервера завершена из-за ошибки :(")
            } finally {
                serverSocket.close()
            }
        }
    }

    private fun stop(){
        serverScope.cancel()
        serverSocket.close()
    }
}