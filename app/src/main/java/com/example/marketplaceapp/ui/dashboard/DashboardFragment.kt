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

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val items = listOf(
            ListingData("Title 1", "Updated today", R.drawable.ic_launcher_background),
            ListingData("Title 2", "Updated yesterday", R.drawable.ic_launcher_background),
            ListingData("Title 3", "Updated 2 days ago", R.drawable.ic_launcher_background)
        )

        binding.recyclerListings.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.recyclerListings.adapter = ListingAdapter(items) { selectedItem ->
            val action = DashboardFragmentDirections
                .actionNavDashboardToItemDetailFragment(
                    itemTitle = selectedItem.title,
                    itemDescription = selectedItem.updated,
                    itemImage = selectedItem.imageRes
                )

            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
