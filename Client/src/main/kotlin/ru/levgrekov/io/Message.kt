package ru.levgrekov.io

data class Message(
    val login: String,
    val recipient: String?,
    val you: Boolean,
    val message: String)