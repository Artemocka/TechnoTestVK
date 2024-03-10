package com.example.technotestvk.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.technotestvk.data.Product
import com.example.technotestvk.databinding.ProductCardBinding
import com.example.technotestvk.poop


class ProductRecyclerViewAdapter(
    private val listener: OnItemListener,
) : ListAdapter<Product, ProductRecyclerViewAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductCardBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        poop("onBindViewHolder(): $position")
        val item = currentList[position]
        holder.bind(item)

        if (position == itemCount.dec()) {
            listener.onEnd()
        }
    }


    class ViewHolder(
        private val binding: ProductCardBinding,
        private val listener: OnItemListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Product
        init {
            binding.root.setOnClickListener { listener.onItemClick(item) }
        }
        fun bind(item: Product) {
            this.item = item
            binding.run {
                price.text = "$${item.price}"
                title.text = item.title
                description.text = item.description
                Glide.with(binding.root)
                    .load(item.thumbnail)
                    .into(thumbnail)
            }
        }
    }
}

interface OnItemListener {
    fun onEnd()
    fun onItemClick(item: Product)
}
