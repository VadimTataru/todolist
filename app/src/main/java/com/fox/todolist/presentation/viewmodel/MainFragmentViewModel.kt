package com.fox.todolist.presentation.viewmodel

import android.util.Log
import com.fox.todolist.data.source.local.NoteDao
import com.fox.todolist.presentation.base.BaseViewModel
import com.fox.todolist.utils.Constants.CHECK_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val noteDao: NoteDao
): BaseViewModel() {

    fun getNotes() = noteDao.getAll()

    fun showLog() {
        Log.d(CHECK_TAG, "${getNotes().value?.size}")
    }
}