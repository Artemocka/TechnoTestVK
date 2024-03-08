package com.example.technotestvk.productrecycler

import androidx.recyclerview.widget.DiffUtil
import com.example.technotestvk.Product

class ProductDiffCallback: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}