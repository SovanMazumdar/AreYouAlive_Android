package com.example.areyouAlright

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)

        // 🔑 REQUIRED WebView settings
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            textZoom = 100
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }

        webView.webViewClient = AreYouAliveWebViewClient()

        // 🔑 VERY IMPORTANT: layout params
        webView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        setContentView(webView)
        title = BuildConfig.APP_NAME

        // 🔁 LOAD THE ENVIRONMENT-SPECIFIC BACKEND URL
        webView.loadUrl(BuildConfig.API_BASE_URL)

        // ✅ MODERN BACK GESTURE HANDLING
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    // Disable this callback and let the system handle back (e.g., exit app)
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private inner class AreYouAliveWebViewClient : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)

            if (request?.isForMainFrame == false) {
                return
            }

            view?.loadData(
                """
                <html>
                    <body style='text-align:center; padding: 20px;'>
                        <h2>Connection Error</h2>
                        <p>Unable to reach the app. Please check your internet connection.</p>
                    </body>
                </html>
                """.trimIndent(),
                "text/html",
                "utf-8"
            )
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return false
        }
    }
}
