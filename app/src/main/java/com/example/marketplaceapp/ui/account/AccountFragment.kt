package com.example.marketplaceapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.marketplaceapp.databinding.FragmentAccountBinding
import android.widget.Toast
import androidx.navigation.fragment.findNavController


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)


        binding.rowListings.setOnClickListener {
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
