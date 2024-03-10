package com.example.technotestvk.mainpage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.technotestvk.R
import com.example.technotestvk.api.ProductApi
import com.example.technotestvk.chips.ChipsAdapter
import com.example.technotestvk.chips.ChipsAdapter.OnChipListner
import com.example.technotestvk.data.Page
import com.example.technotestvk.data.Product
import com.example.technotestvk.data.toFilterChip
import com.example.technotestvk.databinding.FragmentProductsBinding
import com.example.technotestvk.poop
import com.example.technotestvk.recycler.OnItemListener
import com.example.technotestvk.recycler.ProductRecyclerViewAdapter
import com.example.technotestvk.viewmodel.ProductViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductPage : Fragment(), OnItemListener, OnChipListner {
    private lateinit var binding: FragmentProductsBinding
    private var page = Page()
        set(value) {
            field = value
            poop("page $page")
        }
    private val adapter = ProductRecyclerViewAdapter(this)
    private val chipsAdapter:ChipsAdapter = ChipsAdapter(this)
    private val productApi = getRetrofitClient().create(ProductApi::class.java)
    private val viewModel by viewModels<ProductViewModel>(::requireActivity)
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNextPage()
        lifecycleScope.launch(Dispatchers.IO){
            val list = productApi.getCategories().toFilterChip()
            lifecycleScope.launch(Dispatchers.Main.immediate){
                chipsAdapter.submitList(list)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentProductsBinding.inflate(layoutInflater)
        binding.rvList.layoutManager = GridLayoutManager(context, 2)
        binding.rvList.adapter = adapter

        binding.reload.setOnClickListener {
            requestNextPage()
        }
        binding.chipRv.adapter= chipsAdapter
        binding.chipRv.itemAnimator = null


        binding.toolbar.setOnMenuItemClickListener {
//            binding.toolbar.isVisible = false
            binding.searchBar.root.isVisible = true
            binding.toolbar.menu.findItem(R.id.app_bar_search).isVisible = false
            binding.searchBar.searchEditText.requestFocus()
            binding.searchBar.searchIcon.isVisible = false


            val imm =
                binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(binding.searchBar.searchEditText, 0)

            true
        }

        binding.searchBar.cancelButton.setOnClickListener {
            binding.toolbar.menu.findItem(R.id.app_bar_search).isVisible = true
            binding.searchBar.searchEditText.clearFocus()
            binding.searchBar.searchEditText.text.clear()
            binding.searchBar.root.isVisible = false
            binding.searchBar.searchIcon.isVisible = true
            hideKeyboard()

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
        val action = ProductPageDirections.actionMainToProduct(item.id)
        viewModel.item.push(item)
        findNavController().navigate(action)
    }

    private fun requestNextPage() {
        poop("requestNextPage(): ${page.isRequesting} - ${page.filter}")
        if (page.isRequesting || page.filter!=null)
            return


        snackbar?.dismiss()
        page = page.copy(isRequesting = true)

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                binding.reload.isEnabled = false
            }
            try {
                page.list.addAll(productApi.getPage(page.getSkip(), page.getLimit())!!.products)
                withContext(Dispatchers.Main.immediate) {
                    binding.reload.isVisible = false
                    renderPage()

                }
                page = page.copy(index = page.index.inc(), isRequesting = false)
            } catch (e: Exception) {
                poop(e.toString())
                page = page.copy(isRequesting = false)
                withContext(Dispatchers.Main) {
                    Snackbar.make(binding.root, "Oops, something went wrong!", Snackbar.LENGTH_INDEFINITE)
                        .also { snackbar = it }
                        .show()
                    if (page.list.isEmpty()) {
                        binding.reload.isVisible = true
                        binding.reload.isEnabled = true
                    }
                }
            }


        }
    }
    private fun renderPage(){
        poop("${page.list.size} - ${page.filter}")
        if (page.filter==null){
            adapter.submitList(page.list)
        }else{
            adapter.submitList(page.list.filter { it.category == page.filter })
        }
    }

    private fun hideKeyboard() {
        val imm =
            binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onChipChecked(category: String?) {
        page = page.copy(filter = category)
        renderPage()
    }


}

fun getRetrofitClient(): Retrofit {
    val retrofit = Retrofit.Builder().baseUrl("https://dummyjson.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit
}

