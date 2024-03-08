package com.example.technotestvk.api

import com.example.technotestvk.Product
import com.example.technotestvk.Products
import retrofit2.http.GET
import retrofit2.http.Query


interface ProductApi {
    @GET("products/1")
    suspend fun getProductById():Product

    @GET("products")
    suspend fun getAllProduct(): Products


    @GET("products/")
    suspend fun getPage(@Query("skip") skip:Int,@Query("limit") limit:Int): Products


}