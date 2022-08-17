package com.fox.todolist.data.model

import androidx.room.ColumnInfo
import java.util.*

data class ListTuple(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date") val date: Date,
)