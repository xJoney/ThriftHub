package com.example.marketplaceapp.ui.additem

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.DatabaseHelper
import com.example.marketplaceapp.R
import com.example.marketplaceapp.databinding.FragmentAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var auth: FirebaseAuth

    private var selectedImageUri: Uri? = null
    private var selectedVideoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        dbHelper = DatabaseHelper(requireContext())

        val user = auth.currentUser ?: run {
            findNavController().navigate(R.id.LoginFragment)
            return binding.root
        }

        binding.addressInput.isFocusable = false
        binding.addressInput.isClickable = true
        binding.addressInput.setOnClickListener {
            findNavController().navigate(R.id.action_addItemFragment_to_locationPickerFragment)
        }

        setFragmentResultListener("locationRequestKey") { _, bundle ->
            val address = bundle.getString("selectedAddress") ?: return@setFragmentResultListener
            binding.addressInput.setText(address)
        }

        binding.btnAddImage.setOnClickListener { showMediaPicker() }

        binding.addButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val item = binding.itemInput.text.toString()
            val address = binding.addressInput.text.toString()
            val price = binding.priceInput.text.toString()
            val description = binding.descInput.text.toString()

            if (name.isEmpty() || item.isEmpty() || address.isEmpty() ||
                price.isEmpty() || description.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mediaUriString = when {
                selectedImageUri != null -> "image:${selectedImageUri!!.path}"
                selectedVideoUri != null -> "video:${selectedVideoUri!!.path}"
                else -> null
            }

            val ok = dbHelper.insertUser(
                user.uid, name, item, address, price, description, mediaUriString
            )

            if (ok) {
                Toast.makeText(requireContext(), "Item added!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Insert failed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addbtnBack.setOnClickListener { findNavController().navigateUp() }
        return binding.root
    }

    private fun showMediaPicker() {
        val options = arrayOf("Take Photo", "Choose Photo", "Take Video", "Choose Video")

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Select Media")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> takePhotoLauncher.launch(null)
                    1 -> pickImageLauncher.launch(Intent(Intent.ACTION_PICK).apply { type = "image/*" })
                    2 -> takeVideoLauncher.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE))
                    3 -> pickVideoLauncher.launch(Intent(Intent.ACTION_PICK).apply { type = "video/*" })
                }
            }
            .show()
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult
            val safeUri = copyUriToInternalStorage(uri)
            selectedImageUri = safeUri
            selectedVideoUri = null
            binding.itemImagePreview.setImageURI(safeUri)
        }
    }

    private val pickVideoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult
            val safeUri = copyFileToInternalStorage(uri)
            selectedVideoUri = safeUri
            selectedImageUri = null
            safeUri?.let { showVideoThumbnail(it) }
        }
    }

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val uri = saveBitmapToInternalStorage(bitmap)
            selectedImageUri = uri
            selectedVideoUri = null
            binding.itemImagePreview.setImageBitmap(bitmap)
        }
    }

    private val takeVideoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult
            val safeUri = copyFileToInternalStorage(uri)
            selectedVideoUri = safeUri
            selectedImageUri = null
            safeUri?.let { showVideoThumbnail(it) }
        }
    }

    private fun showVideoThumbnail(uri: Uri) {
        val path = uri.path ?: return

        @Suppress("DEPRECATION")
        val thumbnail = android.media.ThumbnailUtils.createVideoThumbnail(
            path,
            android.provider.MediaStore.Video.Thumbnails.MINI_KIND
        )

        if (thumbnail != null) {
            binding.itemImagePreview.setImageBitmap(thumbnail)
        } else {
            binding.itemImagePreview.setImageResource(android.R.drawable.ic_media_play)
        }
    }


    private fun copyFileToInternalStorage(uri: Uri): Uri? {
        return try {
            val input = requireContext().contentResolver.openInputStream(uri) ?: return null
            val file = File(requireContext().filesDir, "vid_${System.currentTimeMillis()}.mp4")
            FileOutputStream(file).use { input.copyTo(it) }
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }

    private fun copyUriToInternalStorage(uri: Uri): Uri? {
        return try {
            val input = requireContext().contentResolver.openInputStream(uri) ?: return null
            val file = File(requireContext().filesDir, "img_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { input.copyTo(it) }
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }

    private fun saveBitmapToInternalStorage(bitmap: android.graphics.Bitmap): Uri? {
        return try {
            val file = File(requireContext().filesDir, "photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use {
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, it)
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
