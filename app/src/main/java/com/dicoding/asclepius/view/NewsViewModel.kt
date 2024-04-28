package com.dicoding.asclepius.view

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import com.dicoding.asclepius.data.retrofit.ApiService
import com.dicoding.asclepius.helper.NewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    constructor() : this(Application())

    private val apiService: ApiService

    private val _newsResponse = MutableLiveData<NewsResponse>()
    val newsResponse: LiveData<NewsResponse> = _newsResponse

    private val _listArticlesItem = MutableLiveData<List<ArticlesItem>>()
    val listArticlesItem: LiveData<List<ArticlesItem>> = _listArticlesItem

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        fetchCancerNewsFromApi()
    }

    private fun fetchCancerNewsFromApi() {
        apiService.getHealthNews(
            "cancer",
            "health",
            "en",
            "e18e2a9312e54f5685d685f9bb3ab2d0"
        ).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles ?: emptyList()
                    displayArticles(articles)
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun displayArticles(articles: List<ArticlesItem>) {
        _listArticlesItem.value = articles
    }

    companion object {
        private const val TAG = "NewsViewModel"
    }
}