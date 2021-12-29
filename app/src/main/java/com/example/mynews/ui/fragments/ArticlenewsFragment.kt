package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.mynews.R
import com.example.mynews.ui.Db.ArticleDatabase
import com.example.mynews.ui.NewsActivity
import com.example.mynews.ui.NewsViewModelProvidefactry
import com.example.mynews.ui.NewsViewmodel
import com.example.mynews.ui.repository.Newsrepository
import kotlinx.android.synthetic.main.articlenews.*

class ArticlenewsFragment:Fragment(R.layout.articlenews) {
    lateinit var viewmodel: NewsViewmodel
    val args:ArticlenewsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsRepository = Newsrepository(ArticleDatabase(requireContext()))
        val vmProviderFactory = NewsViewModelProvidefactry(newsRepository)
        viewmodel = ViewModelProvider(this, vmProviderFactory).get(NewsViewmodel::class.java)

        val article=args.article
        webView.apply {
            webViewClient= WebViewClient()
            article.url?.let { loadUrl(it) }
        }
    }
}