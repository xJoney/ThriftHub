package com.example.marketplaceapp.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.databinding.FragmentChatDetailBinding

class ChatDetailFragment : Fragment() {

    private lateinit var binding: FragmentChatDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ChatDetailFragmentArgs.fromBundle(requireArguments())

        binding.chatHeaderText.text = args.chatName

        binding.chatHistory.text = "Last message: ${args.chatLastMessage}"

          binding.chatbtnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
