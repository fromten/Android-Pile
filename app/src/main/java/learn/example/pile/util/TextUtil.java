package learn.example.pile.util;

/**
 * Created on 2016/7/22.
 */
public class TextUtil {

    public static String firstSentence(String text)
    {
        String[] str=sentenceSub(text);
        if (str!=null&&str.length>0)
        {
            String fs=str[0];
            return fs.length()>0?fs+=".":fs;
        }
        return null;
    }


    public static String[] sentenceSub(String text)
    {
        return text==null?null:text.split("\\.|\\。");
    }

    public static String checkString(String string,String defValue)
    {
       return string==null?defValue:string;
    }
}
