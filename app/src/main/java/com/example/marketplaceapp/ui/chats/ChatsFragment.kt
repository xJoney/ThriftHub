package com.example.marketplaceapp.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketplaceapp.databinding.FragmentChatsBinding
import androidx.navigation.fragment.findNavController
import androidx.core.widget.addTextChangedListener

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        val chatItems = listOf(
            ChatData("Jonathan", "Hey, is this still available?"),
            ChatData("Alina", "I'll send details soon."),
            ChatData("Jerry", "Meetup tomorrow?"),
            ChatData("Nehmat", "Here's my address.")
        )

        val adapter = ChatsAdapter(chatItems) { selectedChat ->
            val action = ChatsFragmentDirections
                .actionNavChatsToChatDetailFragment(
                    selectedChat.name,
                    selectedChat.lastMessage
                )
            findNavController().navigate(action)
        }

        binding.chatsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.chatsRecycler.adapter = adapter


        binding.searchChats.addTextChangedListener { text ->
            adapter.filter(text.toString())
        }

        return binding.root
    }
}
