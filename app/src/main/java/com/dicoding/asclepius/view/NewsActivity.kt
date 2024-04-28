package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.helper.NewsAdapter

class NewsActivity : AppCompatActivity() {
    private val viewModel: NewsViewModel by viewModels {
        ViewModelProvider.NewInstanceFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.rvArticle
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        viewModel.listArticlesItem.observe(this) { articles ->
            recyclerView.adapter = NewsAdapter(articles)
        }
    }
}
