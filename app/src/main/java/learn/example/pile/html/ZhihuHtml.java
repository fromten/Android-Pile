package learn.example.pile.html;

import learn.example.pile.util.HtmlTagBuild;

/**
 * Created on 2016/7/27.
 */
public class ZhihuHtml {

    private String body;
    private String[] cssLink;
    private String[] jsLink;
    private String js;
    private String css;
    public ZhihuHtml(String body, String[] cssLink, String[] jsLink) {
        this.body = body;
        this.cssLink = cssLink;
        this.jsLink = jsLink;
    }

    /**
     * 生成完整的Html页面
     * @return Html
     */
    public String generateHtml()
    {
        StringBuilder builder=new StringBuilder();
        builder.append("<html lang='zh-CN'>");
        builder.append(toHeadTag(cssLink,jsLink));
        if (css!=null)
        {
           builder.append(HtmlTagBuild.styleTag(css));
        }
        builder.append("<body>");
        builder.append(body);
        builder.append("</body>");

        if (js!=null)
        {
            builder.append(HtmlTagBuild.jsTag(js));
        }

        builder.append("</html>");
        return builder.toString();
    }


    public String toHeadTag(String[] css, String[] js)
    {
        StringBuilder builder=new StringBuilder();
        builder.append("<head>");
        for (String str:css)
        {
            builder.append(HtmlTagBuild.cssLinkTag(str));
        }
        builder.append(getMyCss());
        for (String str:js)
        {
            builder.append(HtmlTagBuild.tag("script",HtmlTagBuild.attr("src",str),null));
        }
        builder.append("</head>");
        return builder.toString();
    }

    /**
     * 知乎默认css会添加200px的头部图片高度,覆盖原本的css属性
     * @return
     */
    public String getMyCss()
    {
        return HtmlTagBuild.styleTag(".headline .img-place-holder{" +
                " height: 0px;}");
    }

    /**
     * 设置额外的Js 脚本代码
     * @param js javaScript
     */
    public void setJs(String js) {
        this.js = js;
    }

    /**
     * 设置额外的Css 样式
     * @param css css内容
     */
    public void setCss(String css) {
        this.css = css;
    }
}
