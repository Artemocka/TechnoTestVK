package com.example.technotestvk.data

data class Page(val index:Int=0, val isRequesting:Boolean= false){

    fun getLimit():Int{
        return 20
    }
    
    fun getSkip():Int{
        return index*20
    }
    
    
}