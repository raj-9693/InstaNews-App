package com.example.protej.instanews.webviews

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.protej.instanews.R
import com.example.protej.instanews.databinding.ActivityWebviewsBinding

class webviews_Activity : AppCompatActivity() {
    lateinit var binding: ActivityWebviewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)



        binding.webviewsid.setupWebView()
        val so=intent.getStringExtra("UrlLink")
        binding.webviewsid.loadUrl(so.toString())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    /**
     * Call it like:  binding.webView.setupWebView()
     */
    fun WebView.setupWebView() {

        webViewClient = WebViewClient()


        with(settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true


            builtInZoomControls = true
            displayZoomControls = false
        }


    }
}
