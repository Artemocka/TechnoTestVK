package com.example.technotestvk.categoryrecycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.technotestvk.data.Product
import com.example.technotestvk.databinding.CategoryCardBinding
import com.example.technotestvk.recycler.OnItemListener


class CategoryCardRecyclerViewAdapter(private val listener: OnItemListener,) : ListAdapter<Product, CategoryCardRecyclerViewAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = ViewHolder(binding, listener)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = currentList[position]
        holder.bind(item)

    }


    class ViewHolder(
        private val binding: CategoryCardBinding,
        private val listener: OnItemListener,

        ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: Product

        init {
            binding.root.setOnClickListener { listener.onItemClick(item) }

        }

        fun bind(item: Product) {
            this.item = item
//            Log.e("",item)

            binding.run {

                title.text = item.title
                price.text = "$${item.price}"

                Glide.with(binding.root)
                    .load(item.thumbnail)
                    .into(thumbnail)


            }
        }
    }
    interface OnItemListener {
        fun onItemClick(item: Product)
    }
}


