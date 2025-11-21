package com.example.progresshabitplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.progresshabitplanner.databinding.FragmentCreateScheduleBinding
import com.example.progresshabitplanner.model.CreateCustomScheduleRequest
import com.example.progresshabitplanner.model.CreateRecurringScheduleRequest
import com.example.progresshabitplanner.model.CreateWeekdayScheduleRequest
import com.example.progresshabitplanner.ui.habit.HabitViewModel
import com.example.progresshabitplanner.ui.schedule.ScheduleViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CreateScheduleFragment : Fragment(R.layout.fragment_create_schedule) {

    private var _binding: FragmentCreateScheduleBinding? = null
    private val binding get() = _binding!!

    private val habitViewModel: HabitViewModel by viewModels()
    private val scheduleViewModel: ScheduleViewModel by viewModels()

    private var selectedHabitId: Int? = null
    private var selectedDate: LocalDate? = null
    private var selectedStart: LocalTime? = null
    private var selectedEnd: LocalTime? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateScheduleBinding.bind(view)

        setupHabitSpinner()
        setupDatePicker()
        setupTimePickers()
        setupButtons()

        habitViewModel.loadHabits()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupHabitSpinner() {
        habitViewModel.habits.observe(viewLifecycleOwner) { habits ->
            if (habits.isEmpty()) return@observe

            val habitNames = habits.map { it.name }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                habitNames
            )
            binding.spinnerHabit.adapter = adapter

            binding.spinnerHabit.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedHabitId = habits[position].id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        selectedHabitId = null
                    }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            val today = LocalDate.now()
            val dialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    selectedDate = LocalDate.of(year, month + 1, day)
                    binding.etDate.setText(selectedDate.toString())
                },
                today.year,
                today.monthValue - 1,
                today.dayOfMonth
            )
            dialog.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupTimePickers() {

        binding.etStart.setOnClickListener {
            val dialog = TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    selectedStart = LocalTime.of(hour, minute)
                    binding.etStart.setText(selectedStart.toString())
                },
                8,
                0,
                true
            )
            dialog.show()
        }

        binding.etEnd.setOnClickListener {
            val dialog = TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    selectedEnd = LocalTime.of(hour, minute)
                    binding.etEnd.setText(selectedEnd.toString())
                },
                8,
                0,
                true
            )
            dialog.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupButtons() {

        binding.btnCustomSchedule.setOnClickListener {
            if (!validateInputs()) return@setOnClickListener

            val (start, end, duration) = buildDateTimes()

            val req = CreateCustomScheduleRequest(
                habitId = selectedHabitId!!,
                date = start.format(isoFormatter),
                start_time = start.format(isoFormatter),
                end_time = end.format(isoFormatter),
                duration_minutes = duration,
                is_custom = true,
                participantIds = emptyList(),
                notes = binding.etNotes.text.toString()
            )

            scheduleViewModel.createCustomSchedule(req)
            Toast.makeText(requireContext(), "Custom schedule created!", Toast.LENGTH_SHORT).show()
        }

        binding.btnRecurringSchedule.setOnClickListener {
            if (!validateInputs()) return@setOnClickListener

            val (start, end, duration) = buildDateTimes()

            val req = CreateRecurringScheduleRequest(
                habitId = selectedHabitId!!,
                start_time = start.format(isoFormatter),
                end_time = end.format(isoFormatter),
                duration_minutes = duration,
                repeatPattern = "none",
                repeatDays = 0,
                is_custom = true,
                participantIds = emptyList(),
                notes = binding.etNotes.text.toString()
            )

            scheduleViewModel.createRecurringSchedule(req)
            Toast.makeText(requireContext(), "Recurring schedule created!", Toast.LENGTH_SHORT).show()
        }

        binding.btnWeekdaySchedule.setOnClickListener {
            if (!validateInputs()) return@setOnClickListener

            val (start, end, _) = buildDateTimes()

            val req = CreateWeekdayScheduleRequest(
                habitId = selectedHabitId!!,
                start_time = start.format(isoFormatter),
                end_time = end.format(isoFormatter),
                duration_minutes = 0,
                daysOfWeek = listOf(1, 2, 3, 4, 5),
                numberOfWeeks = 4,
                participantIds = emptyList(),
                notes = binding.etNotes.text.toString()
            )

            scheduleViewModel.createWeekdaySchedule(req)
            Toast.makeText(requireContext(), "Weekday schedule created!", Toast.LENGTH_SHORT).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateInputs(): Boolean {
        if (selectedHabitId == null) {
            Toast.makeText(requireContext(), "Please select a habit", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedDate == null || selectedStart == null || selectedEnd == null) {
            Toast.makeText(requireContext(), "Please select date and times", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedEnd!!.isBefore(selectedStart)) {
            Toast.makeText(requireContext(), "End time must be after start", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildDateTimes(): Triple<LocalDateTime, LocalDateTime, Int> {
        val start = LocalDateTime.of(selectedDate, selectedStart)
        val end = LocalDateTime.of(selectedDate, selectedEnd)
        val duration = java.time.Duration.between(selectedStart, selectedEnd).toMinutes().toInt()
        return Triple(start, end, duration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
