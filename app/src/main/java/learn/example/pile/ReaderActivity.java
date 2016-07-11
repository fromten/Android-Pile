package learn.example.pile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import learn.example.joke.R;
import learn.example.net.Service;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;

/**
 * Created on 2016/7/10.
 */
public class ReaderActivity extends BaseActivity implements IService.Callback<ZhihuNewsContent> {
    private static final String TAG = "ReaderActivity";


    public static final String KEY_CONTENT_ID="KEY_CONTENT_ID";
    private ZhihuContentService mService;
    private HtmlHelper mHtmlHelper;


    private WebView mWebView;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private TextView mImaSource;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        setView();
        initWebView();

        mService=new ZhihuContentService();
        int id=getIntent().getIntExtra(KEY_CONTENT_ID,0);
        if (id!=0){
            mService.getContent(id,this);
        }

    }

    private void setView()
    {
        mWebView= (WebView) findViewById(R.id.web_view);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        mImageView= (ImageView) findViewById(R.id.image);
        mImaSource= (TextView) findViewById(R.id.source);
    }


    private void initWebView(){
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
       // mWebView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onSuccess(ZhihuNewsContent data) {

        //Glide.with(this).load(data.getImage()).into(mImageView);

         if (mHtmlHelper==null)
         {
             mHtmlHelper=new HtmlHelper();
         }
        String html=mHtmlHelper.generateHtml(data.getBody(),data.getCss(),data.getJs());
        mWebView.loadDataWithBaseURL(null,html,"text/html","UTF-8",null);
        stopProgressBar();
    }



    @Override
    public void onFailure(String msg) {
         stopProgressBar();
    }

    private void stopProgressBar()
    {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public static class HtmlHelper{
        private StringBuilder mBuilder=new StringBuilder();


        public String generateHtml(String body,List<String> css, List<String> js)
        {
            mBuilder.append("<html lang='zh-CN'>");
            mBuilder.append(toHeadTag(css,js));
            mBuilder.append("<body>");
            mBuilder.append(body);
            mBuilder.append("</body>");
            mBuilder.append("</html>");
            return mBuilder.toString();
        }


        public String toHeadTag(List<String> css, List<String> js)
        {
            StringBuilder builder=new StringBuilder();
            builder.append("<head>");
            for (String str:css)
            {
                builder.append(toLinkTag(str));
            }
            for (String str:js)
            {
                builder.append(toScriptTag(str));
            }
            builder.append("</head>");
            return builder.toString();
        }

        public String toLinkTag(String css)
        {
            return "<link rel='stylesheet' type='text/css' href='"+css+"'>";
        }

        public String toScriptTag(String js)
        {
            return "<script src='"+js+"'>"+"</script>";
        }
    }
}
