package com.furkancoskun.haberler.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.furkancoskun.haberler.R
import com.furkancoskun.haberler.data.model.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*


class NewsAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<News>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    fun getSelectedItemId(position: Int): News = differ.currentList[position]

    inner class NewsViewHolder(itemView: View, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(news: News) = with(itemView) {

            Picasso.get()
                .load(news.imageUrl)
                .into(iv_news)

            tv_title.text = news.title
            tv_category.text = news.category

            setOnClickListener() {
                interaction?.onSelectedNews(adapterPosition, news)
            }

        }
    }

    interface Interaction {
        fun onSelectedNews(position: Int, news: News)
    }
}