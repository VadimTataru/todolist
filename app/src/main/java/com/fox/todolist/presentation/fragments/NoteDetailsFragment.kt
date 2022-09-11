package com.fox.todolist.presentation.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.TimePickerDialog
import android.content.ClipDescription
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
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
import com.fox.todolist.receiver.NotificationReceiver
import com.fox.todolist.utils.Constants.NOTE_CHANNEL_ID_INC
import com.fox.todolist.utils.Constants.NOTE_DESC_EXTRA
import com.fox.todolist.utils.Constants.NOTE_TITLE_EXTRA
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class NoteDetailsFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    lateinit var binding: FragmentNoteDetailsBinding
    private val viewModel by viewModels<NoteDetailsViewModel>()
    private lateinit var noteEntity: NoteEntity
    private lateinit var cal: Calendar

    //Date pick vars
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteId = getArgs()

        if(noteId != 0) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getById(noteId).collect{
                    noteEntity = it ?: NoteEntity(0, "", "", Date(0), 0)
                    fillFields(noteEntity)
                }
            }
        }

        if(noteId != 0) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                viewModel.deleteNote(noteEntity)
                Snackbar.make(view, "Deleted", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_noteDetailsFragment_to_mainFragment)
            }
        }

        binding.btnCalendar.setOnClickListener {
            date = ""
            pickDate()
        }

        binding.btnAdd.setOnClickListener {
            if(noteId == 0) {
                val note = buildNoteEntity()
                viewModel.saveNote(note)
                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
                setAlarm(note.title, note.description, Random(10).nextInt())
            } else {
                viewModel.updateNote(buildNoteEntity(noteId))
                Snackbar.make(view, "Updated", Snackbar.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.action_noteDetailsFragment_to_mainFragment)
        }
    }

    private fun pickDate() {
        getDateTimeCalendar()
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun getDateTimeCalendar() {
        cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
        date += "$day-${month+1}-$year"
        getDateTimeCalendar()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onTimeSet(picker: TimePicker?, hour: Int, min: Int) {
        date += " $hour:$min"
        binding.tvDate.text = ""
        binding.tvDate.text = date
        cal[Calendar.HOUR] = hour
        cal[Calendar.MINUTE] = min
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
    }

    @SuppressLint("SimpleDateFormat")
    private fun buildNoteEntity(id: Int = 0): NoteEntity {
        val dateAsString = binding.tvDate.text.toString()
        return NoteEntity(
            id,
            binding.etTitle.text.toString(),
            binding.etDescription.text.toString(),
            if(dateAsString.isBlank())
                null
            else
                SimpleDateFormat("dd-MM-yyyy HH:mm").parse(binding.tvDate.text.toString()),
            0
        )
    }

    private fun getArgs(): Int {
        val args: NoteDetailsFragmentArgs by navArgs()
        return if(args.noteIdArg != 0)
            args.noteIdArg
        else
            0
    }

    @SuppressLint("SimpleDateFormat")
    private fun fillFields(note: NoteEntity) {
        binding.etTitle.setText(note.title)
        binding.etDescription.setText(note.description)
        binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy HH:mm").format(note.date!!)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm(title: String, description: String, id: Int) {
        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        intent.putExtra(NOTE_TITLE_EXTRA, title)
        intent.putExtra(NOTE_DESC_EXTRA, description)
        intent.putExtra(NOTE_CHANNEL_ID_INC, id)

        val tick = Date().time

        val pendingIntent = PendingIntent.getBroadcast(requireContext(), tick.toInt(), intent, FLAG_MUTABLE)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis , pendingIntent)
    }
}