package ru.levgrekov.chat.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import ru.levgrekov.chat.convertation.JsonHelper
import ru.levgrekov.chat.support.Message

class ChatState(val onSendRequest: (String)->Unit) {

    private var _recipient = mutableStateOf<String?>(null)
    var recipient: String?
        get() = _recipient.value
        set(value) { _recipient.value = value }

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    private var _inputMessage = mutableStateOf("")
    var inputMessage: String
        get() = _inputMessage.value
        set(value) { _inputMessage.value = value }

    fun deleteMessage(index:Int){
        _messages.removeAt(index)
    }
    fun addMessage(data:Map<String,Any?>){
        _messages.add(
            Message(
                data["login"].toString(),
                data["recipient"]?.toString(),
                data["you"].toString().toBoolean(),
                data["message"].toString(),)
        )
    }
    fun sendMessageToServer() {
        if (inputMessage.isNotBlank()) {
            onSendRequest(
                JsonHelper.toJSON(
                    "TYPE" to "MESSAGE",
                    "message" to inputMessage,
                    "recipient" to recipient ))
            inputMessage = ""
        }
    }

}