package com.example.firebase_learn.data.model

import java.util.UUID

data class Note(
    val noteId: String = UUID.randomUUID().toString(),  // initial value Unique ID for each note
    var title: String = "",
    var data: String = "",//description
    var userId: String = ""
)