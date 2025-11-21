package com.example.progresshabitplanner

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.progresshabitplanner.data.UserPreferences
import com.example.progresshabitplanner.databinding.FragmentEditProfileBinding
import com.example.progresshabitplanner.utils.ImageSaver
import java.io.File

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPrefs: UserPreferences
    private var localImagePath: String? = null

    // Képkiválasztó launcher
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Mentjük le az app saját mappájába
            val savedPath = ImageSaver.saveImageFromUri(requireContext(), it)
            if (savedPath != null) {
                localImagePath = savedPath
                binding.ivEditProfileImage.setImageURI(Uri.fromFile(File(savedPath)))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        userPrefs = UserPreferences(requireContext())

        // Jelenlegi adatok betöltése
        binding.etEditName.setText(userPrefs.getUserName())
        binding.etEditEmail.setText(userPrefs.getUserEmail())

        // Profilkép betöltése, ha létezik
        val savedPath = userPrefs.getImageUri()
        if (!savedPath.isNullOrEmpty()) {
            val file = File(savedPath)
            if (file.exists()) {
                localImagePath = savedPath
                binding.ivEditProfileImage.setImageURI(Uri.fromFile(file))
            }
        }

        // Kép választása gomb
        binding.btnChooseImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Mentés gomb
        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etEditName.text.toString().ifBlank { "Ákos" }
            val email = binding.etEditEmail.text.toString().ifBlank { "balazsakos81@gmail.com" }

            userPrefs.saveUser(
                name = name,
                email = email,
                imageUri = localImagePath
            )

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}