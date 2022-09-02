package com.fox.todolist.presentation.listeners

import com.fox.todolist.data.model.NoteEntity

interface NoteActionListener {
    fun onNoteDetails(note: NoteEntity)
    fun onNoteFavouriteState(note: NoteEntity)
    fun onNoteDelete(note: NoteEntity)
}