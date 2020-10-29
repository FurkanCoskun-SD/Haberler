package com.furkancoskun.haberler.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkancoskun.haberler.R
import com.furkancoskun.haberler.data.model.News
import com.furkancoskun.haberler.ext.safeNavigate
import com.furkancoskun.haberler.ext.showError
import com.furkancoskun.haberler.ui.BaseFragment
import com.furkancoskun.haberler.ui.adapter.NewsAdapter
import com.furkancoskun.haberler.ui.viewmodel.NewsViewModel
import com.furkancoskun.haberler.utils.Status
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : BaseFragment(R.layout.fragment_news), NewsAdapter.Interaction {

    // - Variables - //
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getNews()
    }

    private fun getNews() {
        viewModel.getNews(
        ).observe(viewLifecycleOwner, Observer { resource ->

            when (resource.status) {
                Status.SUCCESS -> {
                    //dataStateChangeListener.showProgressBar(false)
                    resource.data?.let {
                        newsAdapter.submitList(it.news)
                    }
                }
                Status.ERROR -> {
                    resource.exception?.let { Log.d("getNews", it.showError(context)) }
                }
                Status.EMPTY -> {
                    //dataStateChangeListener.showProgressBar(false)
                    Log.e("getNews", "login : Empty")
                }
                //Status.LOADING -> dataStateChangeListener.showProgressBar(true)
            }
        })
    }

    private fun initRecyclerView() {
        newsAdapter = NewsAdapter(this)
        list_news.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }

    override fun onSelectedNews(position: Int, news: News) {
        val action =
            NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(
                Gson().toJson(news)
            )
        findNavController().navigate(action)
    }

}