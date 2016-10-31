package learn.example.pile.util;

/**
 * Created on 2016/7/22.
 */
public class TextUtil {

    public static String splitFirstSentence(String text)
    {
        String[] str=sentenceSub(text);
        if (str!=null&&str.length>0)
        {
            String fs=str[0];
            return fs.length()>0?fs+="。":fs;
        }
        return null;
    }


    /**
     * 分离字符串的句子,按中文句号('。')分离
     * @param text 要分离的字符串
     * @return 一个包含句子的数组
     */
    public static String[] sentenceSub(String text)
    {
        return text==null?null:text.split("\\。");
    }


}
