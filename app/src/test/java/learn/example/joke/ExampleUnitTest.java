package learn.example.joke;


import android.content.res.XmlResourceParser;
import android.text.Html;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    private String url="http://weibo.com/p/23044487ee734f65d401024264636dc43ce531?retcode=6102";
    private String url2="http://v.youku.com/v_show/id_XMTU3MjUwMDkxMg==.html";
    @Test
    public void addition_isCorrect() {
        String res=request(url,null);
        System.out.println(res);
        try {
            XmlPullParser parser= XmlPullParserFactory.newInstance().newPullParser();
            int evnettype=parser.getEventType();
            while (evnettype!=XmlPullParser.END_DOCUMENT)
            {
                if(evnettype==XmlPullParser.START_TAG)
                {
                    String name=parser.getName();
                    if(name.equals("embed")){
                        System.out.println(name);

                        System.out.println(parser.getAttributeValue(4));
                    }
                }
                evnettype = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void getException() throws Exception {
         throw new Exception();
    }

        public static  String request(String httpUrl, String httpArg) {
            BufferedReader reader = null;
            String result = null;
            StringBuffer sbf = new StringBuffer();
           // httpUrl = httpUrl; // + "?" + httpArg;

            try {
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
                connection.addRequestProperty("Cookie","SINAGLOBAL=14.18.29.138_1462969985.476829; UOR=www.baidu.com,news.sina.com.cn,; vjuids=-3004dc83c.154b97d2db3.0.c5179cea3936f; lxlrttp=1463382523; U_TRS1=000000ab.d4c1b95.573c6ce7.8e0867b7; Apache=14.18.25.171_1464099569.329530; SUS=SID-2342760657-1464103306-GZ-szfl4-68738071e9cbbdb9491be1720e251007; SUE=es%3D1e2e6fb721e7524c097078cf4885e621%26ev%3Dv1%26es2%3Dda17443bdd8182b16b2e6b4b09fa29cb%26rs0%3DvcE7ioTFS%252B%252BSTR%252B0qRIeZ9emv1YgyTJZQZ%252B8wTuk2saUWOfZjL3zN8rV3tv%252FyMkrODuDuj7cBKPAaPzQAPuHf2wt4rv4i48iJblqPboNHJZAeL4tj8zacVitnadbxUEiibgG2MHP4uS6YgJbPP1dYffqVnch%252Feuik1cahnYFoOU%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1464103306%26et%3D1464189706%26d%3D40c3%26i%3D1007%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26lt%3D2%26uid%3D2342760657%26user%3D15902049983%26ag%3D1%26name%3D15902049983%26nick%3D%25E7%2594%25A8%25E6%2588%25B72342760657%26sex%3D%26ps%3D0%26email%3D%26dob%3D%26ln%3D%26os%3D%26fmp%3D%26lcp%3D2016-04-23%252013%253A31%253A01; SUB=_2A256QAHaDeRxGeRN71AW9i7KzjuIHXVZNHQSrDV_PUNbu9BeLW_CkW9LHesNETJ6yoXznCBL8lqObSf_tjkGzg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9Whrpek99W0Cfp6dEWOvFyqO5JpX5KzhUgL.Foz0ShzNSo5cSKM2dJLoI7OK7OVreoeXeoMcehqfS7tt; ALF=1495639306; rotatecount=1; ULV=1464104756568:7:7:2:14.18.25.171_1464099569.329530:1463920056415; U_TRS2=000000ab.a7527bcd.57447734.d9c8caa7; vjlast=1463400869.1464104757.11; SessionID=invdbb22qelqcou02akid15f16");
                connection.setRequestMethod("GET");
                // 填入apikey到HTTP header
                connection.connect();
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                    sbf.append("\r\n");
                }
                reader.close();
                result = sbf.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

}