package learn.example.pile.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import learn.example.pile.PhotoActivity;
import learn.example.pile.VideoActivity;
import learn.example.pile.WebViewActivity;

/**
 * Created on 2016/6/23.
 */
public class ActivityLauncher {

    /**
     * 启动自己应用程序内部Web浏览器Activity,使用左边进入动画
     * @param context
     * @param url
     */
    public static void startInternalWebActivity(@NonNull Context context,@NonNull String url)
    {
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.setData(Uri.parse(url));
        ActivityCompat.startActivity((Activity) context,intent,slideAnimation(context));
    }


    /**
     * 启动视频播放器Activity,使用左边进入动画,
     * @param context
     * @param url
     */
    public static void startVideoActivity(@NonNull Context context, @NonNull String url)
    {
        Intent intent=new Intent(context, VideoActivity.class);
        intent.putExtra(VideoActivity.KEY_VIDEO_URL,url);
        ActivityCompat.startActivity((Activity) context,intent,slideAnimation(context));
    }


    /**
     * 启动图片查看Activity,使用左边进入动画
     * @param context
     * @param url
     */

    public static void startPhotoActivity(@NonNull Context context, @NonNull String url)
    {
        Intent intent=new Intent(context, PhotoActivity.class);
        intent.putExtra(PhotoActivity.KEY_IMG_URL,url);
        ActivityCompat.startActivity((Activity) context,intent,slideAnimation(context));
    }



    /**
     *
     * @param context
     * @return 左边进入动画
     */
    public static Bundle slideAnimation(Context context)
    {
        return ActivityOptionsCompat.makeCustomAnimation(context,android.R.anim.slide_in_left,android.R.anim.fade_out).toBundle();
    }

}
