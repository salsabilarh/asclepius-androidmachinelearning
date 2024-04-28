package com.dicoding.asclepius.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.response.ArticlesItem

class NewsAdapter(private val articles: List<ArticlesItem>) :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImageView: ImageView = itemView.findViewById(R.id.articleImageView)
        val articleTitleTextView: TextView = itemView.findViewById(R.id.articleTitleTextView)
        val articleDescriptionTextView: TextView =
            itemView.findViewById(R.id.articleDescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.articleTitleTextView.text = article.title
        holder.articleDescriptionTextView.text = article.description

        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .placeholder(R.drawable.ic_place_holder)
            .into(holder.articleImageView)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}