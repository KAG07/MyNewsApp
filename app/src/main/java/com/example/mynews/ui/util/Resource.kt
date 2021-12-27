package com.example.mynews.ui.util

open class Resource<T>(val data:T?=null, val message:String?=null) {
    class success<T>(data: T?):Resource<T>(data)
    class Error<T>(msg:String?,data: T?):Resource<T>(data,msg)
    class  loading<T>:Resource<T>()

}