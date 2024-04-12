package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Envelope
import compose.icons.lineawesomeicons.LongArrowAltRightSolid
import compose.icons.lineawesomeicons.TrashAlt
import ru.levgrekov.io.Message
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageCard(
    message: Message,
    onChangeRecipient:(String?)->Unit,
    index: Int,
    onDelete: (Int)->Unit

) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = if (message.you) Alignment.CenterEnd else Alignment.CenterStart
    ){
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = dateFormat.format(Date())
        val dateString = SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(Date())
        Surface(
            elevation = 5.dp,
            modifier = Modifier
                .padding(6.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = if (message.you) Color(219, 181, 251) else Color(234, 221, 255)
        ) {
            Row(
                modifier = Modifier.padding(start=20.dp,end = 5.dp,top = 10.dp, bottom = 10.dp),
            ) {
                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = message.login,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colors.primary
                        )
                        message.recipient?.let { recipient ->
                            Icon(LineAwesomeIcons.LongArrowAltRightSolid, null,Modifier.size(17.dp))
                            Surface(
                                modifier = Modifier.alignByBaseline(),
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colors.secondaryVariant.copy(0.7f)
                            ) {
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = recipient,
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Text(
                        fontSize = 15.sp,
                        text = message.message,
                        style = MaterialTheme.typography.body1
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 50))
                            .graphicsLayer(alpha = 0.6f)
                            .clickable{
                                if(message.you){ onDelete(index) }
                                else onChangeRecipient(message.login)
                            },
                        imageVector = if(message.you) LineAwesomeIcons.TrashAlt else LineAwesomeIcons.Envelope ,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary.copy(alpha = 0.6f)
                    )
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.caption,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = if (message.you) Arrangement.End else Arrangement.Start,
//    ) {
//
//    }
}