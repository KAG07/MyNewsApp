package com.example.mynews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController


import androidx.navigation.ui.setupWithNavController
import com.example.mynews.R
import com.example.mynews.ui.Db.ArticleDatabase
import com.example.mynews.ui.repository.Newsrepository

import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = Newsrepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProvidefactry(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewmodel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}