package com.example.marketplaceapp.ui.account

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.marketplaceapp.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        binding.loginbtnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}
