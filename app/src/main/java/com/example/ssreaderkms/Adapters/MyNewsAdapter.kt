package com.example.ssreaderkms.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ssreaderkms.Models.News
import com.example.ssreaderkms.R
import com.example.ssreaderkms.SplashActivity.Companion.accountUserLogin
import com.squareup.picasso.Picasso

class MyNewsAdapter(private val NewsList : List<News>
,private val adaptercontext : Context): RecyclerView.Adapter<MyNewsAdapter.ViewHolder>() {

    var onMarkClick : ((News, Boolean) -> Unit)? = null
    var onClickNews: ((String) -> Unit)? = null

    inner class ViewHolder(listItemView: View):RecyclerView.ViewHolder(listItemView)
    {
        val imageNews = listItemView.findViewById<ImageView>(R.id.imageNews)
        val titleNews = listItemView.findViewById<TextView>(R.id.titleNews)
        val markIc = listItemView.findViewById<ImageView>(R.id.markImg)

        init {
            markIc.setOnClickListener {
                val tempDraw = ContextCompat.getDrawable(adaptercontext, R.drawable.ic_mark)
                if (markIc.drawable.constantState!!.equals(tempDraw!!.constantState))
                {
                    markIc.setImageResource(R.drawable.ic_unmark)
                    onMarkClick?.invoke(NewsList[adapterPosition], false)
                }
                else
                {
                    markIc.setImageResource(R.drawable.ic_mark)
                    onMarkClick?.invoke(NewsList[adapterPosition], true)
                }
            }

            imageNews.setOnClickListener {
                onClickNews?.invoke(NewsList[adapterPosition].linkPage!!)
            }

            titleNews.setOnClickListener {
                onClickNews?.invoke(NewsList[adapterPosition].linkPage!!)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNewsAdapter.ViewHolder {
        val context =parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.news_items,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyNewsAdapter.ViewHolder, position: Int) {

        val newsRow : News = NewsList.get(position)

        Picasso.get().load(NewsList[position].imageURL).into(holder.imageNews)

        val titleRow = holder.titleNews
        titleRow.text = newsRow.title

        val markIC = holder.markIc

        if (accountUserLogin!!.markList.any { it.linkPage == newsRow.linkPage })
        {
            markIC.setImageResource(R.drawable.ic_mark)
        }
        else
        {
            markIC.setImageResource(R.drawable.ic_unmark)
        }
    }

    override fun getItemCount(): Int {
        return NewsList.size
    }
}