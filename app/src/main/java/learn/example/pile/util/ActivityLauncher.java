package learn.example.pile.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import learn.example.joke.R;
import learn.example.pile.ChatActivity;
import learn.example.pile.PhotoActivity;
import learn.example.pile.ReaderActivity;
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
    public static void startInternalWebActivity(@NonNull Context context,@NonNull String url,Bundle bundle)
    {
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.setData(Uri.parse(url));
        ActivityCompat.startActivity((Activity) context,intent,bundle);
    }


    /**
     * 启动视频播放器Activity,使用左边进入动画,
     * @param context
     * @param url
     */
    public static void startVideoActivity(@NonNull Context context, @NonNull String url,Bundle bundle)
    {
        Intent intent=new Intent(context, VideoActivity.class);
        intent.setData(Uri.parse(url));
        ActivityCompat.startActivity((Activity) context,intent,bundle);
    }


    /**
     * 启动图片查看Activity,使用左边进入动画
     * @param context
     * @param url
     */

    public static void startPhotoActivity(@NonNull Context context, @NonNull String url,Bundle bundle)
    {
        Intent intent=new Intent(context, PhotoActivity.class);
        intent.putExtra(PhotoActivity.KEY_IMG_URL,url);
        ActivityCompat.startActivity((Activity) context,intent,bundle);
    }


    public static void startReaderActivity(@NonNull Context context, @NonNull int id,Bundle bundle)
    {
        Intent intent=new Intent(context, ReaderActivity.class);
        intent.putExtra(ReaderActivity.KEY_CONTENT_ID,id);
        ActivityCompat.startActivity((Activity) context,intent,bundle);
    }

    public static void startChatActivity(@NonNull Context context,Bundle bundle)
    {
        Intent intent=new Intent(context, ChatActivity.class);
        ActivityCompat.startActivity((Activity) context,intent,bundle);
    }


    /**
     *  左边进入动画
     * @param context
     * @return bundle to perform animation
     */
    public static Bundle slideAnimation(Context context)
    {
        return ActivityOptionsCompat.makeCustomAnimation(context,R.anim.anim_slide_in_right,R.anim.anim_slide_out_left).toBundle();
    }


    /**
     * 重中心打开动画
     * @param context
     * @return
     */
    public static Bundle openAnimation(Context context)
    {
        return ActivityOptionsCompat.makeCustomAnimation(context, R.anim.anim_center_open,0).toBundle();
    }

}
