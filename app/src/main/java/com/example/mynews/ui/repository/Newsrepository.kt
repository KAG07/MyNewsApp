package com.example.mynews.ui.repository

import com.example.mynews.ui.Api.RetrofitInstance
import com.example.mynews.ui.Db.ArticleDatabase

class Newsrepository(val db:ArticleDatabase) {
    suspend fun getbreakingnews(countrycode:String,pagenum:Int)=
        RetrofitInstance.api.getBreakingNews(countrycode,pagenum)

    suspend fun searchquery(searchquer:String,pagenum:Int)=
        RetrofitInstance.api.searchForNews(searchquer,pagenum)
}