package com.example.progresshabitplanner

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.progresshabitplanner.databinding.FragmentCreateScheduleBinding
import com.example.progresshabitplanner.model.*
import com.example.progresshabitplanner.ui.schedule.ScheduleViewModel

@RequiresApi(Build.VERSION_CODES.O)
class CreateScheduleFragment: Fragment() {
    private var _binding: FragmentCreateScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCustomSchedule.setOnClickListener { createCustom() }
        binding.btnRecurringSchedule.setOnClickListener { createRecurring() }
        binding.btnWeekdaySchedule.setOnClickListener { createWeekdays() }

        viewModel.createResult.observe(viewLifecycleOwner) {
            it.onSuccess {
                Toast.makeText(requireContext(), "Schedule created!", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }.onFailure { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createCustom() {
        val request = CreateCustomScheduleRequest(
            habitId = binding.etHabitId.text.toString().toInt(),
            date = binding.etDate.text.toString(),
            start_time = binding.etStart.text.toString(),
            end_time = binding.etEnd.text.toString(),
            duration_minutes = binding.etDuration.text.toString().toInt(),
            notes = binding.etNotes.text.toString(),
            participantIds = emptyList()
        )
        viewModel.createCustomSchedule(request)
    }

    private fun createRecurring() {
        val request = CreateRecurringScheduleRequest(
            habitId = binding.etHabitId.text.toString().toInt(),
            start_time = binding.etStart.text.toString(),
            end_time = binding.etEnd.text.toString(),
            duration_minutes = binding.etDuration.text.toString().toInt(),
            repeatPattern = "none",
            repeatDays = 30,
            notes = binding.etNotes.text.toString()
        )
        viewModel.createRecurringSchedule(request)
    }

    private fun createWeekdays() {
        val request = CreateWeekdayScheduleRequest(
            habitId = binding.etHabitId.text.toString().toInt(),
            start_time = binding.etStart.text.toString(),
            end_time = binding.etEnd.text.toString(),
            duration_minutes = binding.etDuration.text.toString().toInt(),
            daysOfWeek = listOf(1,2,3),
            numberOfWeeks = 4,
            notes = binding.etNotes.text.toString()
        )
        viewModel.createWeekdaySchedule(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}