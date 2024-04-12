package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.PaperPlaneSolid
import compose.icons.lineawesomeicons.UserAltSlashSolid

@Composable
fun InputField(
    message: String,
    recipient: String?,
    onMessageChange: (message:String)-> Unit,
    onCancelRecipient: ()->Unit,
    onSendRequest: () -> Unit,
) {
    Box(Modifier.padding(8.dp)){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = {onMessageChange(it)},
                modifier = Modifier.weight(1f).fillMaxHeight(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onNext = {onSendRequest()},
                    onSend = {onSendRequest()}
                ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
                shape = CircleShape,
                modifier = Modifier.fillMaxHeight(),
                onClick = {onSendRequest()},
                enabled = message.isNotBlank()
            ) {
                recipient.also {
                    val isPrivate = (it != null)
                    if(isPrivate){
                        Text(modifier = Modifier.padding(horizontal = 3.dp), text = it!!)
                    }
                    val additionalModifier = if (isPrivate) {
                        Modifier
                            .clip(RoundedCornerShape(percent = 50))
                            .aspectRatio(1f)
                            .clickable { onCancelRecipient() }
                    } else Modifier
                    Icon(
                        imageVector = if(isPrivate)  LineAwesomeIcons.UserAltSlashSolid else LineAwesomeIcons.PaperPlaneSolid,
                        contentDescription = "Отправить",
                        modifier = additionalModifier
                    )
                }
            }
        }
    }
}