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

    @JavascriptInterface
    public void passHtml(String outerHtml)
    {

    }
}
