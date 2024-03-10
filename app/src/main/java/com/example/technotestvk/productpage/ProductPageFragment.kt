package com.example.technotestvk.productpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.technotestvk.api.ProductApi
import com.example.technotestvk.categoryrecycler.CategoryCardRecyclerViewAdapter
import com.example.technotestvk.data.Product
import com.example.technotestvk.data.Products
import com.example.technotestvk.databinding.FragmentProductPageBinding
import com.example.technotestvk.mainpage.getRetrofitClient
import com.example.technotestvk.productrecycler.ProductImageRecyclerViewAdapter
import com.example.technotestvk.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductPageFragment : Fragment(), CategoryCardRecyclerViewAdapter.OnItemListener {
    private val imageAdapter = ProductImageRecyclerViewAdapter()
    private val categoryAdapter = CategoryCardRecyclerViewAdapter(this)
    private val viewModel by viewModels<ProductViewModel>(::requireActivity)
    private val productApi = getRetrofitClient().create(ProductApi::class.java)
    private var item: Product? = null
    private lateinit var catergoryProducts: Products
    private lateinit var binding: FragmentProductPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.item.isEmpty()) {
            val id = ProductPageFragmentArgs.fromBundle(requireArguments()).id
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    item = productApi.getProductById(id)
                    catergoryProducts = productApi.getCategories(item!!.category)
                    catergoryProducts = Products(catergoryProducts.products.filter { product: Product -> product!=item })
                    viewModel.item.push(item)
                } catch (_: Exception) {

                }
            }
        } else {
            item = viewModel.item.peek()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    catergoryProducts = productApi.getCategories(item!!.category)
                    catergoryProducts = Products(catergoryProducts.products.filter { product: Product -> product!=item })
                    lifecycleScope.launch (Dispatchers.Main.immediate){
                        categoryAdapter.submitList(catergoryProducts.products)
                    }
                } catch (_: Exception) {
                }
            }
        }

        item
            ?.takeIf { it.images.first().contains("thumbnail.jpg") }
            ?.images?.swapFirstAndLast()

        imageAdapter.submitList(item?.images)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProductPageBinding.inflate(layoutInflater)


        binding.run {
            val item = item!!

            //toolbar
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            toolbar.title = item.title
            ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->

                toolbar.updatePaddingRelative(
                    top = insets.getInsets(
                        WindowInsetsCompat.Type.statusBars() or
                                WindowInsetsCompat.Type.displayCutout()
                    ).top
                )
                categoryRv.updatePaddingRelative(
                    bottom = binding.categoryRv.paddingTop + insets.getInsets(
                        WindowInsetsCompat.Type.navigationBars()
                    ).bottom
                )
                insets
            }


            //RecyclerView
            rvList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvList.adapter = imageAdapter
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvList)

            //Page
            title.text = item.title
            price.text = "$${item.price}"
            description.text = item.description
            rating.text = item.rating.toString()
            stock.text = item.stock.toString()
            category.text = item.category
            brand.text = item.brand

            //RecyclerView
            categoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            categoryRv.adapter = categoryAdapter


        }


        return binding.root
    }



    override fun onItemClick(item: Product) {
        val action = ProductPageFragmentDirections.actionProductToProduct(item.id)
        viewModel.item.push(item)
        findNavController().navigate(action)
    }

}

fun MutableList<String>.swapFirstAndLast() {
    if (size >= 2) {
        val firstItem = this.first()
        this[0] = this.last()
        this[this.size - 1] = firstItem
    }
}

