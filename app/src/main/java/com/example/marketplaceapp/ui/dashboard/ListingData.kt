package com.example.marketplaceapp.ui.dashboard

data class ListingData(
    val name: String,
    val item: String,
    val address: String,
    val price: String,
    val description: String,
    val imageUri: String? = null
)
