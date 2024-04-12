package ru.levgrekov.chat.net

import ru.levgrekov.chat.convertation.JsonHelper
import java.nio.channels.AsynchronousSocketChannel

class ConnectedClient(private val clientSocket: AsynchronousSocketChannel) {

    companion object {
        private val clients = mutableListOf<ConnectedClient>()
    }

    private var login: String? = null
    private val isAlive
        get() = communicator.isAlive

    private val communicator: Communicator by lazy {
        Communicator(clientSocket).apply {
            addStopWorkingListener { clients.removeIf { !it.isAlive } }
        }
    }

    init {
        clients.add(this)
    }

    private suspend fun sendResponse(vararg keyValue: Pair<String, Any?>) =
        JsonHelper.toJSON(*keyValue).apply { communicator.send(this) }

    private fun stop() = communicator.stop()

    fun start() = communicator.startDataReceiving { data -> getRequest(data) }

    private suspend fun getRequest(request: String) {
        val data = JsonHelper.getMapFromJson(request)
        println("$login ---> $request")
        when (data["TYPE"]) {
            "LOGIN" -> processLogIn(data["login"].toString(),data["password"].toString())
            "SIGNIN" -> processSignIn(data["login"].toString(),data["password"].toString())
            "MESSAGE" -> processMessage(data["message"].toString(), data["recipient"]?.toString())
            "EXIT" -> processExit()
            else -> println(request).also { throw Exception("Неизвестный тип операции") }
        }
    }
    private suspend fun preprocessUsername(login: String) : String?{
        val loginInput = login.trim()
        val regex = Regex("^[a-zA-Z][a-zA-Z0-9_]{3,15}\$")
        if (!loginInput.matches(regex)) {
            sendResponse(
                "TYPE" to "LOGIN",
                "message" to "Логин задан некорректно",
                "login" to "server",
                "error" to true,
            )
            return null
        }
        return loginInput
    }
    private suspend fun processLogIn(login: String, password:String) {
        if (this.login != null || clients.any { it.login == login }) {
            sendResponse(
                "TYPE" to "LOGIN",
                "message" to "Вы уже вошли в аккаунт",
                "login" to "server",
                "error" to true,
            )
            return
        }


        preprocessUsername(login)?.let { loginInput ->
            DbContext.authenticateUser(loginInput,password).also { dbResponse->
                when (dbResponse.second) {
                    0 -> {
                        this.login = loginInput
                        sendMessagesToAll("${this.login} присоеденился к чату", true)
                        clients.forEach { client ->
                            if(client.login != this.login){
                                client.sendResponse(
                                    "TYPE" to "LOGIN",
                                    "user_update" to this.login,
                                )
                            }
                            else{
                                sendResponse(
                                    "TYPE" to "LOGIN",
                                    "message" to "Вы успешно вошли в аккаунт ${this.login}",
                                    "login" to this.login,
                                    "user_update" to clients
                                        .toList()
                                        .filter { it.login != null && it.login != this.login }
                                        .map { it.login }
                                )
                            }
                        }
                    }
                    else -> {
                        sendResponse(
                            "TYPE" to "LOGIN",
                            "message" to dbResponse.first,
                            "login" to "server",
                            "error" to true
                        )
                    }
                }
            }
        }
    }

    private suspend fun processSignIn(login: String, password:String){
        preprocessUsername(login)?.let { loginInput ->
            DbContext.registerUser(loginInput,password).also {
                when (it.second) {
                    0 -> {
                        sendResponse(
                            "TYPE" to "SIGNIN",
                            "message" to it.first,
                        )
                    }
                    else -> {
                        sendResponse(
                            "TYPE" to "SIGNIN",
                            "message" to it.first,
                            "error" to true
                        )
                    }
                }
            }
        }
    }

    private suspend fun processMessage(message: String, recipient: String?) {
        if (login == null) {
            sendResponse(
                "TYPE" to "MESSAGE",
                "message" to "Пожалуйста, авторизируйтесь!",
                "login" to "server",
                "error" to true,
            )
            return
        }
        if (login == recipient) {
            sendResponse(
                "TYPE" to "MESSAGE",
                "message" to "Нельзя отправить сообщение самому себе!",
                "login" to "server",
                "error" to true,
            )
            return
        }
        if (recipient == null) {
            sendMessagesToAll(message)
            return
        }
        sendPrivateMessage(message, recipient)
    }

    private suspend fun sendMessagesToAll(message: String, isSystem: Boolean = false) {
        if (message.isNotEmpty()) {
            clients.forEach {
                if(!it.isAlive) { it.stop() }
                else{
                    it.sendResponse(
                        "TYPE" to "MESSAGE",
                        "message" to message,
                        "login" to if (isSystem) "server" else login,
                        "you" to (it.login == login)
                    )
                }
            }
        }
    }

    private suspend fun sendPrivateMessage(message: String, recipient: String?) {
        if (message.isNotEmpty()) {
            val recipientClient = clients.find { it.login == recipient }
            recipientClient?.let {
                sendResponse(
                    "TYPE" to "MESSAGE",
                    "message" to message,
                    "login" to login,
                    "you" to true,
                    "recipient" to recipientClient.login
                )
                it.sendResponse(
                    "TYPE" to "MESSAGE",
                    "message" to message,
                    "login" to login,
                    "you" to false,
                    "recipient" to recipientClient.login
                )
                return
            }
            sendResponse(
                "TYPE" to "MESSAGE",
                "message" to "Пользователя $recipient в данный момент нет в чате, отправить ему сообщение невозможно",
                "login" to "server",
                "error" to true,
            )
        }
    }

    private suspend fun processExit(){
        clients.forEach {
            if(it != this){
                it.sendResponse(
                    "TYPE" to "EXIT",
                    "user_update" to this.login,
                )
            }
            else{
                sendResponse(
                    "TYPE" to "EXIT",
                    "message" to "Вы вышли из аккаунта: ${this.login}",
                )
            }
        }
        sendMessagesToAll("${this.login} вышел из чата", true)
        this.login = null
    }
}