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
    public static  String request (String url){
        BufferedReader in = null;
        StringBuilder builder=new StringBuilder();
        try {
            URL u=new URL(url);
            HttpURLConnection connection= (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10*1000);
            connection.connect();
            in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str;
            while ((str=in.readLine())!=null)
            {
                builder.append(str);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException|NullPointerException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
