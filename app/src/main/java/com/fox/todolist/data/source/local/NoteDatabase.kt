package com.fox.todolist.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fox.todolist.data.model.NoteEntity
import com.fox.todolist.utils.Converters

@Database(
    entities = [NoteEntity::class],
    version = 0
)
@TypeConverters(Converters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}