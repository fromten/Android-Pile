package learn.example.pile.html;

import android.util.Log;

import java.util.List;

import learn.example.pile.util.HtmlTagBuild;

/**
 * Created on 2016/7/27.
 */
public class ZhihuHtml {

    private String body;
    private String[] css;
    private String[] js;

    public ZhihuHtml(String body, String[] css, String[] js) {
        this.body = body;
        this.css = css;
        this.js = js;
    }

    /**
     * 生成完整的Html页面
     * @return Html
     */
    public String generateHtml()
    {
        StringBuilder builder=new StringBuilder();
        builder.append("<html lang='zh-CN'>");
        builder.append(toHeadTag(css,js));
        builder.append("<body>");
        builder.append(body);
        builder.append(HtmlTagBuild.jsTag(getMyJs()));//插入一段代码
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }


    public String toHeadTag(String[] css, String[] js)
    {
        StringBuilder builder=new StringBuilder();
        builder.append("<head>");
        for (String str:css)
        {
            builder.append(HtmlTagBuild.linkTag(str));
        }
        builder.append(getMyCss());
        for (String str:js)
        {
            builder.append(HtmlTagBuild.jsTag(str));
        }
        builder.append("</head>");
        return builder.toString();
    }

    /**
     * 知乎默认css会添加200px的头部图片高度,使用得到css覆盖原本的css属性
     * @return
     */
    public String getMyCss()
    {
        return HtmlTagBuild.styleTag(".headline .img-place-holder{" +
                " height: 0px;}");
    }

    /**
     * 替换知乎页面的评论点击
     * @return javascript text
     */
    public String getMyJs()
    {
       return  "var aTag=document.getElementsByTagName('a');\n" +
                "aTag=aTag[aTag.length-1];\n" +
                "aTag.removeAttribute('href');" +
                "aTag.setAttribute('onClick',\"showAndroidComment()\");\n" +
                "\n" +
                "function showAndroidComment()\n" +
                "{\n" +
                "  ReaderActivity.showCommentFragment();\n" +
                "}\n";
    }

}
