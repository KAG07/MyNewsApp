package com.example.mynews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mynews.ui.repository.Newsrepository

class NewsViewModelProvidefactry(
    val newsrepository: Newsrepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewmodel(newsrepository) as T
    }
}