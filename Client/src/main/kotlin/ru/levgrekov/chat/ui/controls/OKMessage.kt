package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.layout.Box
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
import ru.levgrekov.chat.ui.OKMessageState

@Composable
fun OKMessage(
    modifier: Modifier = Modifier,
    OKMessageState: OKMessageState,
    shape: Shape = RoundedCornerShape(10.dp),
) {
    if (OKMessageState.show){
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                OKMessageState.changeVisibility()
            },
            shape = shape,
            buttons = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        modifier = Modifier.padding(10.dp),
                        onClick = { OKMessageState.changeVisibility() }
                    ) {
                        Text("OK")
                    }
                }
            },
            title = {
                Text(
                    fontSize = 18.sp,
                    text = OKMessageState.title
                )
            },
            text = {
                Text(
                    text = OKMessageState.message
                )
            }
        )
    }
}