package com.example.progresshabitplanner


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.progresshabitplanner.data.UserPreferences
import com.example.progresshabitplanner.databinding.FragmentProfileBinding
import java.io.File

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPrefs = UserPreferences(requireContext())

        binding.tvProfileName.text = userPrefs.getUserName()
        binding.tvProfileEmail.text = userPrefs.getUserEmail()

        val savedPath = userPrefs.getImageUri()
        if (!savedPath.isNullOrEmpty()) {
            val file = File(savedPath)
            if (file.exists()) {
                binding.ivProfile.setImageURI(Uri.fromFile(file))
            }
        }

        binding.btnBackHome.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}