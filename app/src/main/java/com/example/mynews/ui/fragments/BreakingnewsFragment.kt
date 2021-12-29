package com.example.mynews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynews.R
import com.example.mynews.ui.Db.ArticleDatabase
import com.example.mynews.ui.NewsViewModelProvidefactry
import com.example.mynews.ui.NewsViewmodel
import com.example.mynews.ui.adapter.Newsadapter
import com.example.mynews.ui.repository.Newsrepository
import com.example.mynews.ui.util.Resource
import kotlinx.android.synthetic.main.breakingnews.*


class BreakingnewsFragment:Fragment(R.layout.breakingnews) {
    lateinit  var viewmodel:NewsViewmodel
    lateinit var newsadapter:Newsadapter

     val TAG="BREAKINGNEWS"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsRepository = Newsrepository(ArticleDatabase(requireContext()))
        val vmProviderFactory = NewsViewModelProvidefactry(newsRepository)
        viewmodel = ViewModelProvider(this, vmProviderFactory).get(NewsViewmodel::class.java)

        setuprecyclerview()

        newsadapter.setOnItemClicklistener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_BreakingnewsFragment_to_ArticlenewsFragment,
                bundle
            )
        }
        viewmodel.breakingnew.observe(viewLifecycleOwner, Observer {
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
        rvBreakingNews.apply {
            adapter=newsadapter
            layoutManager=LinearLayoutManager(activity)
        }
    }
}