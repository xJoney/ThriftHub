package com.example.marketplaceapp.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.databinding.FragmentItemDetailBinding

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
        binding.itemImage.setImageResource(args.itemImage)

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
