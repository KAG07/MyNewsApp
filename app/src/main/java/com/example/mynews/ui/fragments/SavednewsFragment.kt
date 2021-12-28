package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mynews.R
import com.example.mynews.ui.Db.ArticleDatabase
import com.example.mynews.ui.NewsActivity
import com.example.mynews.ui.NewsViewModelProvidefactry
import com.example.mynews.ui.NewsViewmodel
import com.example.mynews.ui.repository.Newsrepository

class SavednewsFragment:Fragment(R.layout.savednews) {
    lateinit var viewmodel: NewsViewmodel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsRepository = Newsrepository(ArticleDatabase(requireContext()))
        val vmProviderFactory = NewsViewModelProvidefactry(newsRepository)
        viewmodel = ViewModelProvider(this, vmProviderFactory).get(NewsViewmodel::class.java)
    }
}