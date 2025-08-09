package com.example.protej.instanews.DerailsSecond_Activity

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.protej.instanews.R
import com.example.protej.instanews.databinding.ActivityDetalseBinding
import com.example.protej.instanews.webviews.webviews_Activity
import java.util.Locale

class Detalse_Activity : AppCompatActivity(),TextToSpeech.OnInitListener {
    lateinit var binding: ActivityDetalseBinding

    private lateinit var tts: TextToSpeech
    private lateinit var tvDescription: TextView
    private lateinit var btnSpeak: Button
    private var isSpeaking = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityDetalseBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)


        tvDescription= binding.newsContent

        val imageUrl = intent.getStringExtra("Image")

        Glide.with(this)
            .load(imageUrl)

            .placeholder(R.drawable.logo2)
            .error(R.drawable.logo2)

            .transform(CenterCrop(), RoundedCorners(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10f,
                    this.resources.displayMetrics
                ).toInt()
            ))

            .into(binding.newsImage)


        binding.newsAuthor.text=intent.getStringExtra("Author")
        binding.newsTitle.text=intent.getStringExtra("Title")
        binding.newsContent.text=intent.getStringExtra("Content")

        binding.newsDescription.text=intent.getStringExtra("Description")

           val button=  intent.getStringExtra("Urllink")




        tts = TextToSpeech(this, this)


        val apiText =intent.getStringExtra("Content")


        tvDescription.text = apiText



        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)


        binding.btnSpeak.setOnClickListener {
            it.startAnimation(animation)
            if (isSpeaking) {

                Toast.makeText(this,"Stop",Toast.LENGTH_SHORT).show()

                tts.stop()
                isSpeaking = false
            } else {

                speakText(tvDescription.text.toString())
                isSpeaking = true
            }
        }



        binding.btnMoreInfo.setOnClickListener {

         val intent= Intent(this, webviews_Activity::class.java)

         intent.putExtra("UrlLink", button)
             startActivity(intent)


         it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(110).withEndAction {
             it.animate().scaleX(1f).scaleY(1f).duration = 110
             Toast.makeText(this, "More info", Toast.LENGTH_SHORT).show()
         }.start()
     }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun speakText(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            val result = tts.setLanguage(Locale("en", "IN"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Indian English not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}
