package com.example.progresshabitplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.progresshabitplanner.databinding.FragmentCreateScheduleBinding
import com.example.progresshabitplanner.model.CreateCustomScheduleRequest
import com.example.progresshabitplanner.model.CreateRecurringScheduleRequest
import com.example.progresshabitplanner.model.CreateWeekdayScheduleRequest
import com.example.progresshabitplanner.ui.habit.HabitViewModel
import com.example.progresshabitplanner.ui.schedule.ScheduleViewModel
import kotlinx.coroutines.launch
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

        // Observe creation results
        observeCreateResults()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupHabitSpinner() {
        habitViewModel.habits.observe(viewLifecycleOwner) { habits ->
            if (habits.isEmpty()) {
                Log.d("CreateSchedule", "No habits available")
                return@observe
            }

            Log.d("CreateSchedule", "Loaded ${habits.size} habits")
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
                        Log.d("CreateSchedule", "Selected habit ID: $selectedHabitId")
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        selectedHabitId = null
                    }
                }
        }

        habitViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Failed to load habits: $it", Toast.LENGTH_SHORT).show()
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
                    Log.d("CreateSchedule", "Selected date: $selectedDate")
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
                    Log.d("CreateSchedule", "Selected start time: $selectedStart")
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
                    Log.d("CreateSchedule", "Selected end time: $selectedEnd")
                },
                9, // Default to 9:00 instead of 8:00 to avoid end before start
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
                date = selectedDate!!.format(DateTimeFormatter.ISO_LOCAL_DATE),
                start_time = start.format(isoFormatter),
                end_time = end.format(isoFormatter),
                duration_minutes = duration,
                is_custom = true,
                participantIds = emptyList(),
                notes = binding.etNotes.text.toString()
            )

            Log.d("CreateSchedule", "Creating custom schedule: $req")
            createScheduleWithCoroutine { scheduleViewModel.createCustomSchedule(req) }
        }

        binding.btnRecurringSchedule.setOnClickListener {
            if (!validateInputs()) return@setOnClickListener

            val (start, end, duration) = buildDateTimes()

            val req = CreateRecurringScheduleRequest(
                habitId = selectedHabitId!!,
                start_time = start.format(isoFormatter),
                end_time = end.format(isoFormatter),
                duration_minutes = duration,
                repeatPattern = "daily", // Changed from "none" to "daily"
                repeatDays = 7,
                is_custom = true,
                participantIds = emptyList(),
                notes = binding.etNotes.text.toString()
            )

            Log.d("CreateSchedule", "Creating recurring schedule: $req")
            createScheduleWithCoroutine { scheduleViewModel.createRecurringSchedule(req) }
        }

        binding.btnWeekdaySchedule.setOnClickListener {
            if (!validateInputs()) return@setOnClickListener

            val (start, end, duration) = buildDateTimes()

            val req = CreateWeekdayScheduleRequest(
                habitId = selectedHabitId!!,
                start_time = start.format(isoFormatter),
                end_time = end.format(isoFormatter),
                duration_minutes = duration,
                daysOfWeek = listOf(1, 2, 3, 4, 5), // Monday to Friday
                numberOfWeeks = 4,
                participantIds = emptyList(),
                notes = binding.etNotes.text.toString()
            )

            Log.d("CreateSchedule", "Creating weekday schedule: $req")
            createScheduleWithCoroutine { scheduleViewModel.createWeekdaySchedule(req) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createScheduleWithCoroutine(createFunction: suspend () -> Unit) {
        lifecycleScope.launch {
            try {
                createFunction()
                // Success handling is now in observeCreateResults()
            } catch (e: Exception) {
                Log.e("CreateSchedule", "Failed to create schedule: ${e.message}", e)
                Toast.makeText(requireContext(), "Failed to create schedule: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observeCreateResults() {
        scheduleViewModel.createResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                it.onSuccess { response ->
                    Log.d("CreateSchedule", "Schedule created successfully: $response")
                    Toast.makeText(requireContext(), "Schedule created successfully!", Toast.LENGTH_SHORT).show()

                    // Navigate back to home after successful creation
                    findNavController().navigateUp()
                }.onFailure { exception ->
                    Log.e("CreateSchedule", "Schedule creation failed: ${exception.message}", exception)
                    Toast.makeText(requireContext(), "Failed to create schedule: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
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
        val duration = java.time.Duration.between(start, end).toMinutes().toInt()
        Log.d("CreateSchedule", "Built date times - Start: $start, End: $end, Duration: $duration minutes")
        return Triple(start, end, duration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}