package com.example.marketplaceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class User(
    val name: String,
    val item: String,
    val address: String,
    val price: String,
    val description: String
)

class UserAdapter(private val users: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

        class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.userName)
            val item: TextView = itemView.findViewById(R.id.userItem)
            val address: TextView = itemView.findViewById(R.id.userAddress)
            val price: TextView = itemView.findViewById(R.id.userPrice)
            val description: TextView = itemView.findViewById(R.id.userDescription)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user = users[position]
            holder.name.text = user.name
            holder.item.text = user.item
            holder.address.text = user.address
            holder.price.text = user.price
            holder.description.text = user.description
        }

        override fun getItemCount() = users.size
    }
