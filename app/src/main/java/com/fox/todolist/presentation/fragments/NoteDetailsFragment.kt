package com.fox.todolist.presentation.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.TimePickerDialog
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

@AndroidEntryPoint
class NoteDetailsFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: FragmentNoteDetailsBinding
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
                    noteEntity = it ?: NoteEntity(0, "", "", Date(0), 0, 0)
                    fillFields(noteEntity)
                }
            }
        }

        if(noteId != 0) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                viewModel.deleteNote(noteEntity)
                if(noteEntity.pendingId != 0) cancelAlarm(noteEntity.pendingId)
                showMessage("Deleted")
                findNavController().navigate(R.id.action_noteDetailsFragment_to_mainFragment)
            }
        }

        binding.btnCalendar.setOnClickListener {
            date = ""
            pickDate()
        }

        binding.btnAdd.setOnClickListener {
            val broadcastId = Date().time.toInt()
            if(noteId == 0) {
                if(binding.etTitle.text.isNullOrBlank()) {
                    showMessage("Fill title field!")
                    return@setOnClickListener
                }

                val note = buildNoteEntity(broadcastId = broadcastId)
                if(note.date == null)
                    note.pendingId = 0;

                viewModel.saveNote(note)

                if(note.date != null)
                    setAlarm(note.title, note.description, Random(10).nextInt(), broadcastId)
                showMessage("Created")

            } else {
                val note = buildNoteEntity(noteId, broadcastId)
                if(noteEntity.pendingId == 0 && note.date != null) {
                    setAlarm(note.title, note.description, Random(10).nextInt(), broadcastId)
                } else if(noteEntity.pendingId != 0 && noteEntity.date != note.date && note.date != null) {
                    cancelAlarm(noteEntity.pendingId)
                    setAlarm(note.title, note.description, Random(10).nextInt(), broadcastId)
                }

                if(note.date == null)
                    note.pendingId = 0;

                viewModel.updateNote(note)
                showMessage("Updated")
            }
            findNavController().navigate(R.id.action_noteDetailsFragment_to_mainFragment)
        }
    }

    private fun pickDate() {
        getDateTimeCalendar()
        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.datePicker.minDate = cal.timeInMillis
        datePickerDialog.show()
    }

    private fun getDateTimeCalendar() {
        cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
        val isSystem24Hour = is24HourFormat(requireContext())
        hour = cal.get(if(isSystem24Hour) Calendar.HOUR_OF_DAY else Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE) + 2
    }

    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
        date += "$day-${month+1}-$year"
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        val isSystem24Hour = is24HourFormat(requireContext())
        TimePickerDialog(requireContext(), this, hour, minute, isSystem24Hour).show()
    }

    override fun onTimeSet(picker: TimePicker?, hour: Int, min: Int) {
        date += " $hour:$min"
        cal[Calendar.HOUR_OF_DAY] = hour
        cal[Calendar.MINUTE] = min
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        fillDateField(cal)
    }

    @SuppressLint("SetTextI18n")
    private fun fillDateField(cal: Calendar) {
        binding.tvDate.text = ""
        val date = "${"%02d".format(cal[Calendar.DAY_OF_MONTH])}.${"%02d".format(cal[Calendar.MONTH])}.${cal[Calendar.YEAR]}"
        val time = "${"%02d".format(cal[Calendar.HOUR_OF_DAY])}:${"%02d".format(cal[Calendar.MINUTE])}"
        binding.tvDate.text = "$date $time"
    }

    @SuppressLint("SimpleDateFormat")
    private fun buildNoteEntity(id: Int = 0, broadcastId: Int): NoteEntity {
        val dateAsString = binding.tvDate.text.toString()
        return NoteEntity(
            id,
            binding.etTitle.text.toString(),
            binding.etDescription.text.toString(),
            if(dateAsString.isBlank())
                null
            else
                SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date),
            0,
            broadcastId
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
        if(note.date != null)
            binding.tvDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(note.date)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm(title: String, description: String, id: Int, broadcastId: Int) {
        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        intent.putExtra(NOTE_TITLE_EXTRA, title)
        intent.putExtra(NOTE_DESC_EXTRA, description)
        intent.putExtra(NOTE_CHANNEL_ID_INC, id)


        val pendingIntent = PendingIntent.getBroadcast(requireContext(), broadcastId, intent, FLAG_MUTABLE)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis , pendingIntent)
    }

    private fun cancelAlarm(broadcastId: Int) {
        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), broadcastId, intent, FLAG_MUTABLE)

        alarmManager.cancel(pendingIntent)
    }

    private fun showMessage(message: String) = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
}