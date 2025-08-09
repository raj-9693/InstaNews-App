package com.example.protej.instanews.Detailsfirst_Activity

import android.R.attr.visibility
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.protej.instanews.DerailsSecond_Activity.Detalse_Activity
import com.example.protej.instanews.MainActivity
import com.example.protej.instanews.NEWS_API_ADAPTER
import com.example.protej.instanews.R
import com.example.protej.instanews.databinding.ActivityDetailsBinding
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Details_Activity : AppCompatActivity(), NEWS_API_ADAPTER.OnItemclickListner {
   lateinit var  binding: ActivityDetailsBinding

    private lateinit var AdapterApi: NEWS_API_ADAPTER

    lateinit var responceDeta:List<Article>
    lateinit var detabase: Roomdetabase
lateinit var shimmer: ShimmerFrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.my_status_bar_color)

        detabase = Roomdetabase.getDatabase(applicationContext)
        shimmer = binding.shimmercategery

        shimmer.startShimmer()
        binding.shimmercategery.visibility= View.VISIBLE
        binding.recycleviewcategary2.visibility = View.GONE



        val itemName=intent.getStringExtra("Name")
       binding.titleText.text=itemName



        binding.backButtonnews.setOnClickListener {
            intent= Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        Api_news(itemName.toString())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun Api_news(searchnews:String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiNewsInterface::class.java)

        val retrofitResponse = retrofit.getNewsArticles(searchnews, "popularity", "ff3fbb51f20e40c299206f994852ab47")


        retrofitResponse.enqueue(object : retrofit2.Callback<News_Deta?> {
            override fun onResponse(p0: Call<News_Deta?>, p1: Response<News_Deta?>) {

                responceDeta= p1.body()?.articles!!


                binding.recycleviewcategary2.layoutManager=LinearLayoutManager(this@Details_Activity)

                AdapterApi= NEWS_API_ADAPTER(this@Details_Activity, responceDeta,this@Details_Activity,
                    onSaveClick = { article ->

                        lifecycleScope.launch {
                            detabase.StudentDao().insertNews(article)
                            Log.d("RoomSave", "Article Saved: ${article.title}")
                            Toast.makeText(this@Details_Activity, "Successfully Saved", Toast.LENGTH_SHORT).show()
                        }
                    })

                runOnUiThread {
                    binding.recycleviewcategary2.adapter = AdapterApi
                    binding.recycleviewcategary2.setHasFixedSize(true)


                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE


                    binding.recycleviewcategary2.visibility = View.VISIBLE
                }

            }


            override fun onFailure(p0: Call<News_Deta?>, p1: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@Details_Activity, "Failed to load news", Toast.LENGTH_SHORT).show()
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                }

            }
        })


    }

    override fun onitemclick2(position: Int) {

val ApiDeta=responceDeta[position]
        val intent= Intent(this, Detalse_Activity::class.java)

        intent.putExtra("Image", ApiDeta.urlToImage)
        intent.putExtra("Author", ApiDeta.author)
        intent.putExtra("Title", ApiDeta.title)
        intent.putExtra("Content",ApiDeta.content)
        intent.putExtra("Description", ApiDeta.description)
        intent.putExtra("Urllink", ApiDeta.url)


        startActivity(intent)
    }


}