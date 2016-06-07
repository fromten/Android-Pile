package learn.example.pile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import learn.example.joke.R;

/**
 * Created on 2016/5/7.
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    public static final String KEY_URL = "KEY_URL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        Intent intent = getIntent();
        String url;
        if (intent != null&&(url=intent.getStringExtra(KEY_URL))!=null) {
                mWebView.loadUrl(url);
        }
    }

    public void initView()
    {
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        mWebView = (WebView) findViewById(R.id.news_webview);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               if (newProgress>=100)
               {
                   mProgressBar.setVisibility(View.GONE);
               }else
               {
                   if (mProgressBar.getVisibility()!=View.VISIBLE)
                   {
                       mProgressBar.setVisibility(View.VISIBLE);
                   }
                   mProgressBar.setProgress(newProgress);
               }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }
}
