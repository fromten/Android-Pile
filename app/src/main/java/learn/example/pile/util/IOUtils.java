package learn.example.pile.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created on 2016/8/28.
 */
public class IOUtils {

    public static String readString(InputStream in) throws IOException {
        if (in==null)
        {
            return null;
        }
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        StringBuilder sb=new StringBuilder();
        String readStr=null;
        try {
            while ((readStr=reader.readLine())!=null)
            {
                sb.append(readStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            reader.close();
        }
        return sb.toString();
    }
}
