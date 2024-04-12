package ru.levgrekov.chat.ui.controls

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginInput(
    modifier: Modifier = Modifier,
    login: String,
    onLoginChanged: (login: String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = login,
        onValueChange = {onLoginChanged(it)} ,
        label = {
            Text(text = "login")
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null
            )
        }
    )
}