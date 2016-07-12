package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import learn.example.joke.R;
import learn.example.pile.ReaderActivity;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;

/**
 * Created on 2016/7/12.
 */
public class ReadContentFragment extends Fragment implements IService.Callback<ZhihuNewsContent> {

    private WebView mWebView;
    private ReaderActivity.HtmlHelper mHtmlHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_read_content,container,false);
        mWebView= (WebView) view.findViewById(R.id.web_view);
        initWebView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
    public void onSuccess(ZhihuNewsContent data) {
        if (mHtmlHelper==null)
        {
            mHtmlHelper=new ReaderActivity.HtmlHelper();
        }
        String html=mHtmlHelper.generateHtml(data.getBody(),data.getCss(),data.getJs());
        mWebView.loadDataWithBaseURL(null,html,"text/html","UTF-8",null);
    }

    @Override
    public void onFailure(String message) {

    }
}
