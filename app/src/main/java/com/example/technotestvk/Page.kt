package com.example.technotestvk

class Page(var page:Int, ){

    
    fun getLimit():Int{
        return 20
    }
    
    fun getSkip():Int{
        return page*20
    }
    
    
}