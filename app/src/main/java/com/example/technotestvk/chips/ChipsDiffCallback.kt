package com.example.technotestvk.chips

import androidx.recyclerview.widget.DiffUtil
import com.example.technotestvk.data.FilterChip

class ChipsDiffCallback: DiffUtil.ItemCallback<FilterChip>() {
    override fun areItemsTheSame(oldItem: FilterChip, newItem: FilterChip): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FilterChip, newItem: FilterChip): Boolean {
        return oldItem == newItem
    }

}