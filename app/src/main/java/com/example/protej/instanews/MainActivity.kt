package com.example.protej.instanews

import android.R.attr.animation
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.protej.instanews.DerailsSecond_Activity.Detalse_Activity
import com.example.protej.instanews.Detailsfirst_Activity.ApiNewsInterface
import com.example.protej.instanews.Detailsfirst_Activity.Article
import com.example.protej.instanews.Detailsfirst_Activity.Details_Activity
import com.example.protej.instanews.Detailsfirst_Activity.News_Deta
import com.example.protej.instanews.Detailsfirst_Activity.Roomdetabase
import com.example.protej.instanews.databinding.ActivityMainBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import org.chromium.base.ThreadUtils.runOnUiThread
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class MainActivity : AppCompatActivity(), Adapter_firebaseDeta.OnItemclickListner, NEWS_API_ADAPTER.OnItemclickListner {

    lateinit var binding: ActivityMainBinding


    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var loadingDialog: AlertDialog


    lateinit var firebaseDeta: ArrayList<item_firebase>


    lateinit var shimmer: ShimmerFrameLayout
     lateinit var shimmer2: ShimmerFrameLayout
    private lateinit var Adapter: Adapter_firebaseDeta
    lateinit var detabase: Roomdetabase
    private lateinit var AdapterApi: NEWS_API_ADAPTER

    lateinit var responceDeta:List<Article>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        detabase = Roomdetabase.getDatabase(applicationContext)
        firebaseDeta= ArrayList()


        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)

        shimmer = binding.shimmerActivity

        shimmer.startShimmer()
        binding.shimmerActivity.visibility= View.VISIBLE
        binding.horizontalItem.visibility = View.GONE

       shimmer2 =binding.shimmerActivity2
        binding.shimmerActivity2.startShimmer()
        shimmer2.visibility=View.VISIBLE
        binding.Recycleview1.visibility = View.GONE


        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
//

        binding.horizontalItem.layoutManager = layoutManager

        Adapter=Adapter_firebaseDeta(this@MainActivity, firebaseDeta,this@MainActivity)


        binding.horizontalItem.adapter = Adapter



        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("imageUrls")

        fetchImageUrls()

        searchCity()
Api_news()





        binding.logoImage.setOnClickListener {
            it.startAnimation(animation)
    val intent = Intent(this, Saved_News_Activity::class.java)
    startActivity(intent)

}



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun Api_news(searchnews:String="News") {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiNewsInterface::class.java)

        val retrofitResponse = retrofit.getNewsArticles(searchnews, "popularity", "ff3fbb51f20e40c299206f994852ab47")


        retrofitResponse.enqueue(object : retrofit2.Callback<News_Deta?> {
            override fun onResponse(p0: Call<News_Deta?>, p1: Response<News_Deta?>) {


                responceDeta = p1.body()?.articles!!


                binding.Recycleview1.layoutManager = LinearLayoutManager(this@MainActivity)

                AdapterApi = NEWS_API_ADAPTER(
                    this@MainActivity,
                    responceDeta,
                    this@MainActivity,
                    onSaveClick = { article ->


                        lifecycleScope.launch {
                            if (!detabase.StudentDao().isNewsSaved(
                                    article.title,
                                    article.publishedAt,
                                    article.urlToImage.toString()
                                )
                            ) {
                                detabase.StudentDao().insertNews(article)
                                Log.d("RoomSave", "Article Saved: $article")
                                Toast.makeText(
                                    this@MainActivity,
                                    "Successfully Saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Already Saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })


                runOnUiThread {
                    binding.Recycleview1.adapter = AdapterApi
                    binding.Recycleview1.setHasFixedSize(true)


                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE


                    binding.Recycleview1.visibility = View.VISIBLE

                    fun onAdapterFullyLoaded() {
                        hideLoadingDialog()
                    }

                }

            }

            override fun onFailure(p0: Call<News_Deta?>, p1: Throwable) {


                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to load news", Toast.LENGTH_SHORT).show()
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE

                }
            }

        })


    }
    fun onAdapterFullyLoaded() {
        hideLoadingDialog()

    }



    private fun searchCity() {
        val searchEditText = binding.searchEditText

        searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                showLoadingDialog(query)
                Api_news(query)
            }
            true
        }
    }




private fun showLoadingDialog(query: String) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_search, null)
    val tvLoadingText = dialogView.findViewById<TextView>(R.id.namesearch)
    tvLoadingText.text = "Searching for \"$query\"..."

    val builder = AlertDialog.Builder(this)
    builder.setView(dialogView)
    builder.setCancelable(false)

    loadingDialog = builder.create()


    loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    loadingDialog.show()


    val widthInDp = 260
    val heightInDp = 310

    val widthInPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(), resources.displayMetrics
    ).toInt()

    val heightInPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, heightInDp.toFloat(), resources.displayMetrics
    ).toInt()


    loadingDialog.window?.setLayout(widthInPx, heightInPx)
}


    private fun hideLoadingDialog() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }



    private fun fetchImageUrls() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                firebaseDeta.clear()

                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(item_firebase::class.java)
                    item?.let {
                        firebaseDeta.add(it)
                        Log.i("CategoryCheck", "Loaded item: ${it.Name}")
                    }
                }
                Log.d("FirebaseData", "Items Loaded: ${firebaseDeta.size}")

                runOnUiThread {
                    if (firebaseDeta.isNotEmpty()) {
                        Adapter.notifyDataSetChanged()
                        shimmer.stopShimmer()
                        shimmer.visibility = View.GONE
                        binding.horizontalItem.visibility = View.VISIBLE
                    } else {

                        Toast.makeText(this@MainActivity, "कोई डेटा नहीं मिला", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "डेटा लोड करने में विफल", Toast.LENGTH_SHORT).show()
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                }
            }
        })
    }




    override fun onitemclick(position: Int) {

    val FirebaseData=firebaseDeta[position]

        val intent= Intent(this, Details_Activity::class.java)

        intent.putExtra("Name", FirebaseData.Name)

        startActivity(intent)




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

