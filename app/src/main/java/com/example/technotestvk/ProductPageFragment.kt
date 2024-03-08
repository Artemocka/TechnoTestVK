package com.example.technotestvk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (viewModel.item !=null)
        {

        }else{
            val id = ProductPageFragmentArgs.fromBundle(requireArguments()).id
            lifecycleScope.launch(Dispatchers.IO){
                viewModel.item = productApi.getProductById(id)
            }
        }

        binding = FragmentProductPageBinding.inflate(layoutInflater)
        adapter.submitList(viewModel.item?.images)

        viewModel.item?.images?.forEach {
            Log.e("",it)

        }
        binding.rvList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = adapter
        binding.run {

        }


        return binding.root
    }

}