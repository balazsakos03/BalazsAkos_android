package com.example.progresshabitplanner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.progresshabitplanner.data.UserPreferences
import com.example.progresshabitplanner.databinding.FragmentProfileBinding
import com.example.progresshabitplanner.repository.ImageRepository
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageRepository: ImageRepository

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { uploadImage(uri) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageRepository = ImageRepository(requireContext())
        val userPrefs = UserPreferences(requireContext())

        binding.tvProfileName.text = userPrefs.getUserName()
        binding.tvProfileEmail.text = userPrefs.getUserEmail()

        // Betöltjük a Base64 képet
        loadBase64Image(userPrefs.getProfileImageBase64())

        binding.ivProfile.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnBackHome.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.btnLogout.setOnClickListener {
            userPrefs.clear()
            userPrefs.setLoggedIn(false)
            findNavController().navigate(R.id.loginFragment)
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
            .into(binding.ivProfile)
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File.createTempFile("profile_upload", ".jpg", requireContext().cacheDir)
        inputStream?.use { input -> file.outputStream().use { output -> input.copyTo(output) } }
        return file
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
