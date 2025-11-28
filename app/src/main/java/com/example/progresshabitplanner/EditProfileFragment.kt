package com.example.progresshabitplanner

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.progresshabitplanner.data.UserPreferences
import com.example.progresshabitplanner.databinding.FragmentEditProfileBinding
import com.example.progresshabitplanner.repository.ImageRepository
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPrefs: UserPreferences
    private lateinit var imageRepository: ImageRepository

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { uploadImage(uri) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        userPrefs = UserPreferences(requireContext())
        imageRepository = ImageRepository(requireContext())

        binding.etEditName.setText(userPrefs.getUserName())
        binding.etEditEmail.setText(userPrefs.getUserEmail())

        loadBase64Image(userPrefs.getProfileImageBase64())

        binding.btnChooseImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etEditName.text.toString().ifBlank { "Ákos" }
            val email = binding.etEditEmail.text.toString().ifBlank { "balazsakos81@gmail.com" }

            userPrefs.saveUser(name, email, null)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun uploadImage(uri: Uri) {
        val file = uriToFile(uri)
        val userPrefs = UserPreferences(requireContext())

        lifecycleScope.launch {
            try {
                val response = imageRepository.uploadProfileImage(file)

                val base64 = response.profileImageBase64
                android.util.Log.d("UploadImage", "Base64 length = ${base64?.length}")

                if (!base64.isNullOrEmpty()) {
                    userPrefs.saveProfileImageBase64(base64)
                    loadBase64Image(base64)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadBase64Image(base64: String?) {
        if (base64.isNullOrEmpty()) return

        val bytes = Base64.decode(base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        Glide.with(this)
            .load(bitmap)
            .into(binding.ivEditProfileImage)
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File.createTempFile("profile_upload_edit", ".jpg", requireContext().cacheDir)
        inputStream?.use { input -> file.outputStream().use { output -> input.copyTo(output) } }
        return file
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
