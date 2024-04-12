package ru.levgrekov.chat.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.smak.chat.convertation.toByteBuffer
import ru.smak.chat.convertation.toString
import ru.smak.chat.net.ActionCompletionHandler
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import kotlin.coroutines.suspendCoroutine

class Communicator(private val socket: AsynchronousSocketChannel){

    private val stopWorkingListener = mutableListOf<()->Unit>()

    private val communicatorScope = CoroutineScope(Dispatchers.IO)

    fun addStopWorkingListener(l: ()->Unit) = stopWorkingListener.add(l)
    fun removeStopWorkingListener(l: ()->Unit) = stopWorkingListener.remove(l)

    private var stop = false
    val isAlive
        get() = !stop

    suspend fun send(data: String){
        val buf = data.toByteBuffer()
        try {
            val wroteBytesCount = suspendCoroutine{
                socket.write(
                    buf,
                    null,
                    ActionCompletionHandler(it)
                )

            }

        } catch(_: Throwable){
            stop()
        }
    }

    fun startDataReceiving(onDataReceived:suspend (String)->Unit){
        communicatorScope.launch {
            try {
                while (isAlive) {
                    val bf = ByteBuffer.allocate(1024)
                    val readBytesCount = suspendCoroutine {
                        socket.read(bf, null, ActionCompletionHandler(it))
                    }
                    bf.flip()
                    val data = bf.toString(Charsets.UTF_8)
                    onDataReceived(data)

                }
            } catch (_: Throwable){
                stop()
            }
        }
    }

    fun stop() {
        stop = true
        socket.close()
        stopWorkingListener.forEach { it() }
    }

}