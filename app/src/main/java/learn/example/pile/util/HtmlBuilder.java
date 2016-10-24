package learn.example.pile.util;


/**
 * Created on 2016/7/27.
 */
public class HtmlBuilder {

    private StringBuilder mStringBuilder=new StringBuilder();



    public HtmlBuilder startHtml(String attrs)
    {
        mStringBuilder.append("<!DOCTYPE html>");
        mStringBuilder.append("<html");
        if (attrs!=null)
        {
            mStringBuilder.append(' ');
            mStringBuilder.append(attrs);
        }
        mStringBuilder.append(">");
        return this;
    }

    public HtmlBuilder startHead(String attrs)
    {
        mStringBuilder.append("<head");
        if (attrs!=null)
        {
            mStringBuilder.append(' ');
            mStringBuilder.append(attrs);
        }
        mStringBuilder.append(">");
        return this;
    }

    public HtmlBuilder startBody(String attrs)
    {
        mStringBuilder.append("<body");
        if (attrs!=null)
        {
            mStringBuilder.append(' ');
            mStringBuilder.append(attrs);
        }
        mStringBuilder.append(">");
        return this;
    }

    public HtmlBuilder endHead()
    {
        mStringBuilder.append("</head>");
        return this;
    }

    public HtmlBuilder endBody()
    {
        mStringBuilder.append("</body>");
        return this;
    }

    public void endHtml()
    {
        mStringBuilder.append("<html>");
    }

    public HtmlBuilder append(String str)
    {
        mStringBuilder.append(str);
        return this;
    }

    public HtmlBuilder appendTag(String name,String attrs,String content)
    {
        mStringBuilder.append(tag(name,attrs,content));
        return this;
    }

    public HtmlBuilder appendCss(String css)
    {
        mStringBuilder.append(tag("style",attr("type","text/css"),css));
        return this;
    }

    public HtmlBuilder appendCssLink(String href)
    {
        String attrs=attrs("rel","stylesheet","type","text/css","href",href);
        mStringBuilder.append(tag("link",attrs,null));
        return this;
    }



    public  HtmlBuilder appendJS(String js)
    {
        mStringBuilder.append(tag("script",attr("type","text/javascript"),js));
        return this;
    }

    public  HtmlBuilder appendJsLink(String link)
    {
        mStringBuilder.append(tag("script",attrs("type","text/javascript","src",link),null));
        return this;
    }


    @Override
    public String toString() {
        return mStringBuilder.toString();
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
