package com.example.technotestvk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.technotestvk.api.ProductApi
import com.example.technotestvk.databinding.FragmentProductPageBinding
import com.example.technotestvk.recycler.OnItemListener
import com.example.technotestvk.recycler.ProductRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductPage : Fragment(),OnItemListener {
    private lateinit var binding: FragmentProductPageBinding
    private val page = Page(1)
    private val adapter = ProductRecyclerViewAdapter(this)
    private  val productApi = getRetrofitClient().create(ProductApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





            lifecycleScope.launch(Dispatchers.IO) {
            val list = productApi.getPage(0,20)


            withContext(Dispatchers.Main) {

                adapter.submitList(list.products)

            }
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProductPageBinding.inflate(layoutInflater)
        binding.rvList.layoutManager = GridLayoutManager(context,2)
        binding.rvList.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(binding.rvList) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        return binding.root
    }

    override fun onEnd() {
        val list = adapter.currentList.toMutableList()
        lifecycleScope.launch(Dispatchers.IO) {
            list.addAll(productApi.getPage(page.getSkip(),page.getLimit()).products)
            page.page++
            withContext(Dispatchers.Main) {

                adapter.submitList(list)

            }
        }
    }

    override fun onItemClick(item: Product) {
//        TODO("Not yet implemented")
    }


}
fun getRetrofitClient(): Retrofit {
    val retrofit = Retrofit.Builder().baseUrl("https://dummyjson.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit
}

