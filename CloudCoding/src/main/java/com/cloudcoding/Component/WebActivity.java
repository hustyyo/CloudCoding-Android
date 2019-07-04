package com.cloudcoding.Component;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cloudcoding.Component.BaseActivity;
import com.natasa.progresspercent.LineProgress;
import com.cloudcoding.R;

public class WebActivity extends BaseActivity {

    WebView webView;
    LineProgress line_progress_bar;
    TextView nav_forward;
    TextView nav_back;
    TextView nav_refresh;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_activity);

        String filename = getIntent().getStringExtra("file");
        url = getIntent().getStringExtra("url");

        initTopBarWithParam(R.layout.top_bar_web,filename);

        nav_forward = (TextView) findViewInTopBar(R.id.nav_forward);
        nav_back = (TextView)findViewInTopBar(R.id.nav_back);
        nav_refresh = (TextView)findViewInTopBar(R.id.nav_refresh);
        nav_forward.setEnabled(false);
        nav_back.setEnabled(false);
        nav_refresh.setEnabled(true);
        nav_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goForward();
            }
        });
        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
            }
        });
        nav_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_progress_bar.setProgress(0);
                line_progress_bar.setVisibility(View.VISIBLE);
                webView.reload();
            }
        });

        initWebView();
    }

    void updateButton() {
        if(webView.canGoBack()){
            nav_back.setEnabled(true);
        }
        else {
            nav_back.setEnabled(false);
        }

        if(webView.canGoForward()){
            nav_forward.setEnabled(true);
        }
        else {
            nav_forward.setEnabled(false);
        }
    }

    void initWebView() {

        String  url = getIntent().getStringExtra("url");

        line_progress_bar = (LineProgress) findViewById(R.id.line_progress_bar);
        line_progress_bar.setVisibility(View.VISIBLE);

        webView = (WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                line_progress_bar.setProgress(progress);
                if(progress >= 100){
                    line_progress_bar.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){

                updateButton();

                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                updateButton();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                line_progress_bar.setVisibility(View.GONE);
                updateButton();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                line_progress_bar.setVisibility(View.GONE);
                updateButton();
                showDialog(getString(R.string.com_cloudcoding_error),getString(R.string.com_cloudcoding_loading_web_failed));
            }
        });
        line_progress_bar.setProgress(0);
        line_progress_bar.setVisibility(View.VISIBLE);
        webView.loadUrl(url);
    }
}
