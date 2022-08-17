package com.fox.todolist.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.fox.todolist.data.model.ListTuple
import com.fox.todolist.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("select * from notes")
    fun getAll(): LiveData<List<NoteEntity>>

    @Query("select * from notes where id = :noteId")
    fun getById(noteId: Int): Flow<NoteEntity?>

    @Insert(onConflict = REPLACE)
    suspend fun addNote(noteEntity: NoteEntity)

    @Delete
    suspend fun removeNote(noteEntity: NoteEntity)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)
}