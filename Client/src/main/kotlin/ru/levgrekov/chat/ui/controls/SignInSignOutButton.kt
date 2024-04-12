package ru.levgrekov.chat.ui.controls

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.DoorClosedSolid
import compose.icons.lineawesomeicons.DoorOpenSolid

@Composable
fun SignInSignOutButton(
    modifier: Modifier = Modifier,
    isAuthed: Boolean,
    onClickAuthed: ()->Unit,
    onClickNotAuthed: ()-> Unit,
){
    var showDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHoveredState = interactionSource.collectIsHoveredAsState()

    IconButton(
        onClick = {
            if(isAuthed){ showDialog = true }
            else onClickNotAuthed()
        },
        interactionSource = interactionSource,
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(0.6f),
            tint = MaterialTheme.colors.primaryVariant,
            imageVector = when {
                isAuthed && !isHoveredState.value ->  LineAwesomeIcons.DoorClosedSolid
                isAuthed && isHoveredState.value -> LineAwesomeIcons.DoorOpenSolid
                else ->  Icons.Default.AccountCircle
            },
            contentDescription = null,
        )
    }
    if(showDialog){
        TwoActionsMessage(
            firstAction = {showDialog = false},
            firstName = "Отмена",
            secondAction = {
                showDialog = false
                onClickAuthed()
            },
            secondName = "Подтвердить",
            message = "Вы уверены что хотите выйти из аккаунта ?",
            title = "Выйти"
        )
    }
}