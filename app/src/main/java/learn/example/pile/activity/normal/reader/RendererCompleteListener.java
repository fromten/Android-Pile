package learn.example.pile.activity.normal.reader;

public interface RendererCompleteListener{
    /**
     * 当{@link ContentRenderer}请求显示或更新数据时调用
     * @param content Html
     * @param useLocalCss 是否使用本地Css
     */
    void onRenderCompleted(String content,boolean useLocalCss);
}