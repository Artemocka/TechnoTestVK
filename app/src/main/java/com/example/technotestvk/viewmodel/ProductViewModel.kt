package com.example.technotestvk.viewmodel

import androidx.lifecycle.ViewModel
import com.example.technotestvk.data.Product
import java.util.Stack


class ProductViewModel() : ViewModel() {

    var item: Stack<Product> = Stack()


}