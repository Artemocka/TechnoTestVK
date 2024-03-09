package com.example.technotestvk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.technotestvk.api.ProductApi
import com.example.technotestvk.databinding.FragmentProductPageBinding
import com.example.technotestvk.productrecycler.ProductImageRecyclerViewAdapter
import com.example.technotestvk.recycler.ProductRecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductPageFragment : Fragment() {
    private val adapter = ProductImageRecyclerViewAdapter()
    private val viewModel by viewModels<ProductViewModel>(::requireActivity)
    private val productApi = getRetrofitClient().create(ProductApi::class.java)

    private lateinit var binding: FragmentProductPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.item == null) {
            val id = ProductPageFragmentArgs.fromBundle(requireArguments()).id
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    viewModel.item = productApi.getProductById(id)
                }catch (e:Exception){

                }
            }
        }
        viewModel.item
            ?.takeIf { it.images.first().contains("thumbnail.jpg") }
            ?.images?.swapFirstAndLast()

        adapter.submitList(viewModel.item?.images)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProductPageBinding.inflate(layoutInflater)

        binding.toolbar.title = viewModel.item?.title
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->

            binding.toolbar.updatePaddingRelative(
                top = insets.getInsets(
                    WindowInsetsCompat.Type.statusBars() or
                            WindowInsetsCompat.Type.displayCutout()
                ).top
            )


            insets
        }
        binding.run {
            val item = viewModel.item!!
            //toolbar
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            //RecyclerView
            rvList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvList.adapter = adapter
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.rvList)

            //Page
            title.text = item.title
            description.text = item.description
            rating.text = item.rating.toString()
            stock.text = "Остаток: ${item.stock}"
            category.text = item.category

        }


        return binding.root
    }

}

fun MutableList<String>.swapFirstAndLast() {
    if (size >= 2) {
        val firstItem = this.first()
        this[0] = this.last()
        this[this.size - 1] = firstItem
    }
}

