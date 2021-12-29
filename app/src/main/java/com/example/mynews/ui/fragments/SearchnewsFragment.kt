package com.example.mynews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynews.R
import com.example.mynews.ui.Db.ArticleDatabase
import com.example.mynews.ui.NewsActivity
import com.example.mynews.ui.NewsViewModelProvidefactry
import com.example.mynews.ui.NewsViewmodel
import com.example.mynews.ui.adapter.Newsadapter
import com.example.mynews.ui.repository.Newsrepository
import com.example.mynews.ui.util.Constants.Companion.search_news_delay
import com.example.mynews.ui.util.Resource
import kotlinx.android.synthetic.main.searchnews.*
import kotlinx.android.synthetic.main.searchnews.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchnewsFragment:Fragment(R.layout.searchnews) {
    lateinit var viewmodel: NewsViewmodel
    lateinit var newsadapter: Newsadapter

    val TAG="Searchnews"
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
            findNavController().navigate(R.id.action_SearchnewsFragment_to_ArticlenewsFragment,bundle)
        }

        var job:Job?=null
        etSearch.addTextChangedListener{editable->
            job?.cancel()
            job= MainScope().launch {
                delay(search_news_delay)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewmodel.searchnews(editable.toString())
                    }
                }
            }
        }

        viewmodel?.searchnew?.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.success->{
                    hideprogressbar()
                    it.data?.let {res->
                        newsadapter.differ.submitList(res.articles)
                    }
                }
                is Resource.Error->{
                    hideprogressbar()
                    it.message?.let {msg->
                        Log.d(TAG,"Error Occured:$msg")
                    }
                }
                is Resource.loading->{
                    showprogressbar()
                }
            }
        })
    }

    private fun showprogressbar(){
        paginationProgressBar.visibility=View.VISIBLE
    }

    private fun hideprogressbar(){
        paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun setuprecyclerview(){
        newsadapter= Newsadapter()
        rvSearchNews.apply {
            adapter=newsadapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}