package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.levgrekov.chat.ui.ChatState

@Composable
fun Chat(chatState: ChatState){
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(Modifier.weight(5f)) {
            LazyScrollable(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.LightGray)
                    .shadow(5.dp),
                shape = RoundedCornerShape(10.dp),
                messages = chatState.messages,
                onChangeRecipient = {chatState.recipient = it},
                onDeleteCard = {chatState.deleteMessage(it)}
            )
        }

        Box(Modifier.weight(1f)) {
            InputField(
                message = chatState.inputMessage,
                recipient =  chatState.recipient,
                onMessageChange = { chatState.inputMessage = it },
                onCancelRecipient = {chatState.recipient = null},
                onSendRequest = chatState::sendMessageToServer
            )
        }
    }
}