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
import learn.example.pile.activity.normal.FragmentActivity;
import learn.example.pile.activity.normal.DetailJokeActivity;
import learn.example.pile.activity.normal.PhotoActivity;
import learn.example.pile.activity.normal.ShortVideoActivity;
import learn.example.pile.activity.normal.VideoActivity;
import learn.example.pile.activity.normal.ZhihuReaderActivity;
import learn.example.pile.fragment.NetEaseReaderFragment;

/**
 * Created on 2016/6/23.
 */
public class ActivityLauncher {



    /**
     * 启动视频播放器Activity,普通的显示视频
     * @param context
     * @param url
     */
    public static void startVideoActivity(@NonNull Context context, @NonNull String url)
    {
        Intent intent=new Intent(context, VideoActivity.class);
        intent.setData(Uri.parse(url));
        startActivity(context,intent,null);
    }


    /**
     * 开启视频播放器,这会显示评论菜单项
     * @param uri 视频播放Uri地址
     * @param title 视频显示的标题
     * @param fragmentClassName fragment来显示内容;
     * @param argus fragment对应参数 <code> Fragment.setArguments(argus) </code>
     * @see FragmentActivity
     */
    public static void startVideoActivitySupportCommentMenu(@NonNull Context context,@NonNull String uri,@Nullable String title,
                                                            String fragmentClassName,Bundle argus)
    {
        Intent intent=new Intent(context, VideoActivity.class);
        intent.setData(Uri.parse(uri));
        intent.putExtra(FragmentActivity.KEY_FRAGMENT_CLASS_NAME,fragmentClassName);
        intent.putExtra(VideoActivity.KEY_TITLE,title);
        intent.putExtra(FragmentActivity.KEY_FRAGMENT_ARGUMENTS,argus);
        startActivity(context,intent,null);
    }


    /**
     * 启动图片查看Activity
     * @param context
     * @param url 图片的URL
     */

    public static void startPhotoActivityForSingle(@NonNull Context context, @NonNull String url)
    {
       startPhotoActivityForMulti(context,new String[]{url},0);
    }

    /**
     * 启动图片查看Activity
     * @param context
     * @param url 图片的URL
     */

    public static void startPhotoActivityForMulti(@NonNull Context context, @NonNull String[] url, int position)
    {
        Intent intent=new Intent(context, PhotoActivity.class);
        intent.putExtra(PhotoActivity.KEY_IMAGE_URLS,url);
        intent.putExtra(PhotoActivity.KEY_SCROLL_TO_POSITION,position);
        startActivity(context,intent,makeExplodeAnimation(context));
    }

    /**
     * 启动图片查看器,查看网易新闻多图片新闻
     * @param context
     * @param skipID  @see learn.example.pile.jsonbean.NetEaseNews
     */
    public static void startPhotoActivityForNetEase(@NonNull Context context, @NonNull String skipID)
    {
        Intent intent=new Intent(context, PhotoActivity.class);
        intent.putExtra(PhotoActivity.KEY_NETEASE_SKIPID,skipID);
        startActivity(context,intent,makeExplodeAnimation(context));
    }



    /**
     * 启动显示详细笑话内容Activity
     * @param groupJson  groupBean class 反序列的json 数据
     */
    public static void startDetailJokeActivity(@NonNull Context context, @NonNull String groupJson)
    {
        Intent intent=new Intent(context, DetailJokeActivity.class);
        intent.putExtra(DetailJokeActivity.KEY_GROUP_JSON,groupJson);
        startActivity(context,intent,null);
    }

    /**
     * 启动阅读器,显示知乎内容
     * @param id  id 知乎文章ID
     */
    public static void startZhihuReaderActivity(@NonNull Context context, @NonNull int id)
    {
        Intent intent=new Intent(context, ZhihuReaderActivity.class);
        intent.putExtra(ZhihuReaderActivity.KEY_ZHIHU_DOC_ID,id);
        startActivity(context,intent,null);
    }

    public static void startNetEaseReaderActivity(Context context,String docId,String boardId)
    {
        Bundle args=new Bundle();
        args.putString(NetEaseReaderFragment.KEY_DOCID,docId);
        args.putString(NetEaseReaderFragment.KEY_BOARD_ID,boardId);
        String className=NetEaseReaderFragment.class.getCanonicalName();

        Intent intent=FragmentActivity.makeIntent(context,className,args);
        startActivity(context,intent,makeSlideAnimation(context));
    }

    public static void startChatActivity(@NonNull Context context)
    {
        Intent intent=new Intent(context, ChatActivity.class);
        startActivity(context,intent,null);
    }


    /**
     * 启动activity 播放短视屏
     */
    public static void startShortVideoActivity(Context context,String url)
    {
        Intent intent=new Intent(context, ShortVideoActivity.class);
        intent.setData(Uri.parse(url));
        startActivity(context,intent,null);
    }

    /**
     * 启动Activity
     * @param context 上下文
     * @param intent
     * @param bundle 动画
     */
    public static void startActivity(@NonNull Context context,@NonNull Intent intent,@Nullable Bundle bundle)
    {
        ContextCompat.startActivities(context,new Intent[]{intent},bundle);
    }


    /**
     *  左边进入动画
     * @param context
     * @return bundle to perform animation
     */
    public static Bundle makeSlideAnimation(Context context)
    {
        return ActivityOptionsCompat.makeCustomAnimation(context,R.anim.anim_slide_right_in,R.anim.anim_slide_right_out).toBundle();
    }


    /**
     * 从中心打开动画
     * @param context
     * @return
     */
    public static Bundle makeExplodeAnimation(Context context)
    {
        return ActivityOptionsCompat.makeCustomAnimation(context, R.anim.anim_center_open,0).toBundle();
    }

}
