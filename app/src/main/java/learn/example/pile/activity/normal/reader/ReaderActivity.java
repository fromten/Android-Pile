package learn.example.pile.activity.normal.reader;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.Locale;

import learn.example.net.OkHttpRequest;
import learn.example.pile.R;
import learn.example.pile.fragment.WebFragment;
import learn.example.pile.fragment.comment.CommentFragment;
import learn.example.pile.fragment.comment.NetEaseCommentFragment;
import learn.example.pile.fragment.comment.ZhihuCommentFragment;
import learn.example.pile.html.ImageClickInserter;
import learn.example.pile.html.NetEaseHtml;
import learn.example.pile.html.VideoClickInserter;
import learn.example.pile.html.ZhihuHtml;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;
import learn.example.pile.object.NetEase;
import learn.example.pile.util.HtmlTagBuild;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created on 2016/7/10.
 */
public class ReaderActivity extends AppCompatActivity implements RendererCompleteListener {
    private static final String TAG = "ReaderActivity";

    public static final String KEY_ZHIHU_CONTENT_ID ="key_zhihu_content_id";
    public static final String KEY_NETEASE_CONTENT_ID ="key_netease_content_id";


    private ImageView mImageView;
    private TextView mImgSource;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private WebFragment mWebFragment;
    private Fragment mCommentFragment;
    private MoveEvent mMoveEvent;

    private ContentRenderer mContentRenderer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setTitle(null);
        setContentView(R.layout.activity_reader);
        setView();
        setSupportActionBar(mToolbar);
        initPages();
        mMoveEvent=new MoveEvent();
    }



    private void initPages()
    {
        mWebFragment= (WebFragment) getSupportFragmentManager().findFragmentById(R.id.read_main_show);
        mWebFragment.requestProgressBar(true,0);
        if (getIntent().hasExtra(KEY_ZHIHU_CONTENT_ID))
        {
            mContentRenderer=new ZhihuRenderer();
        }else {
            mContentRenderer=new NetEaseRenderer();
        }
        mContentRenderer.onActivityCreated(this);
    }



    private void setView()
    {
        mToolbar= (Toolbar) findViewById(R.id.tool_bar);
        mAppBarLayout= (AppBarLayout) findViewById(R.id.app_bar_layout);
        mImageView= (ImageView) findViewById(R.id.image);
        mImgSource= (TextView) findViewById(R.id.img_source);
    }

    public WebView getWebView()
    {
        return mWebFragment.getWebView();
    }

    public ImageView getToolbarImageView(){
        return mImageView;
    }

    public TextView getToolbarTextView(){
        return mImgSource;
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
        if (mContentRenderer!=null)
        {
            mContentRenderer.onActivityDestroy();
        }
        mWebFragment.getWebView().destroy();
        super.onDestroy();
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
    private void showMenuBackUpButton()
    {
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * 显示评论Fragment
     * 如果Fragment已经创建过,并当前是隐藏时,显示它
     *
     */
    public void  showCommentFragment()
    {
        //还没有创建或创建失败
        if (mContentRenderer==null)return;

        mAppBarLayout.setExpanded(false,true);
        FragmentTransaction mAnimationTransaction=getSupportFragmentManager()
                 .beginTransaction()
                 .setCustomAnimations(R.anim.anim_slide_right_to_start,R.anim.anim_slide_out_to_right);
        if (mCommentFragment==null)
        {
            Fragment fragment=mContentRenderer.onCreateCommentFragment();
            mCommentFragment=fragment;
            if (fragment!=null)
            {
                mAnimationTransaction
                        .add(R.id.read_main_show,mCommentFragment)
                        .commit();
            }

        }else if (!mCommentFragment.isVisible()){
            mAnimationTransaction.show(mCommentFragment).commit();
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
                        .setCustomAnimations(R.anim.anim_slide_left_in,R.anim.anim_slide_left_to_end)
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
                showMenuBackUpButton();
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

    @Override
    public void onRenderCompleted(final String content, final boolean useLocalCss) {
        mAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                if (useLocalCss)
                {
                    mWebFragment.loadLocalDataWithDefCss(content);
                }else {
                    mWebFragment.loadLocalData(content);
                }
            }
        });
        showMenuItem();
        setWebProgressVisibility(View.INVISIBLE);
    }


    private  class MoveEvent {
        private boolean inPress;
        private float downX;
        private float downY;

        private int xOffset;
        private int yOffset;

        public MoveEvent() {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            yOffset= metrics.heightPixels/15;
            xOffset = metrics.widthPixels/3;
        }

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
                   if (Math.abs(y-downY)<=yOffset)
                   {
                       int offsetX= (int) (x-downX);
                       if (Math.abs(offsetX)>=xOffset)
                       {
                           if (offsetX>0)
                           {
                               onBackPressed();
                           }else {
                               showCommentFragment();
                           }
                          inPress=false;
                          return true;
                       }
                   }
                }
            }
            return false;
        }
    }



}
