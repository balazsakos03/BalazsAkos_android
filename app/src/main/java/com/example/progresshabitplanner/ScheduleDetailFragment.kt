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
import com.example.progresshabitplanner.databinding.FragmentScheduleDetailBinding
import com.example.progresshabitplanner.ui.schedule.ScheduleDetailViewModel

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
            binding.tvTitle.text = schedule.habit.title
            binding.tvTime.text =
                "${schedule.startTime ?: "-"} - ${schedule.endTime ?: "-"}"
            binding.tvNotes.text = schedule.notes ?: "No notes"
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteSchedule(scheduleId)
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Schedule deleted", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }.onFailure { e ->
                Toast.makeText(requireContext(), "Delete failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
