package com.example.marketplaceapp.ui.account

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.databinding.FragmentSignupBinding
import com.example.marketplaceapp.R
import com.google.firebase.auth.FirebaseAuth

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.signupbtnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.inputEmailPhone.text.toString()
            val password = binding.inputNewPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Account created!", Toast.LENGTH_SHORT).show()

                        // Navigate to login
                        findNavController().navigate(
                            SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                        )

                    } else {
                        Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }

        return binding.root
    }
}
