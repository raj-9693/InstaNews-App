package com.example.protej.instanews.Detailsfirst_Activity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DAO_CLASS {

    @Insert

    suspend fun  insertNews(News: Article)

    @Delete
    suspend fun  Deletenews(News: Article)

@Query("SELECT * FROM news ORDER BY id DESC")
fun getAlldetanews(): LiveData<List<Article>>


    @Query("""
    SELECT EXISTS(
        SELECT * FROM News 
        WHERE title = :title AND publishedAt = :publishedAt AND urlToImage = :urlToImage
    )
""")
    suspend fun isNewsSaved(
        title: String,
        publishedAt: String,
        urlToImage: String
    ): Boolean



}