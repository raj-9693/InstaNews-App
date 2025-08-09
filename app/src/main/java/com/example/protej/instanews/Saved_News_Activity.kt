package com.example.protej.instanews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protej.instanews.DerailsSecond_Activity.Detalse_Activity
import com.example.protej.instanews.Detailsfirst_Activity.Article
import com.example.protej.instanews.Detailsfirst_Activity.Roomdetabase
import com.example.protej.instanews.databinding.ActivitySavedNewsBinding
import kotlinx.coroutines.launch

class Saved_News_Activity : AppCompatActivity(), Adapter_saved_news.OnItemClickListener {
    lateinit var binding: ActivitySavedNewsBinding

    lateinit var detabase: Roomdetabase


    lateinit var Adapter: Adapter_saved_news
    private lateinit var mainLayout: ConstraintLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySavedNewsBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)


        detabase = Roomdetabase.getDatabase(applicationContext)


        mainLayout = findViewById(R.id.main)

        Adapter = Adapter_saved_news(this,this,)

        binding.savedRecycleview.layoutManager= LinearLayoutManager(this)

        binding.savedRecycleview.adapter=Adapter

            detabase.StudentDao().getAlldetanews().observe(this) { list ->

                Log.d("RoomData", "List Size: ${list.size}")
                Adapter.setData(list)

                if (list.isEmpty()) {
                    mainLayout.setBackgroundResource(R.drawable.newsbackground1)
                } else {
                    mainLayout.setBackgroundResource(R.drawable.backbround2)
                }
            }




        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article =Adapter.getArticleAt(position)

                lifecycleScope.launch {
                    detabase.StudentDao().Deletenews(article)
                    Toast.makeText(this@Saved_News_Activity, "Deleted!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.savedRecycleview)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun click(position: Int) {
        val ApiDeta = Adapter.getArticleAt(position)




        val intent = Intent(this, Detalse_Activity::class.java)

        intent.putExtra("Image", ApiDeta.urlToImage)

        intent.putExtra("Author", ApiDeta.author)
        intent.putExtra("Title", ApiDeta.title)
        intent.putExtra("Content",ApiDeta.content)
        intent.putExtra("Description", ApiDeta.description)
        intent.putExtra("Urllink", ApiDeta.url)
        startActivity(intent)





    }
}

