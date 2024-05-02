package ru.levgrekov.chat.net

import java.nio.ByteBuffer
import java.nio.channels.CompletionHandler
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ActionCompletionHandler<R>(private val c: Continuation<R>) : CompletionHandler<R, Any?> {
    override fun completed(result: R, attachment: Any?) {
        c.resume(result)
    }

    override fun failed(exc: Throwable, attachment: Any?) {
        c.resumeWithException(exc)
    }
}
class DynamicBufferActionCompletionHandler(private val buffer: ByteBuffer, private val onDataReceived: (Int) -> Unit) : CompletionHandler<Int, Unit> {
    override fun completed(result: Int, attachment: Unit) {
        if (result > 0) {
            buffer.position(buffer.position() + result)
            onDataReceived(result)
            if (!buffer.hasRemaining()) {
                val newBufferSize = buffer.capacity() * 2
                val newBuffer = ByteBuffer.allocate(newBufferSize)
                buffer.flip()
                newBuffer.put(buffer)
                buffer.clear()
                buffer.put(newBuffer)
            }
        } else if (result == -1) {
            println("Connection closed by client.")
        }
    }

    override fun failed(exc: Throwable, attachment: Unit) {
        println("Read operation failed: ${exc.message}")
    }
}