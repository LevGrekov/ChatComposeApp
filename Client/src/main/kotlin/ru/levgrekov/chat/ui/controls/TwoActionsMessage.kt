package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TwoActionsMessage(
    firstAction:() -> Unit,
    secondAction:() -> Unit,
    firstName: String,
    secondName: String,
    title: String? = null,
    message: String? = null,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        shape = shape,
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                TextButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = firstAction
                ) {
                    Text(firstName)
                }
                TextButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = secondAction
                ) {
                    Text(secondName)
                }
            }
        },

        title = {
            title?.let {
                Text(
                    fontSize = 18.sp,
                    text = it
                )
            }
        },
        text = {
            message?.let {
                Text(
                    fontSize = 18.sp,
                    text = it
                )
            }
        }
    )
}