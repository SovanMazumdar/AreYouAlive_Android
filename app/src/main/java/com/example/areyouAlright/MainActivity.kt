package com.example.areyouAlright

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
    private val TAG = "AreYouAlright"

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
        val apiUrl = BuildConfig.API_BASE_URL
        Log.d(TAG, "Loading API URL: $apiUrl")
        Log.d(TAG, "App Name: ${BuildConfig.APP_NAME}")
        Log.d(TAG, "Build Type: ${BuildConfig.BUILD_TYPE}")
        
        webView.loadUrl(apiUrl)

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
        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.d(TAG, "Page started loading: $url")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d(TAG, "Page finished loading: $url")
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)

            // Log the error for debugging
            Log.e(TAG, "WebView Error - URL: ${request?.url}, Error Code: ${error?.errorCode}, Description: ${error?.description}")

            if (request?.isForMainFrame == false) {
                Log.d(TAG, "Ignoring sub-frame error")
                return
            }

            view?.loadData(
                """
                <html>
                    <head>
                        <style>
                            body {
                                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                margin: 0;
                                padding: 0;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                height: 100vh;
                            }
                            .error-container {
                                background: white;
                                border-radius: 12px;
                                padding: 40px;
                                text-align: center;
                                box-shadow: 0 10px 40px rgba(0,0,0,0.1);
                                max-width: 400px;
                            }
                            h2 {
                                color: #d32f2f;
                                margin: 0 0 16px 0;
                                font-size: 24px;
                            }
                            p {
                                color: #666;
                                margin: 8px 0;
                                line-height: 1.6;
                                font-size: 14px;
                            }
                            .debug-info {
                                background: #f5f5f5;
                                padding: 16px;
                                border-radius: 8px;
                                margin-top: 20px;
                                text-align: left;
                                font-family: monospace;
                                font-size: 12px;
                                color: #333;
                            }
                            .retry-hint {
                                margin-top: 20px;
                                padding-top: 20px;
                                border-top: 1px solid #eee;
                            }
                            .retry-hint strong {
                                color: #333;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="error-container">
                            <h2>⚠️ Connection Error</h2>
                            <p>Unable to reach the app. Please check your internet connection.</p>
                            <div class="debug-info">
                                <strong>Error Code:</strong> ${error?.errorCode}<br>
                                <strong>Error Description:</strong> ${error?.description}<br>
                                <strong>URL:</strong> ${request?.url}
                            </div>
                            <div class="retry-hint">
                                <strong>Troubleshooting:</strong><br>
                                1. Check your WiFi/Mobile connection<br>
                                2. Verify Flask backend is running<br>
                                3. Check firewall settings<br>
                                4. Try refreshing (back and forward)
                            </div>
                        </div>
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
            Log.d(TAG, "URL Loading: ${request?.url}")
            return false
        }
    }
}
