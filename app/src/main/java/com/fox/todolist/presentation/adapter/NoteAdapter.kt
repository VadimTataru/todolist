package com.fox.todolist.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fox.todolist.R
import com.fox.todolist.databinding.TaskItemBinding

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    // TODO: Change to NoteClass
    private val noteList = mutableListOf<String>()

    class NoteHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = TaskItemBinding.bind(item)

        fun bind(header: String) {
            binding.tvTitle.text = header
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
    fun mockData(data: String) {
        noteList.add(data)
        notifyDataSetChanged()
    }
}