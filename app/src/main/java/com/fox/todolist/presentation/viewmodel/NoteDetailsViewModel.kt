package com.fox.todolist.presentation.viewmodel

import android.util.Log
import com.fox.todolist.presentation.base.BaseViewModel
import com.fox.todolist.utils.Constants.CHECK_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(private val str: String) : BaseViewModel() {

    init {
        Log.d(CHECK_TAG, "hi there! I provide you")
    }

    fun showLog() {
        Log.d(CHECK_TAG, "hi there! I provide you $str")
    }
}