package com.example.technotestvk.data

data class Page(
    val index: Int = 0,
    val filter: String? = null,
    val isRequesting: Boolean = false,
    val list: MutableList<Product> = mutableListOf(),
) {
    fun getLimit(): Int {
        return 20
    }

    fun getSkip(): Int {
        return index * 20
    }
}