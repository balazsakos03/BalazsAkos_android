package com.example.progresshabitplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.progresshabitplanner.databinding.FragmentRegisterBinding
import com.example.progresshabitplanner.ui.auth.AuthViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Regisztráció gomb
        binding.btnRegisterConfirm.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(name, email, password)
            }
        }

        // Login képernyőre vissza
        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        // Regisztrációs eredmény figyelése
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }.onFailure {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
