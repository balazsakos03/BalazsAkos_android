package com.example.progresshabitplanner

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresshabitplanner.databinding.FragmentHomeBinding
import com.example.progresshabitplanner.ui.habit.HabitAdapter
import com.example.progresshabitplanner.ui.habit.HabitViewModel
import com.example.progresshabitplanner.ui.home.HomeScheduleAdapter
import com.example.progresshabitplanner.ui.home.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val habitViewModel: HabitViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        val habitAdapter = HabitAdapter(emptyList())
        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabits.adapter = habitAdapter

        // Habitek megfigyelése
        habitViewModel.habits.observe(viewLifecycleOwner) { habits ->
            habitAdapter.updateData(habits)
        }

        val scheduleAdapter = HomeScheduleAdapter(emptyList())
        binding.recyclerViewSchedules.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSchedules.adapter = scheduleAdapter

        // Schedulök megfigyelése
        homeViewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            if (schedules.isNullOrEmpty()) {
                binding.tvNoSchedules.visibility = View.VISIBLE
                binding.recyclerViewSchedules.visibility = View.GONE
            } else {
                binding.tvNoSchedules.visibility = View.GONE
                binding.recyclerViewSchedules.visibility = View.VISIBLE
                scheduleAdapter.updateData(schedules)
            }
        }

        // Schedule hibák
        homeViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                binding.tvNoSchedules.text = errorMsg
                binding.tvNoSchedules.visibility = View.VISIBLE
            }
        }

        binding.btnGoToProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.btnAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addHabitFragment)
        }

        binding.btnAddSchedule.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createScheduleFragment)
        }

        habitViewModel.loadHabits()
        homeViewModel.loadTodaySchedules()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
