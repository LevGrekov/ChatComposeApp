package ru.levgrekov.chat.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.levgrekov.chat.convertation.toString
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
        val ba = data.toByteArray()
        val buf = ByteBuffer.allocate(ba.size+Int.SIZE_BYTES)
        buf.putInt(ba.size)
        buf.put(ba)
        buf.flip()
        try {
            val wroteBytesCount = suspendCoroutine{
                socket.write(
                    buf,
                    null,
                    ActionCompletionHandler(it)
                )
            }
        } catch(e: Throwable){
            println("send: $e.message")
        }
    }


    fun startDataReceiving(onDataReceived:suspend (String)->Unit){
        communicatorScope.launch {
            try {
                while (isAlive) {
                    var capacity = Int.SIZE_BYTES
                    repeat(2) {
                        val buf = ByteBuffer.allocate(capacity)
                        val read = suspendCoroutine { c->
                            socket.read(
                                buf,
                                null,
                                ActionCompletionHandler(c)
                            )
                        }
                        buf.flip()
                        if(it == 0) {
                            capacity = buf.getInt()
                        } else {
                            val data = buf.toString(Charsets.UTF_8)
                            onDataReceived(data)
                        }
                        buf.clear()
                    }
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