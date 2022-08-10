package com.fox.todolist.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fox.todolist.data.model.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 0
)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}