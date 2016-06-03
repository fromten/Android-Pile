package learn.example.pile.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created on 2016/6/1.
 */
public class StringRequest {
    public static  String request (String url)
    {
        try {
            URL u=new URL(url);
            HttpURLConnection connection= (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder=new StringBuilder();
            String str;
            while ((str=in.readLine())!=null)
            {
                builder.append(str);
            }
            return builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
