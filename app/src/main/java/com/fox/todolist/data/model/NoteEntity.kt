package com.fox.todolist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fox.todolist.utils.Converters
import java.util.*

@Entity(
    tableName = "notes"
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    //@TypeConverters(Converters::class)
    @ColumnInfo(name = "date") val date: Date?,
    @ColumnInfo(name = "importantly") val importantly: Int
)