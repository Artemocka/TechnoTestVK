package com.example.technotestvk.data

data class Page(
    val index: Int = 0,
    val filter: String? = null,
    val search: String? = null,
    val isRequesting: Boolean = false,
    val list: List<Product> = listOf(),
    val categorylist: List<Product> = listOf(),
    val searchList: List<Product> = listOf(),

    ) {
    fun getLimit(): Int {
        return 20
    }

    fun getSkip(): Int {
        return index * 20
    }
}