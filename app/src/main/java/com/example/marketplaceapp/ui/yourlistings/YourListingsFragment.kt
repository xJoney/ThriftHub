package com.example.marketplaceapp.ui.yourlistings

import ListingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marketplaceapp.DatabaseHelper
import com.example.marketplaceapp.databinding.FragmentYourListingsBinding
import com.example.marketplaceapp.ui.dashboard.ListingData
import com.google.firebase.auth.FirebaseAuth

class YourListingsFragment : Fragment() {

    private var _binding: FragmentYourListingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYourListingsBinding.inflate(inflater, container, false)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            findNavController().navigateUp()
            return binding.root
        }

        val db = DatabaseHelper(requireContext())
        val listings: List<ListingData> = db.getListingsByUser(user.uid)

        binding.recyclerYourListings.layoutManager =
            GridLayoutManager(requireContext(), 2)

        binding.recyclerYourListings.adapter =
            ListingAdapter(listings) { selected: ListingData ->

                val action =
                    YourListingsFragmentDirections
                        .actionYourListingsFragmentToEditListingFragment(
                            listingId        = selected.id,
                            itemTitle        = selected.item,
                            itemAddress      = selected.address,
                            itemPrice        = selected.price,
                            itemDescription  = selected.description,
                            itemImageUri     = selected.imageUri ?: ""
                        )

                findNavController().navigate(action)
            }

        binding.listitembtnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
