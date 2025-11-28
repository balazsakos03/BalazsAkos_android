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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.progresshabitplanner.data.UserPreferences
import com.example.progresshabitplanner.databinding.FragmentLoginBinding
import com.example.progresshabitplanner.repository.ProfileRepository
import com.example.progresshabitplanner.ui.auth.AuthViewModel
import com.example.progresshabitplanner.utils.SessionManager
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    private lateinit var profileRepository: ProfileRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        profileRepository = ProfileRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Login gomb
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Email and password are required", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }

        // Register gomb
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Auth eredmény figyelése
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { authResponse ->

                Toast.makeText(
                    requireContext(),
                    "Welcome ${authResponse.user.name}",
                    Toast.LENGTH_LONG
                ).show()

                val session = SessionManager(requireContext())
                session.saveAuthToken(authResponse.tokens.accessToken)

                // smooth login
                val prefs = UserPreferences(requireContext())
                prefs.setLoggedIn(true)

                // MOST TÖLTJÜK LE A TELJES PROFILT A BACKENDRŐL!!
                lifecycleScope.launch {
                    try {
                        val profile = profileRepository.getMyProfile()

                        // user adatok mentése
                        prefs.saveUser(
                            name = profile.username ?: "",
                            email = profile.email ?: "",
                            imageUri = null
                        )

                        // profilkép mentése
                        prefs.saveProfileImageBase64(profile.profileImageBase64)
                        prefs.saveProfileImageUrl(profile.profileImageUrl)

                        // belépés
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            requireContext(),
                            "Failed to load profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Login failed: ${it.message}",
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
