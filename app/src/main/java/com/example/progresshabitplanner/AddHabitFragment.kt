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
import com.example.progresshabitplanner.databinding.FragmentAddHabitBinding
import com.example.progresshabitplanner.ui.habit.HabitViewModel
@RequiresApi(Build.VERSION_CODES.O)
class AddHabitFragment : Fragment(){
    private var _binding: FragmentAddHabitBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveHabit.setOnClickListener {
            val name = binding.etHabitName.text.toString().trim()
            val description = binding.etHabitDescription.text.toString().trim()
            val goal = binding.etHabitGoal.text.toString().trim()
            val categoryIdText = binding.etCategoryId.text.toString().trim()

            if (name.isEmpty() || description.isEmpty() || goal.isEmpty() || categoryIdText.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val categoryId = categoryIdText.toIntOrNull()
            if (categoryId == null) {
                Toast.makeText(requireContext(), "Category ID must be a number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createHabit(name, description, categoryId, goal)
        }

        viewModel.createResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { habit ->
                Toast.makeText(
                    requireContext(),
                    "Habit created: ${habit.name}",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigateUp()
            }.onFailure { e ->
                Toast.makeText(
                    requireContext(),
                    "Failed to create habit: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}