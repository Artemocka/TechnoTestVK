package com.example.technotestvk.productrecycler

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.technotestvk.databinding.ProductImageBinding


class ProductImageRecyclerViewAdapter() : ListAdapter<String, ProductImageRecyclerViewAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }


    class ViewHolder(
        private val binding: ProductImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {

            Log.e("",item)
            binding.run {
                Glide.with(binding.root)
                    .load(item)
                    .into(imageView)


            }
        }
    }
}


