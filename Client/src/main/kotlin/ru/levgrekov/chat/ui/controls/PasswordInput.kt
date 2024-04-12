package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.EyeSlashSolid
import compose.icons.lineawesomeicons.EyeSolid

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    password: String?,
    onPasswordChanged: (password: String) -> Unit
) {
    var isPasswordHidden by remember { mutableStateOf(true) }
    TextField(
        modifier = modifier,
        value = password ?: "",
        singleLine = true,
        onValueChange = {
            onPasswordChanged(it)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable(
                    onClickLabel =
                    if (isPasswordHidden) {
                        "password"
                    } else "password"
                ) {
                    isPasswordHidden = !isPasswordHidden
                },
                imageVector = if (isPasswordHidden) {
                    LineAwesomeIcons.EyeSolid
                } else LineAwesomeIcons.EyeSlashSolid,
                contentDescription = null
            )
        },
        label = {
            Text(text = "password")
        },
        visualTransformation = if(isPasswordHidden) VisualTransformation.None else PasswordVisualTransformation()
    )
}