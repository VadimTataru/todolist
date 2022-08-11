package com.fox.todolist.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fox.todolist.R
import com.fox.todolist.data.model.NoteEntity
import com.fox.todolist.databinding.TaskItemBinding

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    private var noteList = mutableListOf<NoteEntity>()

    class NoteHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = TaskItemBinding.bind(item)

        fun bind(note: NoteEntity) {
            binding.tvTitle.text = note.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return NoteHolder(view)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillNoteList(notes: MutableList<NoteEntity>) {
        noteList = notes
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNote(note: NoteEntity) {
        noteList.add(note)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeNote(note: NoteEntity) {
        noteList.remove(note)
        notifyDataSetChanged()
    }
}