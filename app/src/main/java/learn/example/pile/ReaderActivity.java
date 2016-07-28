package learn.example.pile;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.Locale;

import learn.example.net.OkHttpRequest;
import learn.example.pile.fragment.CommentFragment;
import learn.example.pile.fragment.WebFragment;
import learn.example.pile.html.NetEaseHtml;
import learn.example.pile.html.ZhihuHtml;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;
import learn.example.pile.object.NetEase;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.HtmlTagBuild;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created on 2016/7/10.
 */
public class ReaderActivity extends AppCompatActivity  {
    private static final String TAG = "ReaderActivity";


    public static final String KEY_ZHIHU_CONTENT_ID ="key_zhihu_content_id";
    public static final String KEY_NETEASE_CONTENT_ID ="key_netease_content_id";


    private NetEaseManager mNetEaseManager;
    private ZhihuManager mZhihuManager;


    private ImageView mImageView;
    private TextView mImgSource;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private WebFragment mWebFragment;
    private CommentFragment mCommentFragment;

    private MoveEvent mMoveEvent=new MoveEvent();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        setView();
        setSupportActionBar(mToolbar);
        initPages();
    }



    private void initPages()
    {
        mWebFragment= new WebFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.read_main_show,mWebFragment).commit();

        mAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                mWebFragment.requestProgressBar(true,0);
            }
        });
        Intent intent=getIntent();

        if (intent.hasExtra(KEY_NETEASE_CONTENT_ID))
        {
            String[] array=getIntent().getStringArrayExtra(KEY_NETEASE_CONTENT_ID);
            String netEaseBoardId=array[0];
            String netEaseDocId =array[1];
            mNetEaseManager=new NetEaseManager(netEaseDocId,netEaseBoardId);
            mNetEaseManager.requestHtml();
        }else if (intent.hasExtra(KEY_ZHIHU_CONTENT_ID)){
            int  zhihuID=getIntent().getIntExtra(KEY_ZHIHU_CONTENT_ID,0);
            mZhihuManager=new ZhihuManager(zhihuID);
            mZhihuManager.requestHtml();
        }
    }



    private void setView()
    {
        mToolbar= (Toolbar) findViewById(R.id.tool_bar);
        mAppBarLayout= (AppBarLayout) findViewById(R.id.app_bar_layout);
        mImageView= (ImageView) findViewById(R.id.image);
        mImgSource= (TextView) findViewById(R.id.img_source);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbar.inflateMenu(R.menu.reader_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_comment)
        {
            showCommentFragment();
        }else if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        if (mNetEaseManager!=null)
            mNetEaseManager.remove();

        if (mZhihuManager!=null)
            mZhihuManager.remove();

        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);//添加一个右边退出动画
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (mMoveEvent.onTouchEvent(ev)||getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 显示菜单向上回退按钮
     */
    private void showMenuBackUp()
    {
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //设置箭头为黑色
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        if (upArrow!=null)
        {
            upArrow.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.black_light,null), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    /**
     * 显示评论Fragment
     * 如果Fragment已经创建过,并当前是隐藏时,显示它
     *
     */
    public void  showCommentFragment()
    {
        mAppBarLayout.setExpanded(false,true);
        if (mCommentFragment==null)
        {
            mCommentFragment=new CommentFragment();
            Bundle bundle=new Bundle();
            if (mNetEaseManager!=null)
            {
                bundle.putStringArray(CommentFragment.KEY_NETEASE_ID,new String[]{mNetEaseManager.netEaseBoardId, mNetEaseManager.netEaseDocId});
            }else if (mZhihuManager!=null){
                bundle.putInt(CommentFragment.KEY_ZHIHU_ID,mZhihuManager.zhihuDocId);
            }
            mCommentFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left)
                    .add(R.id.read_main_show,mCommentFragment).commit();
        }else if (!mCommentFragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left)
                    .show(mCommentFragment).commit();
        }
    }


    /**
     * 监听回退建按下,如果CommentFragment看得见,隐藏它.
     * 否则调用父类方法
     */
    @Override
    public void onBackPressed() {
        if (mCommentFragment!=null&&mCommentFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left)
                        .hide(mCommentFragment).commit();
        }else {
            super.onBackPressed();
        }

    }

    private void showMenuItem()
    {
        //延时操作
        mToolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                MenuItem item=  mToolbar.getMenu().findItem(R.id.menu_comment);
                if (item!=null)
                {
                    item.setVisible(true);
                }
                showMenuBackUp();
            }
        },500);
    }

    public void setWebProgressVisibility(final int visible)
    {
        mAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mWebFragment.getProgressBar()!=null)
                {
                    mWebFragment.getProgressBar().setVisibility(visible);
                }
            }
        });

    }



    private class MoveEvent {
        private boolean inPress;
        private float downX;
        private float downY;

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                inPress = true;
                downX=event.getX();
                downY=event.getY();
            } else if (action == MotionEvent.ACTION_UP) {
                inPress = false;
            } else if (action == MotionEvent.ACTION_MOVE) {
                if (inPress) {
                    float x=event.getX();
                    float y=event.getY();
                   if (Math.abs(y-downY)<=30)
                   {
                       int offsetX= (int) (x-downX);
                       if (Math.abs(offsetX)>=200)
                       {
                           if (offsetX>0)
                           {
                               onBackPressed();
                           }else {
                               showCommentFragment();
                           }
                          inPress=false;
                       }
                       return true;
                   }
                }
            }
            return false;
        }
    }

    private class HtmlClick{
        @JavascriptInterface()
        public void showCommentFragment()
        {
            //正确的姿势使用这个方法
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ReaderActivity.this.showCommentFragment();
                }
            });
        }

        @JavascriptInterface()
        public void openPhotos(final String res)
        {
            //正确的姿势使用这个方法
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bundle anim= ActivityLauncher.openAnimation(ReaderActivity.this);
                    ActivityLauncher.startPhotoActivity(ReaderActivity.this,new String[]{res},anim);
                }
            });
        }
    }

    private class NetEaseManager{
        private String netEaseDocId;
        private String netEaseBoardId;
        private Call mCall;
        public NetEaseManager(String netEaseDocId, String netEaseBoardId) {
            this.netEaseDocId = netEaseDocId;
            this.netEaseBoardId = netEaseBoardId;
        }

        public void requestHtml()
        {
            Request request=new Request.Builder().url(String.format(Locale.CHINA,NetEase.ARTICLE_URL2, netEaseDocId)).build();
            mCall=OkHttpRequest.getInstance(ReaderActivity.this).newStringRequest(request, new OkHttpRequest.RequestCallback<String>() {
                @Override
                public void onSuccess(String res) {

                    setWebProgressVisibility(View.INVISIBLE);

                    mWebFragment.getWebView().getSettings().setJavaScriptEnabled(true);
                    mWebFragment.getWebView().addJavascriptInterface(new HtmlClick(),"ReaderActivity");
                    String html=new NetEaseHtml(netEaseDocId,res).getHtml();
                    html+= HtmlTagBuild.jsTag(getImageClickInterface());
                    mWebFragment.loadLocalData(html);
                    showMenuItem();
                }
                @Override
                public void onFailure(String msg) {

                }
            });
        }

        private String getImageClickInterface()
        {
            return "var objs = document.getElementsByTagName(\"img\");\n" +
                    "for(var i=0;i<objs.length;i++)\n" +
                    "    {\n" +
                    "       objs[i].onclick=function(){\n" +
                    "          ReaderActivity.openPhotos(this.src);\n" +
                    "       }\n" +
                    "    }";
        }

        public void remove()
        {
           if (mCall!=null)
               mCall.cancel();
        }
    }
    private class ZhihuManager{

        private int zhihuDocId;
        private ZhihuContentService mService;
        public ZhihuManager(int docId) {
            zhihuDocId=docId;
        }

        public void requestHtml()
        {
           mService=new ZhihuContentService();
           mService.getContent(zhihuDocId, new IService.Callback<ZhihuNewsContent>() {
                @Override
                public void onSuccess(ZhihuNewsContent data) {
                    setWebProgressVisibility(View.INVISIBLE);

                    final String imgSource=data.getImageSource();
                    Glide.with(ReaderActivity.this).load(data.getImage()).fitCenter().into(new ImageViewTarget<GlideDrawable>(mImageView) {
                        @Override
                        protected void setResource(GlideDrawable resource) {
                            mImageView.setImageDrawable(resource);
                            mImgSource.setText(imgSource);
                            showMenuItem();
                        }
                    });
                    mWebFragment.getWebView().getSettings().setJavaScriptEnabled(true);
                    mWebFragment.getWebView().addJavascriptInterface(new HtmlClick(),"ReaderActivity");
                    ZhihuHtml html=new ZhihuHtml(data.getBody(),data.getCss(),new String[]{getMyJs()});
                    mWebFragment.loadLocalData(html.generateHtml());
                }

                @Override
                public void onFailure(String message) {

                }
           });
        }

        /**
         * 替换知乎页面的评论点击
         * @return javascript text
         */
        public String getMyJs()
        {
            return  "var aTag=document.getElementsByTagName('a');\n" +
                    "aTag=aTag[aTag.length-1];\n" +
                    "aTag.removeAttribute('href');" +
                    "aTag.setAttribute('onClick',\"showAndroidComment()\");\n" +
                    "\n" +
                    "function showAndroidComment()\n" +
                    "{\n" +
                    "  ReaderActivity.showCommentFragment();\n" +
                    "}\n";
        }

        public void remove()
        {
            if (mService!=null)
                mService.cancelAll();
        }
    }

}
