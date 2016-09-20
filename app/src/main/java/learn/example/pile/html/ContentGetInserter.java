package learn.example.pile.html;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import learn.example.pile.database.model.NewsArticle;
import learn.example.pile.util.ActiveAndroidHelper;

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
        return "<script type='text/javascript'>\n" +
                "var page=document.documentElement.outerHTML;\n" +
                "ContentGetInserter.passHtml(String(page));\n" +
                "</script>";
    }

    @JavascriptInterface
    public void passHtml(String outerHtml)
    {

    }
}
