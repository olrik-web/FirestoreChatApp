package com.example.firestorechatapp

import java.text.SimpleDateFormat
import java.util.*

data class MessageModel(
    val messageText: String = "",
    val sender: String="",
    val date: String = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()),
    val message_type: Int = 0

)