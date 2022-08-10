package com.fox.todolist.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fox.todolist.R
import com.fox.todolist.databinding.FragmentMainBinding
import com.fox.todolist.presentation.adapter.NoteAdapter
import kotlin.random.Random

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initViews()
    }

    private fun initViews() {
        binding.btnAdd.setOnClickListener {
            mockData()
        }
    }

    private fun initRecycler() {
        binding.recyclerView.apply {
            noteAdapter = NoteAdapter()
            adapter = noteAdapter
        }
    }

    private fun mockData() {
        val num = Random.nextInt(10)
        noteAdapter.mockData("Note #$num")
    }
}