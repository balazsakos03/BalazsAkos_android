package com.example.progresshabitplanner

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.progresshabitplanner.databinding.FragmentScheduleDetailBinding
import com.example.progresshabitplanner.ui.schedule.ScheduleDetailViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleDetailFragment : Fragment() {

    private var _binding: FragmentScheduleDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleDetailViewModel by viewModels()

    private var scheduleId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleId = arguments?.getInt("scheduleId") ?: -1
        if (scheduleId == -1) {
            Toast.makeText(requireContext(), "Invalid schedule", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        viewModel.loadSchedule(scheduleId)

        viewModel.schedule.observe(viewLifecycleOwner) { schedule ->
            // Habit title (name)
            binding.tvTitle.text = schedule.habit.name

            // Format time properly (HH:mm)
            val startTime = formatTime(schedule.start_time)
            val endTime = formatTime(schedule.end_time)
            binding.tvTime.text = "$startTime - $endTime"

            // Format date
            val scheduleDate = formatDate(schedule.date)
            binding.tvDate.text = scheduleDate

            // Duration
            val duration = if (schedule.duration_minutes > 0) {
                "Duration: ${schedule.duration_minutes} minutes"
            } else {
                "Duration: Not specified"
            }
            binding.tvDuration.text = duration

            // Status
            binding.tvStatus.text = "Status: ${schedule.status}"

            // Schedule type
            val scheduleType = if (schedule.is_custom) "Custom Schedule" else "Recurring Schedule"
            binding.tvType.text = "Type: $scheduleType"

            // Notes - show "No notes" if empty
            binding.tvNotes.text = schedule.notes?.ifBlank { "No notes provided" } ?: "No notes provided"
        }

        binding.btnDelete.setOnClickListener {
            // Show confirmation dialog before deleting
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Schedule")
                .setMessage("Are you sure you want to delete this schedule?")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.deleteSchedule(scheduleId)
                }
                .setNegativeButton("Cancel", null)
                .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                requireContext().getColor(R.color.red)
            )

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                requireContext().getColor(R.color.textSecondary)
            )
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Schedule deleted successfully", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }.onFailure { e ->
                Toast.makeText(requireContext(), "Delete failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTime(dateTimeString: String): String {
        return try {
            // Try to parse as ISO date time
            if (dateTimeString.contains("T")) {
                val timePart = dateTimeString.substringAfter("T").substringBefore(".")
                if (timePart.length >= 5) timePart.substring(0, 5) else timePart
            } else {
                // Fallback: manual extraction
                dateTimeString.substring(11, 16)
            }
        } catch (e: Exception) {
            // Ultimate fallback - manual extraction
            if (dateTimeString.length >= 16) {
                dateTimeString.substring(11, 16)
            } else {
                dateTimeString
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(dateString: String): String {
        return try {
            if (dateString.contains("T")) {
                // Format as "November 22, 2025"
                val date = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
                date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
            } else {
                dateString
            }
        } catch (e: Exception) {
            // Fallback: just show the original string
            dateString
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}