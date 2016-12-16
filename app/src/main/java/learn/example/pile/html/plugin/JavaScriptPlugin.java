package learn.example.pile.html.plugin;

/**
 * Created on 2016/9/12.
 */
//插入javascript 代码进入Html
public interface JavaScriptPlugin {
    /**
     * 客户端与JS进行交互,WebView使用返回值作为全局对象名字.
     * 应该返回一个常量字符串,使用{@code Class.getName()} 是不可靠的,因为proguard
     * @see android.webkit.WebView#addJavascriptInterface(Object, String)
     * @return Html全局对象的名字
     */
    String getName();

    /**
     * @return javaScript代码
     */
    String getJavaScript();
}
