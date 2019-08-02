package course.examples.ui.webview

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : Activity() {

    companion object {
        private const val TAG = "HelloWebViewClient"
    }

    private lateinit var mWebView: WebView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        mWebView = findViewById(R.id.webview)

        // Set a kind of listener on the WebView so the WebView can intercept
        // URL loading requests if it wants to
        mWebView.webViewClient = HelloWebViewClient()

        // Add Zoom controls to WebView
        mWebView.settings.builtInZoomControls = true

        mWebView.loadUrl("https://www.cs.umd.edu/~aporter/Tmp/bee.html")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private inner class HelloWebViewClient : WebViewClient() {

        // Give application a chance to catch additional URL loading requests
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.i(TAG, "About to load:$url")
            view.loadUrl(url)
            return true
        }
    }

}