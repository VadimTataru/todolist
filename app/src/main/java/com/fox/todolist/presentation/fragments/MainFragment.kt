package com.fox.todolist.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fox.todolist.R
import com.fox.todolist.data.model.NoteEntity
import com.fox.todolist.databinding.FragmentMainBinding
import com.fox.todolist.presentation.adapter.NoteAdapter
import com.fox.todolist.presentation.listeners.NoteActionListener
import com.fox.todolist.presentation.viewmodel.MainFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private val viewModel by viewModels<MainFragmentViewModel>()

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

        viewModel.getNotes().observe(viewLifecycleOwner, Observer{ notes ->
            noteAdapter.fillNoteList(notes)
        })
    }

    private fun initViews() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteDetailsFragment)
        }
    }

    private fun initRecycler() {
        binding.recyclerView.apply {
            noteAdapter = NoteAdapter(object: NoteActionListener {
                override fun onNoteDetails(note: NoteEntity) {
                    Toast.makeText(requireContext(), note.id.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onNoteFavouriteState(note: NoteEntity) {
                    TODO("Not yet implemented")
                }

                override fun onNoteDelete(note: NoteEntity) {
                    TODO("Not yet implemented")
                }

            })
            adapter = noteAdapter
        }
    }
}