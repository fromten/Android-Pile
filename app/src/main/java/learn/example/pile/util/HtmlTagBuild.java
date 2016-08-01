package learn.example.pile.util;

import java.util.Locale;

/**
 * Created on 2016/7/27.
 */
public class HtmlTagBuild {

    private static final String TAG="<%s %s>%s </%s>";
    private static final String ATTR="%s='%s'";

    public static String styleTag(String css)
    {
        return tag("style",attr("type","text/css"),css);
    }

    public static String headTag(String wrapContent)
    {
        return tag("head",null,wrapContent);
    }

    public static String jsTag(String js)
    {
        return tag("script",attr("type","text/javascript"),js);
    }

    public static String cssLinkTag(String href)
    {
        String attr=attr("rel","stylesheet")+attr("type","text/css")+ attr("href",href);
        return tag("link",attr,null);
    }


    public static String imageTag(int width,int height,String src)
    {
        String srcAttr=attr("src",src);
        if (width==0&&height==0)
        {
            return tag("image",srcAttr,null);
        }
        String widthAttr=attr("width", String.valueOf(width));
        String heightAttr=attr("height", String.valueOf(height));
        return tag("image",widthAttr+heightAttr+srcAttr,null);
    }

    /**
     * 生成Xml标签
     * @param tagName 标签名字
     * @param attrs 标签属性
     * @param wrapContent 标签内容
     * @return 完整的XML标签
     */
    public static String tag(String tagName,String attrs,String wrapContent)
    {

        String wrap=TextUtil.checkString(wrapContent,"");
        return String.format(Locale.CHINA,TAG,tagName,attrs,wrap,tagName);
    }

    /**
     * 生成属性参数,如 attr("width","50") ,return width='50'
     * @param attrName 属性名字
     * @param value 属性值
     * @return 完整的属性
     */
    public static String attr(String attrName,String value)
    {
        return String.format(Locale.CHINA,ATTR,attrName,value);
    }


}
