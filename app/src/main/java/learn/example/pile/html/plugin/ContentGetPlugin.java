package learn.example.pile.html.plugin;

import android.webkit.JavascriptInterface;

/**
 * Created on 2016/9/20.
 */
public class ContentGetPlugin implements JavaScriptPlugin {

    @Override
    public String getName() {
        return "ContentGetPlugin";
    }

    @Override
    public String getJavaScript() {
        return "var page=document.documentElement.outerHTML;\n"+
                getName()+".passHtml(String(page));";
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
