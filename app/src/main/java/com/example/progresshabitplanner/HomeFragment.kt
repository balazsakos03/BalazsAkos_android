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

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // RecyclerView beállítása
        val adapter = HabitAdapter(emptyList())
        binding.recyclerViewSchedules.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSchedules.adapter = adapter

        // Gomb a profilra navigáláshoz
        binding.btnGoToProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        // Megfigyeljük a habit listát
        viewModel.habits.observe(viewLifecycleOwner) { habits ->
            if (habits.isEmpty()) {
                binding.tvNoSchedules.visibility = View.VISIBLE
                binding.recyclerViewSchedules.visibility = View.GONE
                binding.tvNoSchedules.text = "No habits yet"
            } else {
                binding.tvNoSchedules.visibility = View.GONE
                binding.recyclerViewSchedules.visibility = View.VISIBLE
                adapter.updateData(habits)
            }
        }

        // Hibák megjelenítése (pl. ha hálózati hiba van)
        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvNoSchedules.text = it
                binding.tvNoSchedules.visibility = View.VISIBLE
            }
        }

        // Habitek lekérése
        viewModel.loadHabits()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
