package learn.example.pile.util;

/**
 * Created on 2016/7/22.
 */
public class TextUtil {

    public static String firstSentence(String text)
    {
        String[] str=sentenceSub(text);
        if (str.length>=0)
        {
            return str[0]+"。";
        }
        return null;
    }


    public static String[] sentenceSub(String text)
    {
        if (text==null)
            throw new NullPointerException("null object");
        return text.split("\\.|\\。");
    }

}
