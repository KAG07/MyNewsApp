package com.example.mynews.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynews.ui.models.Article
import com.example.mynews.ui.models.NewsResponse
import com.example.mynews.ui.repository.Newsrepository
import com.example.mynews.ui.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewmodel(val repository: Newsrepository):ViewModel() {
    val breakingnew:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingpage=1
    var breakingnewsreponse:NewsResponse?=null
    val searchnew:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchpage=1
    var searchgnewsreponse:NewsResponse?=null
    init {
        getbreakingnews("us")
    }

    fun getbreakingnews(countrycode:String)= viewModelScope.launch {
            breakingnew.postValue(Resource.loading())
            val response=repository.getbreakingnews(countrycode,breakingpage)
            breakingnew.postValue(handlebreakingnewsreponse(response))
        }

    fun searchnews(searchquer:String)=viewModelScope.launch {
        searchnew.postValue(Resource.loading())
        val response=repository.searchquery(searchquer,searchpage)
        searchnew.postValue(handlesearchnewsreponse(response))
    }

    private fun handlebreakingnewsreponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingpage++
                if(breakingnewsreponse==null){
                    breakingnewsreponse=resultResponse}
                else{
                    val oldarticle=breakingnewsreponse?.articles
                    val newarticle=resultResponse?.articles
                    oldarticle?.plus(newarticle)
                }
                return Resource.success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlesearchnewsreponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchpage++
                if(searchgnewsreponse==null){
                    searchgnewsreponse=resultResponse}
                else{
                    val oldarticle=searchgnewsreponse?.articles
                    val newarticle=resultResponse?.articles
                    oldarticle?.plus(newarticle)
                }
                return Resource.success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    fun savenews(article: Article)=viewModelScope.launch {
            repository.upsert(article)
    }

    fun getsavednews()=repository.getsavednews()

    fun deletearticle(article: Article)=viewModelScope.launch {
        repository.deletearticle(article)
    }
}