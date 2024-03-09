package com.example.technotestvk

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.technotestvk.api.ProductApi
import com.example.technotestvk.databinding.FragmentProductsBinding
import com.example.technotestvk.recycler.OnItemListener
import com.example.technotestvk.recycler.ProductRecyclerViewAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductPage : Fragment(), OnItemListener {
    private lateinit var binding: FragmentProductsBinding
    private var page = Page()
    private val adapter = ProductRecyclerViewAdapter(this)
    private val productApi = getRetrofitClient().create(ProductApi::class.java)
    private val viewModel by viewModels<ProductViewModel>(::requireActivity)
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNextPage()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        binding = FragmentProductsBinding.inflate(layoutInflater)
        binding.rvList.layoutManager = GridLayoutManager(context, 2)
        binding.rvList.adapter = adapter

        binding.reload.setOnClickListener{
            requestNextPage()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(Type.systemBars() or Type.displayCutout())
            val rvPadding = binding.rvList.paddingTop
            binding.rvList.setPadding(systemBars.left + rvPadding, rvPadding, systemBars.right + rvPadding, systemBars.bottom + rvPadding)
            binding.toolbar.updatePaddingRelative(top = systemBars.top)
            insets
        }

        return binding.root
    }

    override fun onEnd() {
        requestNextPage()

    }

    override fun onItemClick(item: Product) {
        val action = ProductPageDirections.actionProductsToProductPageFragment2(item.id)
        viewModel.item = item
        findNavController().navigate(action)
    }

    private fun requestNextPage() {
        if (page.isRequesting)
            return
        val list = adapter.currentList.toMutableList()
        snackbar?.dismiss()

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                binding.reload.isEnabled = false
            }
            page = page.copy(isRequesting = true)
            try {
                list.addAll(productApi.getPage(page.getSkip(), page.getLimit())!!.products)
                withContext(Dispatchers.Main.immediate) {
                    binding.reload.isVisible= false
                    adapter.submitList(list)

                }
                page = page.copy(index = page.index.inc(), isRequesting = false)
            } catch (e: Exception) {
                poop(e.toString())
                page = page.copy(isRequesting = false)
                withContext(Dispatchers.Main) {
                    Snackbar.make(binding.root, "Oops, something went wrong!", Snackbar.LENGTH_INDEFINITE)
                        .also { snackbar = it }
                        .show()
                    if (adapter.currentList.isEmpty()) {
                        binding.reload.isVisible = true
                        binding.reload.isEnabled = true
                    }
                }
            }


        }
    }

}

fun getRetrofitClient(): Retrofit {
    val retrofit = Retrofit.Builder().baseUrl("https://dummyjson.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit
}

