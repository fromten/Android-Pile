package learn.example.pile.object;

import android.net.UrlQuerySanitizer;

import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/7/21.
 */
public class MiaoPai {

    public static final String HOT_URL="http://c.miaopai.com/m/ascent_cache.json";

    //失败---------------------------------------------------

    public static  String getHotUrl()
    {
        String url=HOT_URL;
        url+="?timestamp="+ TimeUtil.getTimeStamp();
        url+="&unique_id=6f08b087-9338-381e-81e2-34fe165c2623&os=android&token=&vend=miaopai&udid=6f08b087-9338-381e-81e2-34fe165c2623&uuid=6f08b087-9338-381e-81e2-34fe165c2623&channel=&deviceId=6f08b087-9338-381e-81e2-34fe165c2623&version=6.5.6";
        return url;
    }
}
