package com.example.mynews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynews.R
import com.example.mynews.ui.Db.ArticleDatabase
import com.example.mynews.ui.NewsViewModelProvidefactry
import com.example.mynews.ui.NewsViewmodel
import com.example.mynews.ui.adapter.Newsadapter
import com.example.mynews.ui.repository.Newsrepository
import com.example.mynews.ui.util.Constants.Companion.QUERY_PAGE_CNT
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
                        newsadapter.differ.submitList(res.articles.toList())
                        val totalpages=res.totalResults/ QUERY_PAGE_CNT+2
                        islastpage=viewmodel.breakingpage==totalpages
                        if(islastpage){
                                rvBreakingNews.setPadding(0,0,0,0)

                        }
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
        isloading=true
    }

    private fun hideprogressbar(){
        paginationProgressBar.visibility=View.INVISIBLE
        isloading=false
    }

    var isloading=false
    var islastpage=false
    var isscrolling=false

    var scrolllistener=object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutmanager=recyclerView.layoutManager as LinearLayoutManager
            val firstvisibleitemposition=layoutmanager.findFirstVisibleItemPosition()
            val visiblecount=layoutmanager.childCount
            val totalitemcnt=layoutmanager.itemCount

            val isnotloadinandnotlastpage=!isloading&&!islastpage
            val islastitem=firstvisibleitemposition+visiblecount>=totalitemcnt
            val isnotbeginnig=firstvisibleitemposition>=0
            val istotalnotvisisble=totalitemcnt>=QUERY_PAGE_CNT
            val shouldpaginate=isnotloadinandnotlastpage&&islastitem&&isnotbeginnig&&istotalnotvisisble&&isscrolling
            if(shouldpaginate){
                viewmodel.getbreakingnews("us")
                isscrolling=false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                isscrolling=true
        }
    }
    private fun setuprecyclerview(){
        newsadapter= Newsadapter()
        rvBreakingNews.apply {
            adapter=newsadapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingnewsFragment.scrolllistener)
        }
    }
}