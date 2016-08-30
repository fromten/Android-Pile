package learn.example.pile.util;


/**
 * Created on 2016/7/27.
 */
public class HtmlTagBuild {



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
     * @param wrapContent 标签文本
     * @return 完整的XML标签
     */
    public static String tag(String tagName,String attrs,String wrapContent)
    {
        String tag;
        if (attrs==null)
        {
            tag="<"+tagName+">";
        }else {
            tag="<"+tagName+" "+attrs+">";
        }
        if (wrapContent!=null)
        {
            tag+=wrapContent;
        }
        return tag+"</"+tagName+">";
    }


    /**
     * 生成属性,默认对应 属性-值
     * @param args[偶数] 属性名字
     * @param args[奇数] 属性值
     * @return 完整的属性
     * @throws IllegalArgumentException 如果args数量不等于偶数,无法匹配
     */
    public static String attrs(String... args)
    {
        if (args.length%2!=0)
        {
            throw new IllegalArgumentException("Expect valid count but count of the args is "+ args.length);
        }
        int count=args.length;
        String result="";
        for (int i=0;i<count;i+=2)
        {
            result+=attr(args[i],args[i+1]);
        }
        return result;
    }


    /**
     * 生成属性,默认对应 属性-值
     * @param attrName 属性名字
     * @param value 属性值
     * @return 完整的属性
     */
    public static String attr(String attrName,String value)
    {
        return String.format("%s='%s' ",attrName,value);
    }


}
