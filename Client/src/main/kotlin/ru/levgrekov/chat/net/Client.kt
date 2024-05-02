package ru.levgrekov.chat.net

import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousSocketChannel
import kotlin.coroutines.suspendCoroutine

class Client(private val host: String = "localhost", private val port: Int = 5106) {

    private val serverDataReceivedListener = mutableListOf<(String)->Unit>()

    private val socket: AsynchronousSocketChannel = AsynchronousSocketChannel.open()
    private val communicator: Communicator by lazy { Communicator(socket) }
    private val clientScope = CoroutineScope(Dispatchers.IO + Job())


    fun start() = clientScope.launch{
        try {
            suspendCoroutine<Void> {
                socket.connect(InetSocketAddress(host, port), null, ActionCompletionHandler(it))
            }
            communicator.startDataReceiving { data ->
                serverDataReceivedListener.forEach {
                    it(data)
                }
            }
        } catch (_: Throwable){}
    }

    fun sendRequest(request: String) = clientScope.launch { communicator.send(request) }

    fun stop() {
        clientScope.cancel()
        communicator.stop()
    }

    fun addServerDataReceivedListener(l:(String)->Unit) = serverDataReceivedListener.add(l)

    fun removeServerDataReceivedListener(l:(String)->Unit) = serverDataReceivedListener.remove(l)
}