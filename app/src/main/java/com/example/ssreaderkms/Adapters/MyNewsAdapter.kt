package com.example.ssreaderkms.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ssreaderkms.Models.News
import com.example.ssreaderkms.R
import com.squareup.picasso.Picasso

class MyNewsAdapter(private val NewsList : List<News>): RecyclerView.Adapter<MyNewsAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View):RecyclerView.ViewHolder(listItemView)
    {
        val imageNews = listItemView.findViewById<ImageView>(R.id.imageNews)
        val titleNews = listItemView.findViewById<TextView>(R.id.titleNews)
        val descriptionNews = listItemView.findViewById<TextView>(R.id.descriptionNews)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNewsAdapter.ViewHolder {
        val context =parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.news_items,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyNewsAdapter.ViewHolder, position: Int) {

        val newsRow : News =NewsList.get(position)

        Picasso.get().load(NewsList[position].imageURL).into(holder.imageNews)

        val titleRow = holder.titleNews
        titleRow.text = newsRow.title

        val descriptionRow = holder.descriptionNews
        descriptionRow.text = newsRow.description

    }

    override fun getItemCount(): Int {
        return NewsList.size
    }
}