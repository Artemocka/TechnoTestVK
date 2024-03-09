package com.example.technotestvk.data

import android.graphics.Bitmap

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Double,
    val rating:Float,
    val stock:Int,
    val brand:String,
    val category: String,
    val thumbnail:String,
    val images: MutableList<String>
)
//0
//id	1
//title	"iPhone 9"
//description	"An apple mobile which is nothing like apple"
//price	549
//discountPercentage	12.96
//rating	4.69
//stock	94
//brand	"Apple"
//category	"smartphones"
//thumbnail	"https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
//images
//0	"https://cdn.dummyjson.com/product-images/1/1.jpg"
//1	"https://cdn.dummyjson.com/product-images/1/2.jpg"
//2	"https://cdn.dummyjson.com/product-images/1/3.jpg"
//3	"https://cdn.dummyjson.com/product-images/1/4.jpg"
//4	"https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"