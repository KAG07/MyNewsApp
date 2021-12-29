package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynews.R
import com.example.mynews.ui.Db.ArticleDatabase
import com.example.mynews.ui.NewsViewModelProvidefactry
import com.example.mynews.ui.NewsViewmodel
import com.example.mynews.ui.adapter.Newsadapter
import com.example.mynews.ui.repository.Newsrepository
import com.example.mynews.ui.util.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.breakingnews.*
import kotlinx.android.synthetic.main.savednews.*
import kotlinx.android.synthetic.main.searchnews.*

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

        val itemtouchhelper=object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
               return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val pos=viewHolder.adapterPosition
                val article=newsadapter.differ.currentList[pos]
                viewmodel.deletearticle(article)
                Snackbar.make(view,"Article deleted",Snackbar.LENGTH_LONG).apply {
                    setAction("undo"){
                        viewmodel.savenews(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemtouchhelper).apply {
            attachToRecyclerView(rvSavedNews)
        }

        viewmodel.getsavednews().observe(viewLifecycleOwner, Observer {
            newsadapter.differ.submitList(it)
        })
    }





    private fun setuprecyclerview(){
        newsadapter= Newsadapter()
        rvSavedNews.apply {
            adapter=newsadapter
            layoutManager= LinearLayoutManager(activity)

        }
    }
}