package learn.example.pile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import learn.example.joke.R;
import learn.example.pile.net.StringRequest;

/**
 * Created on 2016/5/7.
 */
public class NewsActivity extends AppCompatActivity  {

    public static final String LINK_KEY="link";
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mWebView= (WebView) findViewById(R.id.news_webview);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        Intent intent=getIntent();
        if(intent!=null)
        {
            String url=intent.getStringExtra(LINK_KEY);
            if(url!=null)
            mWebView.loadUrl(url);
        }

    }
    public class RequestThread extends Thread
    {
        private String url;
        RequestThread(String url)
       {
         this.url=url;
      }
        @Override
        public void run() {
            String html= StringRequest.request(url,null);
        }
    }
}