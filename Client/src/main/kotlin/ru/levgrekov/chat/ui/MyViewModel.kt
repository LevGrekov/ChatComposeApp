package ru.levgrekov.chat.ui
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.levgrekov.chat.net.Client
import ru.levgrekov.io.JsonHelper

class MyViewModel{
    val viewScope = CoroutineScope(Dispatchers.Main + Job())
    private val stopListeners = mutableListOf<()->Unit>()
    private val dataReadyListeners = mutableListOf< (String)->Unit>()
    private var stop = false

    val okMessageState = OKMessageState()
    val authState = AuthState(::sendRequest)
    val chatState = ChatState(::sendRequest)

    init {
        try {
            Client().apply {
                addStopListener { stop() }
                addDataReadyListener {  sendRequest(it)  }
                addServerDataReceivedListener { getResponse(it) }
                start()
            }
        }
        catch (ce: Exception){
            okMessageState.configure("404","")
        }

    }

    private val _usersOnline = mutableStateListOf<String>()
    val usersOnline: List<String> = _usersOnline


    private fun getResponse(response: String) {
        val data = JsonHelper.getMapFromJson(response)
        println(response)
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
                } ?: run {

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

    private fun sendRequest(data: String) {
        if (stop) stopListeners.forEach { it() }
        dataReadyListeners.forEach { it(data) }
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