package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynews.R
import com.example.mynews.ui.Db.ArticleDatabase
import com.example.mynews.ui.NewsViewModelProvidefactry
import com.example.mynews.ui.NewsViewmodel
import com.example.mynews.ui.adapter.Newsadapter
import com.example.mynews.ui.repository.Newsrepository
import kotlinx.android.synthetic.main.savednews.*

class SavednewsFragment:Fragment(R.layout.savednews) {
    lateinit var viewmodel: NewsViewmodel
    lateinit var newsadapter: Newsadapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsRepository = Newsrepository(ArticleDatabase(requireContext()))
        val vmProviderFactory = NewsViewModelProvidefactry(newsRepository)
        viewmodel = ViewModelProvider(this, vmProviderFactory).get(NewsViewmodel::class.java)
        setuprecyclerview()
        newsadapter.setOnItemClicklistener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_SavednewsFragment_to_ArticlenewsFragment,bundle)
        }
    }

    private fun setuprecyclerview(){
        newsadapter= Newsadapter()
        rvSavedNews.apply {
            adapter=newsadapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}