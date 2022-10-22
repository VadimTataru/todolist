package com.fox.todolist.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.fox.todolist.R
import com.fox.todolist.data.model.NoteEntity
import com.fox.todolist.databinding.FragmentMainBinding
import com.fox.todolist.presentation.adapter.NoteAdapter
import com.fox.todolist.presentation.listeners.NoteActionListener
import com.fox.todolist.presentation.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private val viewModel by viewModels<MainFragmentViewModel>()
    private lateinit var noteList: List<NoteEntity>
    private lateinit var tempNoteList: ArrayList<NoteEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(view)
        initViews()

        viewModel.getNotes().observe(viewLifecycleOwner) { notes ->
            noteList = notes as MutableList<NoteEntity>
            noteAdapter.fillNoteList(notes)
        }

        initSearchView()
    }

    private fun initViews() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteDetailsFragment)
        }
    }

    private fun initRecycler(view: View) {
        binding.recyclerView.apply {
            noteAdapter = NoteAdapter(object: NoteActionListener {
                override fun onNoteDetails(note: NoteEntity) {
                    val action = MainFragmentDirections.actionMainFragmentToNoteDetailsFragment(note.id)
                    Navigation.findNavController(view).navigate(action)
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

    private fun initSearchView() {
        val searchView: SearchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                tempNoteList = arrayListOf()
                val newText = text!!.lowercase(Locale.getDefault())
                if(newText.isNotEmpty()) {
                    noteList.forEach{
                        if(it.title.lowercase(Locale.getDefault()).contains(newText)) {
                            tempNoteList.add(it)
                        }
                    }

                    noteAdapter.fillNoteList(tempNoteList as List<NoteEntity>)
                } else {
                    tempNoteList.clear()
                    noteAdapter.fillNoteList(noteList)
                }
                return false
            }
        })
    }
}