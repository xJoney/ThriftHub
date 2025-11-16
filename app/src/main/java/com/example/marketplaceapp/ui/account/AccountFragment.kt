package com.example.marketplaceapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.marketplaceapp.databinding.FragmentAccountBinding
import android.widget.Toast
import com.example.marketplaceapp.R
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val auth = FirebaseAuth.getInstance()
        binding.rowLogin.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_nav_account_to_LoginFragment)
        }
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            findNavController().navigate(R.id.LoginFragment)
            return binding.root
        }

        if (user != null) {
            binding.rowLoginText.text = "Sign out"
        }

        binding.rowListings.setOnClickListener {
            findNavController().navigate(R.id.yourListingsFragment)
            Toast.makeText(requireContext(), "View Listings clicked", Toast.LENGTH_SHORT).show()
        }

        binding.rowSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
        }

        binding.rowLogin.setOnClickListener {
            val action = AccountFragmentDirections.actionNavAccountToLoginFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }
}
