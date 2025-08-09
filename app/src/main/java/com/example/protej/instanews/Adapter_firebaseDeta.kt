package com.example.protej.instanews

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.protej.instanews.Detailsfirst_Activity.Article
import com.squareup.picasso.Picasso


class Adapter_firebaseDeta(val context: Activity, val MyDeta: ArrayList<item_firebase>, val listner: OnItemclickListner)
    : RecyclerView.Adapter<Adapter_firebaseDeta.Viewholder>() {

    interface OnItemclickListner{

        fun onitemclick(position: Int)


    }

    class Viewholder(view: View): RecyclerView.ViewHolder(view){

// Firebase_Deta
        val Name=view.findViewById<TextView>(R.id.titleText)
        val Image=view.findViewById<ImageView>(R.id.imageView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val set= LayoutInflater.from(parent.context).inflate(R.layout.newsitem_card,parent,false)
        return Viewholder(set)


    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        // Firebase
       val setDeta=MyDeta[position]


        // Firebase
        holder.Name.text=setDeta.Name
        Picasso.get()
            .load(setDeta.Image)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.Image)
          holder.itemView.setOnClickListener {
    listner.onitemclick(position)
}


    }

    override fun getItemCount(): Int {
        return MyDeta.size
    }



}