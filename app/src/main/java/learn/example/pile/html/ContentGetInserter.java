package learn.example.pile.html;

import android.webkit.JavascriptInterface;

/**
 * Created on 2016/9/20.
 */
public class ContentGetInserter implements JavaScriptInserter {

    @Override
    public String getName() {
        return "ContentGetInserter";
    }

    @Override
    public String getJavaScript() {
        return "var page=document.documentElement.outerHTML;\n"+
                "ContentGetInserter.passHtml(String(page));";
    }

    /**
     * WebView回调此方法
     * @param outerHtml 网页的内容文本
     */
    @JavascriptInterface
    public void passHtml(String outerHtml)
    {

    }
}
