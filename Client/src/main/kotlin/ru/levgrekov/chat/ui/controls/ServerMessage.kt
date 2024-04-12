package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.levgrekov.io.Message

@Composable
fun ServerMessage(message: Message){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            elevation = 5.dp,
            modifier = Modifier.padding(6.dp),
            color = Color(234, 221, 255)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = message.message ?: "null",
                    style = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontSize = 15.sp,
                    )
                )
            }
        }
    }
}