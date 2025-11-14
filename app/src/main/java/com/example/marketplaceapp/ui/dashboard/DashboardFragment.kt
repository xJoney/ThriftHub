package com.example.marketplaceapp.ui.dashboard

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
import com.example.marketplaceapp.AddItemActivity
import com.example.marketplaceapp.DatabaseHelper
import android.content.Intent


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // initialize db and get all users data
        val db = DatabaseHelper(requireContext())
        val listings = db.getAllUsers()
        //testing
        if (listings.isEmpty()) {
            Toast.makeText(requireContext(), "No listings found in database", Toast.LENGTH_SHORT).show()
        }
        // Setup RecyclerView layout and adapter
        binding.recyclerListings.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerListings.adapter = ListingAdapter(listings) { selectedItem ->
            Toast.makeText(requireContext(), "Clicked: ${selectedItem.item}", Toast.LENGTH_SHORT).show()
        }
        // opens add item activity
        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener {
           val intent = Intent(requireContext(), AddItemActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    // reloads the data from the database to get updated ver.
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
                    sellerName = selectedItem.name
                )

            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
