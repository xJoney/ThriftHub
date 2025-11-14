package com.example.marketplaceapp.ui.item

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.databinding.FragmentItemDetailBinding
import android.net.Uri
import com.example.marketplaceapp.R


class ItemDetailFragment : Fragment() {

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)

        val args = ItemDetailFragmentArgs.fromBundle(requireArguments())

        binding.itemTitle.text = args.itemTitle
        binding.itemDescription.text = args.itemDescription

        if (!args.itemImageUri.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(args.itemImageUri)
                val stream = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(stream)
                binding.itemImage.setImageBitmap(bitmap)
                stream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                binding.itemImage.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            binding.itemImage.setImageResource(R.drawable.ic_launcher_background)
        }


        binding.buttonMessage.setOnClickListener {
            val action = ItemDetailFragmentDirections.actionItemDetailFragmentToChatDetailFragment(
                chatName = args.sellerName,
                chatLastMessage =  "Start your conversation..."
            )
            findNavController().navigate(action)
        }

        binding.itembtnBack.setOnClickListener {
            findNavController().navigateUp()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
