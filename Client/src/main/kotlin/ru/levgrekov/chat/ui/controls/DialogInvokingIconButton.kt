package ru.levgrekov.chat.ui.controls

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogInvokingIconButton(
    ico: ImageVector,
    contentDescription: String?,
    onDialogClose: ()->Unit,
    content: @Composable () -> Unit
    ){
    var showDialog by remember { mutableStateOf(false) }
    IconButton(onClick = { onDialogClose().also { showDialog = true } }) {
        Icon(
            imageVector = ico,
            contentDescription = contentDescription
        )
        if(showDialog){
            Dialog(
                onDismissRequest = {onDialogClose().also { showDialog = false }},
                properties = DialogProperties(dismissOnClickOutside = true)
            ){ content() }
        }
    }
}