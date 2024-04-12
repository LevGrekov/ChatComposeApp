package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.levgrekov.io.Message

@Composable
fun LazyScrollable(
    messages:List<Message>,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    onChangeRecipient: (String?)->Unit,
    onDeleteCard: (index: Int)->Unit
) {
    val state = rememberLazyListState()
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty() && state.firstVisibleItemIndex  in messages.size - 7 .. messages.size  ) {
            state.scrollToItem(messages.size - 1)
        }
    }
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        Box(modifier = Modifier.fillMaxSize()
            .background(color = Color.LightGray)
            .padding(10.dp)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp),
                state = state) {
                items(messages.size) { index ->
                    messages[index].apply {
                        when(this.login){
                            "server" -> ServerMessage(this)
                            else-> MessageCard(this,onChangeRecipient,index){
                                onDeleteCard(index)
                            }
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }
    }
}
