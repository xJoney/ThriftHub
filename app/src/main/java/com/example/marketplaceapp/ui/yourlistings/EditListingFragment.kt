package com.example.marketplaceapp.ui.yourlistings
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.DatabaseHelper
import com.example.marketplaceapp.databinding.FragmentEditListingBinding

class EditListingFragment : Fragment() {

    private var _binding: FragmentEditListingBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditListingBinding.inflate(inflater, container, false)
        db = DatabaseHelper(requireContext())

        // get args sent from YourListingsFragment
        val args = EditListingFragmentArgs.fromBundle(requireArguments())

        // show original data
        binding.editTitle.setText(args.itemTitle)
        binding.editAddress.setText(args.itemAddress)
        binding.editPrice.setText(args.itemPrice)
        binding.editDescription.setText(args.itemDescription)

        if (!args.itemImageUri.isNullOrEmpty()) {
            binding.imgPreview.setImageURI(Uri.parse(args.itemImageUri))
        }

        //update
        binding.btnUpdate.setOnClickListener {
            val updatedItem = binding.editTitle.text.toString()
            val updatedAddress = binding.editAddress.text.toString()
            val updatedPrice = binding.editPrice.text.toString()
            val updatedDescription = binding.editDescription.text.toString()
            val updatedImageUri = args.itemImageUri   // keep same image for now

            val success = db.updateListingById(
                id = args.listingId,
                newItem = updatedItem,
                newAddress = updatedAddress,
                newPrice = updatedPrice,
                newDescription = updatedDescription,
                newImageUri = updatedImageUri
            )

            if (success) {
                Toast.makeText(requireContext(), "Listing Updated", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

        //delete
        binding.btnDelete.setOnClickListener {
            val deleted = db.deleteListingById(args.listingId)

            if (deleted) {
                Toast.makeText(requireContext(), "Listing Deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
