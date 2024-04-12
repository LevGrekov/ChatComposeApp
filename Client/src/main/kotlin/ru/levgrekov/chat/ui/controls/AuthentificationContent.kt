package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.levgrekov.chat.ui.AuthState

@Composable
fun AuthenticationContent(
    modifier: Modifier = Modifier,
    authState: AuthState
) {
    if (authState.showDialog) {
        Dialog(
            onDismissRequest = { authState.showDialog = false },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Surface(
                Modifier.fillMaxWidth().fillMaxHeight(),
                color = MaterialTheme.colors.surface,
            ) {
                AuthenticationForm(
                    modifier = modifier,
                    login = authState.login,
                    password = authState.password,
                    onLoginChanged = { authState.login = it },
                    onPasswordChanged = { authState.password = it },
                    enableAuthentication = authState.authEnable(),
                    onAuthenticate = authState::onLogIn,
                    onRegistered = authState::onSignIn,
                )
            }
        }
    }
}