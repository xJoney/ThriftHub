package com.example.marketplaceapp.ui.yourlistings

import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.DatabaseHelper
import com.example.marketplaceapp.databinding.FragmentEditListingBinding
import java.io.File

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

        val args = EditListingFragmentArgs.fromBundle(requireArguments())

        binding.editTitle.setText(args.itemTitle)
        binding.editAddress.setText(args.itemAddress)
        binding.editPrice.setText(args.itemPrice)
        binding.editDescription.setText(args.itemDescription)

        loadMediaPreview(args.itemImageUri)

        binding.btnUpdate.setOnClickListener {
            val success = db.updateListingById(
                id = args.listingId,
                newItem = binding.editTitle.text.toString(),
                newAddress = binding.editAddress.text.toString(),
                newPrice = binding.editPrice.text.toString(),
                newDescription = binding.editDescription.text.toString(),
                newImageUri = args.itemImageUri // keep same media
            )

            Toast.makeText(requireContext(),
                if (success) "Listing Updated" else "Update Failed",
                Toast.LENGTH_SHORT
            ).show()

            if (success) findNavController().navigateUp()
        }

        binding.btnDelete.setOnClickListener {
            val deleted = db.deleteListingById(args.listingId)

            Toast.makeText(requireContext(),
                if (deleted) "Listing Deleted" else "Delete Failed",
                Toast.LENGTH_SHORT
            ).show()

            if (deleted) findNavController().navigateUp()
        }

        binding.edititembtnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun loadMediaPreview(media: String?) {
        if (media.isNullOrEmpty()) return

        val fileName = File(media.removePrefix("image:").removePrefix("video:")).name
        val file = File(requireContext().filesDir, fileName)

        if (!file.exists()) {
            return
        }

        when {
            media.startsWith("image:") -> {
                val bmp = BitmapFactory.decodeFile(file.absolutePath)
                binding.imgPreview.setImageBitmap(bmp)
            }

            media.startsWith("video:") -> {
                @Suppress("DEPRECATION")
                val thumbnail = ThumbnailUtils.createVideoThumbnail(
                    file.absolutePath,
                    MediaStore.Video.Thumbnails.MINI_KIND
                )
                binding.imgPreview.setImageBitmap(thumbnail)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
