package com.fox.todolist.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fox.todolist.R
import com.fox.todolist.data.model.ListTuple
import com.fox.todolist.data.model.NoteEntity
import com.fox.todolist.databinding.TaskItemBinding
import com.fox.todolist.presentation.listeners.NoteActionListener
import java.text.SimpleDateFormat

class NoteAdapter(
    private val actionListener: NoteActionListener
): RecyclerView.Adapter<NoteAdapter.NoteHolder>(), View.OnClickListener {

    private var noteList = mutableListOf<NoteEntity>()


    class NoteHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = TaskItemBinding.bind(item)

        @SuppressLint("SimpleDateFormat")
        fun bind(note: NoteEntity) {
            binding.tvTitle.text = note.title
            if(note.date != null)
                binding.tvDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(note.date)
        }
    }

    override fun onClick(v: View) {
        val note = v.tag as NoteEntity

        actionListener.onNoteDetails(note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return NoteHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = noteList[position]
        holder.itemView.tag = note
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillNoteList(notes: List<NoteEntity>?) {
        if(notes != null)
            noteList = notes.toMutableList()
        else return
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