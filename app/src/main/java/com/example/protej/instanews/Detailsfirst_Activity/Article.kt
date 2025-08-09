package com.example.protej.instanews.Detailsfirst_Activity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "News")

data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val author: String?=null,
    val content: String,
    val description: String,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String? = null,
    var isSaved: Boolean = false  // add this field


)