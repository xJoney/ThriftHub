package com.example.marketplaceapp.ui.dashboard

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplaceapp.R
import com.example.marketplaceapp.databinding.ItemListingBinding

class ListingAdapter(
    private val items: List<ListingData>,
    private val onItemClick: (ListingData) -> Unit) : RecyclerView.Adapter<ListingAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemListingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }


    // connects the data object from db to the xml layout
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.txtTitle.text = item.item
        holder.binding.txtUpdated.text = item.description
        holder.binding.txtPrice.text = "$${item.price}"

        if (!item.imageUri.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(item.imageUri)
                val stream = holder.binding.root.context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(stream)
                holder.binding.imgItem.setImageBitmap(bitmap)
                stream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                holder.binding.imgItem.setImageResource(android.R.color.darker_gray)
            }
        } else {
            holder.binding.imgItem.setImageResource(android.R.color.darker_gray)
        }

        holder.binding.root.setOnClickListener { onItemClick(item) }
    }




    override fun getItemCount(): Int = items.size
}
