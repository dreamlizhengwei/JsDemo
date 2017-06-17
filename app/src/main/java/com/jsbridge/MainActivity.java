package com.jsbridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * web和Android交互方式总结
 */
public class MainActivity extends AppCompatActivity {

    private WebView webview;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView) findViewById(R.id.webview);
        tv = (TextView) findViewById(R.id.params);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用JavaScript
        //允许弹出框
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webview.loadUrl("file:///android_asset/js.html");

        webview.addJavascriptInterface(new JsToJava(), "stub");

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                tv.setText("prompt方式，参数：" + message);
                // 调用一下cancel或者confirm都行
                result.cancel();
                return true;
            }
        });
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                tv.setText("url方式交互，url是：" + url);
                return true;
            }
        });
    }

    private class JsToJava {
        // 高版本需要加这个注解才能生效
        @JavascriptInterface
        public void jsMethod(final String paramFromJS) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText("传统方式js调用java，参数：" + paramFromJS);
                }
            });
        }
    }
}
