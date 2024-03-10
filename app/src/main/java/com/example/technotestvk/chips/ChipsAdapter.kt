package com.example.technotestvk.chips

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.technotestvk.data.FilterChip
import com.example.technotestvk.databinding.ItemChipBinding
import com.example.technotestvk.recycler.OnItemListener


class ChipsAdapter(private val listener: OnChipListner) : ListAdapter<FilterChip, ChipsAdapter.ViewHolder>(ChipsDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).name.hashCode().toLong()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val viewHolder = ViewHolder(binding)
        binding.root.setOnClickListener {
            val list = currentList.toMutableList()
            list.forEachIndexed { index, filterChip ->
                if (index == viewHolder.adapterPosition) {
                    list[index] = filterChip.copy(isChecked = !filterChip.isChecked)
                    listener.onChipChecked(filterChip.name.takeIf { !filterChip.isChecked })
                } else if (filterChip.isChecked) {
                    list[index] = filterChip.copy(isChecked = false)
                }
            }
            submitList(list)


        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    class ViewHolder(private val binding: ItemChipBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterChip) {
            binding.run {
                chip.text = item.name
                chip.isChecked = item.isChecked
            }
        }

    }

    interface OnChipListner {
        fun onChipChecked(category: String?)
    }

}