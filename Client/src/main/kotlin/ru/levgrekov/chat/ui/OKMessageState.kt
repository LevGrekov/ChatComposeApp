package ru.levgrekov.chat.ui

import androidx.compose.runtime.mutableStateOf

class OKMessageState{
    var message: String = "Ошибка"
    var title: String = "Title"
    private var _showDialog = mutableStateOf(false)
    var show: Boolean
        get() = _showDialog.value
        set(value) {
            _showDialog.value = value
        }

    fun changeVisibility() {
        _showDialog.value = !_showDialog.value
    }

    fun configure(title:String, message: String){
        this.title = title
        this.message = message
        changeVisibility()
    }
}