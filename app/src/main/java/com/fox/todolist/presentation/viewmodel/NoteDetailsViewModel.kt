package com.fox.todolist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fox.todolist.data.model.NoteEntity
import com.fox.todolist.data.source.local.NoteDao
import com.fox.todolist.presentation.base.BaseViewModel
import com.fox.todolist.utils.Constants.CHECK_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteDao: NoteDao
) : BaseViewModel() {

    fun saveNote(note: NoteEntity) = viewModelScope.launch {
        noteDao.addNote(note)
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        noteDao.removeNote(note)
    }
}