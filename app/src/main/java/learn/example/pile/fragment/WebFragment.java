package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import learn.example.pile.R;
import learn.example.pile.util.HtmlBuilder;

/**
 * Created on 2016/7/27.
 */
public class WebFragment extends Fragment {

    private WebView mWebView;
    private FrameLayout mRootLayout;
    private ProgressBar mProgressBar;


    private boolean supportProgressBar;
    private int progressBarStyle;
    private boolean indeterminateProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        mRootLayout= (FrameLayout) view.findViewById(R.id.root);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebView();
        if (supportProgressBar)
        {
            addProgressBar();
        }
    }

    private void initWebView() {
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey_light, null));
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setVerticalScrollBarEnabled(true);

    }


    public void loadLocalData(String html) {
        mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }


    /**
     * 加载Html进入webview,不同loadLocalData()这会应用App基础的css样式
     * @param html
     * @see assets folder  app.css
     */
    public void loadLocalDataWithDefCss(String html)
    {
        String attrs=HtmlBuilder.attrs("rel","stylesheet","type","text/css","href","app.css");
        String link= HtmlBuilder.tag("link",attrs,null);
        String insertCss=html.replaceFirst("</head>",link+"</head>");
        mWebView.loadDataWithBaseURL("file:///android_asset/", insertCss, "text/html", "UTF-8", null);
    }



    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }



    /**
     * 请求添加进度栏,不确定进度栏将布局在中间. 确定进度栏布局在顶部
     * @param indeterminate 是否为不确定进度栏
     * @param style 进度栏样式,如果为0,设置默认主题进度栏样式
     */
    public void requestProgressBar(boolean indeterminate,int style)
    {
        supportProgressBar=true;
        progressBarStyle=style;
        indeterminateProgressBar=indeterminate;
    }


    private void addProgressBar()
    {
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (indeterminateProgressBar)
        {
            params.gravity=Gravity.CENTER;
        }else {
            params.gravity= Gravity.TOP;
        }
        int defStyle=indeterminateProgressBar?android.R.attr.progressBarStyleLarge:android.R.attr.progressBarStyleHorizontal;
        int style=progressBarStyle==0?defStyle:progressBarStyle;
        mProgressBar=new ProgressBar(getContext(),null,style);
        mRootLayout.addView(mProgressBar,params);
    }

    public void setProgress(int progress)
    {
        if (mProgressBar!=null)
        {
            mProgressBar.setProgress(progress);
        }
    }

    public ProgressBar getProgressBar()
    {

        return mProgressBar;
    }


    public WebView getWebView()
    {
        return mWebView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onStart();
        mWebView.onResume();
    }

    @Override
    public void onDestroy() {
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView=null;
        super.onDestroy();
    }

}
