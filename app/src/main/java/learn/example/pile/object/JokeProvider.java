package learn.example.pile.object;

import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/8/12.
 */
public class JokeProvider {

    public final static String TUIJIAN_URL="http://ic.snssdk.com/neihan/stream/mix/v1/?";
    public final static String COMMENT_URL="http://isub.snssdk.com/2/data/v2/get_essay_comments/?";
    public final static String APP_PARAMS="mpic=1&essence=1&content_type=-101&message_cursor=-1&iid=5143808772&device_id=24984230527&ac=wifi&channel=NHSQH5AN&aid=7&app_name=joke_essay&version_code=431&device_platform=android&ssmix=a&device_type=HTC+One+-+4.4.4+-+API+19+-+1080x1920&os_api=19&os_version=4.4.4&openudid=b3d4f3c9f28b9cfa&manifest_version_code=431";

    public static String createHotUrl(int count, int screenWidth)
    {
        return TUIJIAN_URL+APP_PARAMS+"&count="+count+"&screen_width="+screenWidth+"&min_time="+ TimeUtil.getCurrentTime()/1000;
    }

    public static String createCommentUrl(int start,int length,String groupId)
    {
        return COMMENT_URL+APP_PARAMS+"&offset="+start+"&count="+length+"&group_id="+groupId;
    }
}
