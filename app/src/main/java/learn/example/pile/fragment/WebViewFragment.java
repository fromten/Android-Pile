package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import learn.example.pile.R;
import learn.example.pile.util.HtmlBuilder;

/**
 * Created on 2016/10/26.
 * 兼容版本
 * @see android.webkit.WebViewFragment
 */

public class WebViewFragment extends Fragment {
    private WebView mWebView;
    private boolean mIsWebViewAvailable;

    /**
     * Called to instantiate the view. Creates and returns the WebView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = new WebView(getActivity());
        mIsWebViewAvailable = true;
        return mWebView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebSettings();
    }

    protected void initWebSettings()
    {
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey_light, null));
        WebSettings settings=mWebView.getSettings();
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadsImagesAutomatically(true);
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
        String attrs= HtmlBuilder.attrs("rel","stylesheet","type","text/css","href","app.css");
        String link= HtmlBuilder.tag("link",attrs,null);
        String completeHtml=html.replaceFirst("</head>",link+"</head>");
        mWebView.loadDataWithBaseURL("file:///android_asset/", completeHtml, "text/html", "UTF-8", null);
    }


    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }


    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView() {
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    /**
     * Gets the WebView.
     */
    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }
}
