package com.example.protej.instanews

import android.app.Activity
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.protej.instanews.Detailsfirst_Activity.Article


class  NEWS_API_ADAPTER(val context: Activity, var NewDeta: List<Article>,private val listner2: OnItemclickListner,private val onSaveClick: (Article) -> Unit )
        : RecyclerView.Adapter<NEWS_API_ADAPTER.Viewholder>() {



        interface OnItemclickListner{

            fun onitemclick2(position: Int)



        }

        class Viewholder(view: View): RecyclerView.ViewHolder(view){


            val Tital=view.findViewById<TextView>(R.id.title)
            val Thumbnail=view.findViewById<ImageView>(R.id.Thumbnail)
            val Date=view.findViewById<TextView>(R.id.date)
            val Saved=view.findViewById<LottieAnimationView>(R.id.save_animation)
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

            val newsSet= LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)

            return Viewholder(newsSet)



        }

        override fun onBindViewHolder(holder: Viewholder, position: Int) {
            val setNew = NewDeta[position]

            holder.Tital.text = setNew.title
            holder.Date.text = setNew.publishedAt



            Glide.with(context)
                .load(setNew.urlToImage)
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

            holder.Saved.setOnClickListener {
                if (!holder.Saved.isAnimating) {
                    if (!setNew.isSaved) {
                        holder.Saved.playAnimation()
                        onSaveClick(setNew)
                    } else {
                        Toast.makeText(context, "Already Saved", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            holder.itemView.setOnClickListener {
                listner2.onitemclick2(position)
            }

            if (position == NewDeta.size - 1) {

                (context as? MainActivity)?.onAdapterFullyLoaded()
            }
        }
    override fun getItemCount(): Int {
        return NewDeta.size
    }
}








