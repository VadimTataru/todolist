package com.fox.todolist.presentation.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fox.todolist.R
import com.fox.todolist.data.model.NoteEntity
import com.fox.todolist.databinding.FragmentNoteDetailsBinding
import com.fox.todolist.presentation.viewmodel.NoteDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class NoteDetailsFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    lateinit var binding: FragmentNoteDetailsBinding
    private val viewModel by viewModels<NoteDetailsViewModel>()

    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0
    private var date = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteId = getArgs()

        binding.btnCalendar.setOnClickListener {
            date = ""
            pickDate()
        }

        binding.btnAdd.setOnClickListener {
            if(noteId == 0) {
                viewModel.saveNote(buildNoteEntity())
                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.updateNote(buildNoteEntity(noteId))
                Snackbar.make(view, "Updated", Snackbar.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.action_noteDetailsFragment_to_mainFragment)
        }

        if(noteId != 0) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getById(noteId).collect{
                        fillFields(it!!)
                    }
                }
            }
        }
    }

    private fun pickDate() {
        getDateTimeCalendar()
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
        date += "$day/${month+1}/$year"
        getDateTimeCalendar()
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onTimeSet(picker: TimePicker?, hour: Int, min: Int) {
        date += " $hour:$min"
        binding.tvDate.text = ""
        binding.tvDate.text = date
    }

    private fun buildNoteEntity(id: Int = 0): NoteEntity {
        return NoteEntity(
            id,
            binding.etTitle.text.toString(),
            binding.etDescription.text.toString(),
            20000,
            0
        )
    }

    private fun getArgs(): Int {
        val args: NoteDetailsFragmentArgs by navArgs()
        return if(args.noteIdArg != null || args.noteIdArg != 0)
            args.noteIdArg
        else
            0
    }

    private fun fillFields(note: NoteEntity) {
        binding.etTitle.setText(note.title)
        binding.etDescription.setText(note.description)
        binding.tvDate.text = note.date.toString()
    }
}