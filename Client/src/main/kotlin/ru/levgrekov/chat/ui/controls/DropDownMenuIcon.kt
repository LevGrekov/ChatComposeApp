package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DropdownMenuButton(
    list: List<String>,
    mainIcon: ImageVector,
    subIcons: ImageVector,
    action: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        TextButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(mainIcon, null)
            Text("Участники")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEach{
                DropdownMenuItem(
                    onClick = {action(it)} ,
                ){
                    Row {
                        Icon(subIcons,null, tint = MaterialTheme.colors.primary)
                        Text(it)
                    }
                }

            }
        }
    }
}