package ru.levgrekov.chat.ui
import androidx.compose.runtime.mutableStateListOf
import ru.levgrekov.chat.convertation.JsonHelper
import ru.levgrekov.chat.net.Client

class MyViewModel{
    private val stopListeners = mutableListOf<()->Unit>()
    private val dataReadyListeners = mutableListOf< (String)->Unit>()
    private var stop = false

    val okMessageState = OKMessageState()
    val authState = AuthState(::send)
    val chatState = ChatState(::send)

    private val client: Client?

    init {
        client = try {
            Client().apply {
                addStopListener { stop() }
                addDataReadyListener {  sendRequest(it)  }
                addServerDataReceivedListener { getResponse(it) }
                start()
            }
        }
        catch (ce: Exception){
            okMessageState.configure("404","")
            null
        }

    }

    private val _usersOnline = mutableStateListOf<String>()
    val usersOnline: List<String> = _usersOnline


    private fun getResponse(response: String) {
        val data = JsonHelper.getMapFromJson(response)
        if(data.containsKey("error")){
            okMessageState.configure("Ошибка",data["message"].toString())
        }
        else when(data["TYPE"]){
            "SIGNIN" -> {
                okMessageState.configure("Успех!",data["message"].toString())
            }
            "LOGIN" -> {
                data["user_update"]?.let { update ->
                    when (update) {
                        is String -> _usersOnline.add(update)
                        is List<*> -> {
                            okMessageState.configure("Успех!", data["message"].toString())
                            authState.userName = data["login"].toString()
                            authState.showDialog = false
                            _usersOnline.addAll(update.filterIsInstance<String>())
                        }
                        else -> throw Exception()
                    }
                }
            }
            "MESSAGE" -> {
                chatState.addMessage(data)
            }
            "EXIT" -> {
                data["user_update"]?.let {
                    _usersOnline.remove(data["user_update"])
                } ?: run {
                    okMessageState.configure("Будем по вам скучать",data["message"].toString())
                    _usersOnline.clear()
                }
            }

            else -> println(response)
        }
    }

    private fun send(data: String) {
        client?.sendRequest(data)
        //if (stop) stopListeners.forEach { it() }
        //dataReadyListeners.forEach { it(data) }
    }

    fun exitRequest(){
        if(!stop){
            stop = true
            stopListeners.forEach { it() }
        }
    }

    private fun addDataReadyListener(l: (String) -> Unit) = dataReadyListeners.add(l)

    private fun removeDataReadyListener(l: (String) -> Unit) = dataReadyListeners.remove(l)

    private fun addStopListener(l: () -> Unit) = stopListeners.add(l)

    private fun removeStopListener(l: () -> Unit) = stopListeners.remove(l)

}