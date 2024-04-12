package ru.levgrekov.chat.ui

import androidx.compose.runtime.mutableStateOf
import ru.levgrekov.io.JsonHelper

class AuthState(val onSendRequest: (String)->Unit) {
    private var _showDialog = mutableStateOf(true)
    var showDialog: Boolean
        get() = _showDialog.value
        set(value) {
            _showDialog.value = value
        }

    private var _userName = mutableStateOf<String?>(null)
    var userName: String?
        get() = _userName.value
        set(value) {
            _userName.value = value
        }

    private var _login = mutableStateOf("")
    var login: String
        get() = _login.value
        set(value) { _login.value = value}

    private var _password = mutableStateOf("")
    var password: String
        get() = _password.value
        set(value) {
            _password.value = value
        }

    fun authEnable(): Boolean =
        password.isNotEmpty() && login.isNotEmpty()

    fun onLogIn() = onSendRequest(
        JsonHelper.toJSON(
            "TYPE" to "LOGIN",
            "login" to login,
            "password" to password
        )
    )

    fun onSignIn() = onSendRequest(
        JsonHelper.toJSON(
            "TYPE" to "SIGNIN",
            "login" to login,
            "password" to password
        )
    )

    fun onSignOut() = onSendRequest(
        JsonHelper.toJSON(
            "TYPE" to "EXIT",
        )
    ).also { userName = null }

}