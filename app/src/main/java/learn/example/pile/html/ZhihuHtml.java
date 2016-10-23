package learn.example.pile.html;


import learn.example.pile.util.HtmlBuilder;

/**
 * Created on 2016/7/27.
 */
public class ZhihuHtml implements Html{

    private String body;
    private String[] cssLinks;
    private String[] jsLinks;
    private String extraJs;
    private String extraCss;
    public ZhihuHtml(String body, String[] cssLinks, String[] jsLinks) {
        this.body = body;
        this.cssLinks = cssLinks;
        this.jsLinks = jsLinks;
    }




    public void fillHead(HtmlBuilder builder)
    {
        builder.startHead(null);

        for (String href:cssLinks)
        {
            builder.appendCssLink(href);
        }

        //知乎默认css会添加200px的头部图片高度,覆盖原本的css属性
        builder.appendCss(".headline .img-place-holder{" +
                             " height: 0px;}");

        builder.appendCss(extraCss);

        for (String src:jsLinks)
        {
            builder.appendJsLink(src);
        }

       builder.appendJS(extraJs);

        builder.endHead();
    }



    /**
     * 设置额外的Js 脚本代码
     * @param js javaScript
     */
    public void setExtraJs(String js) {
        this.extraJs = js;
    }

    /**
     * 设置额外的Css 样式
     * @param css css内容
     */
    public void setExtraCss(String css) {
        this.extraCss = css;
    }

    @Override
    public String getHtml() {
        HtmlBuilder htmlBuilder=new HtmlBuilder();
        htmlBuilder.startHtml(HtmlBuilder.attr("lang","zh-CN"));
        fillHead(htmlBuilder);
        htmlBuilder.startBody(null)
                .append(body)
                .endBody().endHtml(); ;

        return htmlBuilder.toString();
    }
}
