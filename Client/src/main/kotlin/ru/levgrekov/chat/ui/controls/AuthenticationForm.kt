package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    login: String,
    password: String,
    enableAuthentication: Boolean,
    onLoginChanged: (login: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onAuthenticate: () -> Unit,
    onRegistered: () -> Unit
) {
    var registering by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Авторизация",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginInput(
                    modifier = Modifier.fillMaxWidth(),
                    login = login ?: "",
                    onLoginChanged = onLoginChanged
                )
                Spacer(modifier = Modifier.height(16.dp))
                PasswordInput(
                    modifier = Modifier.fillMaxWidth(),
                    password = password ?: "",
                    onPasswordChanged = onPasswordChanged
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    modifier = modifier,
                    onClick = {
                        if (!registering) onAuthenticate() else onRegistered()
                    },
                    enabled = enableAuthentication
                ) {
                    Text(
                        text = if (registering) "Sing In" else "Log In"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = { registering = !registering },
            modifier = Modifier.padding(top = 8.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(if (registering) "Already have an account? Login" else "Don't have an account? Register")
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}