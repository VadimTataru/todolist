package com.fox.todolist.data.source.local

import androidx.room.*
import com.fox.todolist.data.model.NoteEntity

@Dao
interface NoteDao {

    @Query("select * from notes")
    fun getAll(): List<NoteEntity>

    @Insert
    fun addNote(noteEntity: NoteEntity)

    @Delete
    fun removeNote(noteEntity: NoteEntity)

    @Update
    fun updateNote(noteEntity: NoteEntity)
}