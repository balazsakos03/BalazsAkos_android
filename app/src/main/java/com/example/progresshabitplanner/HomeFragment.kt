package com.example.progresshabitplanner

import android.os.Build
import android.os.Bundle
import android.util.Log
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

        Log.d("HomeFragment", "=== HOME FRAGMENT STARTED ===")

        val habitAdapter = HabitAdapter(emptyList())
        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabits.adapter = habitAdapter

        // Habitek megfigyelése
        habitViewModel.habits.observe(viewLifecycleOwner) { habits ->
            Log.d("HomeFragment", "Habits updated: ${habits.size} habits")
            habitAdapter.updateData(habits)
        }

        val scheduleAdapter = HomeScheduleAdapter(emptyList())
        binding.recyclerViewSchedules.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSchedules.adapter = scheduleAdapter

        // Schedulök megfigyelése
        homeViewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            Log.d("HomeFragment", "Schedules LiveData updated: ${schedules?.size ?: 0} schedules")

            if (schedules.isNullOrEmpty()) {
                Log.d("HomeFragment", "No schedules - showing empty message")
                binding.tvNoSchedules.visibility = View.VISIBLE
                binding.recyclerViewSchedules.visibility = View.GONE
                binding.tvNoSchedules.text = "No schedules for today"
            } else {
                Log.d("HomeFragment", "Schedules found - updating UI with ${schedules.size} schedules")
                binding.tvNoSchedules.visibility = View.GONE
                binding.recyclerViewSchedules.visibility = View.VISIBLE
                scheduleAdapter.updateData(schedules)
            }
        }

        // Schedule hibák
        homeViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Log.e("HomeFragment", "Error received: $it")
                binding.tvNoSchedules.text = it
                binding.tvNoSchedules.visibility = View.VISIBLE
                binding.recyclerViewSchedules.visibility = View.GONE
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

        // DEBUG: Hosszú kattintás a profil gombon
        binding.btnGoToProfile.setOnLongClickListener {
            Log.d("HomeFragment", "Debug button pressed - loading all schedules")
            homeViewModel.loadAllSchedulesForDebug()
            true
        }

        // Adatok betöltése
        Log.d("HomeFragment", "Loading initial data...")
        habitViewModel.loadHabits()
        homeViewModel.loadTodaySchedules()

        // DEBUG: Azonnal töltsük be az összes schedule-t is
        homeViewModel.loadAllSchedulesForDebug()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "onResume - refreshing data")
        homeViewModel.loadTodaySchedules()
        habitViewModel.loadHabits()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}