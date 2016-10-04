package learn.example.pile.html;

/**
 * Created on 2016/9/12.
 */
//插入javascript 代码进入Html
public interface JavaScriptInserter {
    /**
     * 客户端与JS进行交互,返回值会被WebView使用作为全局对象名字
     * @see android.webkit.WebView#addJavascriptInterface(Object, String)
     * @return Html全局对象的名字
     */
    String getName();

    /**
     * @return javaScript代码
     */
    String getJavaScript();
}
