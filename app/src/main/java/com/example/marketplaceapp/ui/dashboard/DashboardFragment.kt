package com.example.marketplaceapp.ui.dashboard

import ListingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marketplaceapp.databinding.FragmentDashboardBinding
import com.example.marketplaceapp.R
import android.widget.*
import com.example.marketplaceapp.ui.additem.AddItemFragment
import com.example.marketplaceapp.DatabaseHelper
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val binding = _binding!!

        //login check
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            findNavController().navigate(R.id.LoginFragment)
            return binding.root
        }

        //load listings
        val db = DatabaseHelper(requireContext())
        val listings = db.getAllUsers()

        if (listings.isEmpty()) {
            Toast.makeText(requireContext(), "No listings found", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerListings.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerListings.adapter = ListingAdapter(listings) { selectedItem ->

            val action = DashboardFragmentDirections
                .actionNavDashboardToItemDetailFragment(
                    itemTitle = selectedItem.item,
                    itemDescription = selectedItem.description,
                    itemImageUri = selectedItem.imageUri ?: "",
                    sellerName = selectedItem.name,
                    itemAddress = selectedItem.address
                )

            findNavController().navigate(action)
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(com.example.marketplaceapp.R.id.addItemFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val db = DatabaseHelper(requireContext())
        val updatedListing = db.getAllUsers()

        binding.recyclerListings.adapter = ListingAdapter(updatedListing) { selectedItem ->

            val action = DashboardFragmentDirections
                .actionNavDashboardToItemDetailFragment(
                    itemTitle = selectedItem.item,
                    itemDescription = selectedItem.description,
                    itemImageUri = selectedItem.imageUri ?: "",
                    sellerName = selectedItem.name,
                    itemAddress = selectedItem.address
                )

            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
