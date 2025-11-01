package com.example.marketplaceapp.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplaceapp.databinding.ItemChatBinding

class ChatsAdapter(
    private val originalList: List<ChatData>,
    private val onItemClick: (ChatData) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {

    private var filteredList = originalList.toMutableList()

    inner class ChatViewHolder(val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = filteredList[position]
        holder.binding.chatName.text = item.name
        holder.binding.chatPreview.text = item.lastMessage
        holder.binding.chatInitial.text = item.name.take(1)

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = filteredList.size

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            originalList.toMutableList()
        } else {
            originalList.filter {
                it.name.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}

