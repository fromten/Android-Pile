package learn.example.pile.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;

import learn.example.pile.R;
import learn.example.pile.activity.normal.ChatActivity;
import learn.example.pile.activity.normal.PhotoActivity;
import learn.example.pile.activity.normal.ReaderActivity;
import learn.example.pile.activity.normal.VideoActivity;
import learn.example.pile.activity.normal.WebViewActivity;

/**
 * Created on 2016/6/23.
 */
public class ActivityLauncher {

    /**
     * 启动自己应用程序内部Web浏览器Activity
     * @param context
     * @param url
     */
    public static void startInternalWebActivity(@NonNull Context context,@NonNull String url,Bundle bundle)
    {
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.setData(Uri.parse(url));
        startActivity(context,intent,bundle);
    }


    /**
     * 启动视频播放器Activity
     * @param context
     * @param url
     */
    public static void startVideoActivity(@NonNull Context context, @NonNull String url,Bundle bundle)
    {
        Intent intent=new Intent(context, VideoActivity.class);
        intent.setData(Uri.parse(url));
        startActivity(context,intent,bundle);
    }


    /**
     * 启动图片查看Activity
     * @param context
     * @param url 图片的URL
     */

    public static void startPhotoActivityForSingle(@NonNull Context context, @NonNull String url, Bundle bundle)
    {
        Intent intent=new Intent(context, PhotoActivity.class);
        intent.putExtra(PhotoActivity.KEY_IMG_URL,url);
        startActivity(context,intent,bundle);
    }

    /**
     * 启动图片查看器,查看网易新闻图片新闻
     * @param context
     * @param skipID
     * @param bundle
     */
    public static void startPhotoActivityForNetEase(@NonNull Context context, @NonNull String skipID,Bundle bundle)
    {
        Intent intent=new Intent(context, PhotoActivity.class);
        intent.putExtra(PhotoActivity.KEY_NETEASE_SKIPID,skipID);
        startActivity(context,intent,bundle);
    }


    /**
     * 启动阅读器,显示知乎内容
     * @param id  id 知乎文章ID
     */
    public static void startReaderActivityForZhihu(@NonNull Context context, @NonNull int id, Bundle bundle)
    {
        Intent intent=new Intent(context, ReaderActivity.class);
        intent.putExtra(ReaderActivity.KEY_ZHIHU_CONTENT_ID,id);
        startActivity(context,intent,bundle);
    }

    /**
     * 启动阅读器,显示网易内容
     * @param array  所需要的消息,array[0]=boardID,=array[1]=docID
     */
    public static void startReaderActivityForNetEase(@NonNull Context context, String[] array, Bundle bundle)
    {
        Intent intent=new Intent(context, ReaderActivity.class);
        intent.putExtra(ReaderActivity.KEY_NETEASE_CONTENT_ID,array);
        startActivity(context,intent,bundle);
    }

    public static void startChatActivity(@NonNull Context context,Bundle bundle)
    {
        Intent intent=new Intent(context, ChatActivity.class);
        startActivity(context,intent,bundle);
    }

    public static void startActivity(@NonNull Context context,@NonNull Intent intent,@Nullable Bundle bundle)
    {
        ContextCompat.startActivities(context,new Intent[]{intent},bundle);
    }



    /**
     *  左边进入动画
     * @param context
     * @return bundle to perform animation
     */
    public static Bundle slideAnimation(Context context)
    {
        return ActivityOptionsCompat.makeCustomAnimation(context,R.anim.anim_slide_right_to_start,R.anim.anim_slide_out_to_right).toBundle();
    }


    /**
     * 从中心打开动画
     * @param context
     * @return
     */
    public static Bundle openAnimation(Context context)
    {
        return ActivityOptionsCompat.makeCustomAnimation(context, R.anim.anim_center_open,0).toBundle();
    }

}
