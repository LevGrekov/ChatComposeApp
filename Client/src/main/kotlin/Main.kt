import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Envelope
import compose.icons.lineawesomeicons.UsersSolid
import ru.levgrekov.chat.ui.MyViewModel
import ru.levgrekov.chat.ui.controls.*

fun main() = application {
    val vm = MyViewModel()
    Window(onCloseRequest = {
        vm.exitRequest()
        exitApplication()
    }) {
        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.height(60.dp),
                        backgroundColor = Color(234, 221, 255),
                        title = {
                            Text(
                                text = vm.authState.userName ?: "Необходима Авторизация",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                ),
                            )
                        },
                        navigationIcon = {
                            SignInSignOutButton(
                                isAuthed = (vm.authState.userName != null),
                                onClickAuthed = {
                                    vm.authState.onSignOut()
                                    vm.authState.showDialog = true
                                },
                                onClickNotAuthed = { vm.authState.showDialog = true }
                            )
                        },
                        actions = {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ){
                                DropdownMenuButton(
                                    list = vm.usersOnline,
                                    mainIcon = LineAwesomeIcons.UsersSolid,
                                    subIcons = LineAwesomeIcons.Envelope,
                                    action = {vm.chatState.recipient = it}
                                )
                            }
                        }
                    )
                },
                content = {Chat(vm.chatState)}
            )
            AuthenticationContent(Modifier,vm.authState)
            OKMessage(Modifier,vm.okMessageState)
        }
    }
}





