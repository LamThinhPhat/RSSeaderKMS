package com.example.ssreaderkms.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.ssreaderkms.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val webViewPage = findViewById<WebView>(R.id.webViewPage)
        val linkPageFromContentActivity = intent.getStringExtra("UrlPage")

        webViewPage.loadUrl(linkPageFromContentActivity!!)
        webViewPage.webViewClient = WebViewClient()

    }
}