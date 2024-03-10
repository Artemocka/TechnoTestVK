package com.example.technotestvk.api

import com.example.technotestvk.data.Product
import com.example.technotestvk.data.Products
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ProductApi {
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("products")
    suspend fun getAllProduct(): Products


    @GET("products/")
    suspend fun getPage(@Query("skip") skip:Int,@Query("limit") limit:Int): Products
    @GET("products/category/{category}")
    suspend fun getCategories(@Path("category") category:String): Products

    @GET("products/categories")
    suspend fun getCategories(): List<String>


}