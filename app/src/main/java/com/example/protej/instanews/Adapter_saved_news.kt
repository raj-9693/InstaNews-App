package com.example.protej.instanews

import android.app.Activity
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.protej.instanews.Detailsfirst_Activity.Article


class Adapter_saved_news(val context: Activity,  val listener: OnItemClickListener, ) :
    RecyclerView.Adapter<Adapter_saved_news.Viewholder>() {



    private val articleList = mutableListOf<Article>()

    fun setData(newList: List<Article>) {
        articleList.clear()
        articleList.addAll(newList)
        notifyDataSetChanged()
    }

    fun getArticleAt(position: Int): Article {
        return articleList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_saved_desion, parent, false)
        return Viewholder(view)
    }


    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val setdeta = articleList[position]

        holder.Tital.text = setdeta.title
        holder.Date.text = setdeta.publishedAt

        Glide.with(context)
            .load(setdeta.urlToImage)
            .placeholder(R.drawable.logo2)
            .error(R.drawable.logo2)

            .transform(
                CenterCrop(), RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10f,
                        context.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(holder.Thumbnail)

        holder.itemView.setOnClickListener {
            listener.click(position)
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    interface OnItemClickListener{
        fun click(position: Int)
    }

    class Viewholder(view: View) : RecyclerView.ViewHolder(view) {
        val Tital: TextView = view.findViewById(R.id.title)
        val Thumbnail: ImageView = view.findViewById(R.id.Thumbnail)
        val Date: TextView = view.findViewById(R.id.date)
    }
}
