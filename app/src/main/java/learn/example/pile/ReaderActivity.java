package learn.example.pile;


import android.gesture.GestureUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.List;

import learn.example.joke.R;
import learn.example.pile.fragment.CommentFragment;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;

/**
 * Created on 2016/7/10.
 */
public class ReaderActivity extends AppCompatActivity implements IService.Callback<ZhihuNewsContent> {
    private static final String TAG = "ReaderActivity";


    public static final String KEY_CONTENT_ID="KEY_CONTENT_ID";
    private ZhihuContentService mService;


    private ImageView mImageView;
    private TextView mImgSource;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private int newsId;

    private ReadContentFragment mReadContentFragment;
    private CommentFragment mCommentFragment;

    private TouchDirectionEvent mTouchDirectionEvent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        setView();
        setTouchHandle();
        setSupportActionBar(mToolbar);

        mService=new ZhihuContentService();
        int id=getIntent().getIntExtra(KEY_CONTENT_ID,0);
        if (id!=0){
            mReadContentFragment=new ReadContentFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.read_main_show,mReadContentFragment).commit();
            mService.getContent(id,this);
        }

    }

    private void setView()
    {
        mToolbar= (Toolbar) findViewById(R.id.tool_bar);
        mAppBarLayout= (AppBarLayout) findViewById(R.id.app_bar_layout);
        mImageView= (ImageView) findViewById(R.id.image);
        mImgSource= (TextView) findViewById(R.id.img_source);
    }



    private void setTouchHandle()
    {
        mTouchDirectionEvent=new TouchDirectionEvent();
        //管理屏幕手势移动方向
        mTouchDirectionEvent.setListener(new TouchDirectionEvent.MoveDirectionListener() {
            @Override
            public boolean toRight(int offset) {
                if (offset>=200)
                {
                    onBackPressed();
                    return true;
                }
                return false;
            }

            @Override
            public boolean toLeft(int offset) {
                if (offset>=200)
                {
                    showCommentFragment();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reader_menu,menu);
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
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);//添加一个右边退出动画
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mTouchDirectionEvent.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 显示菜单向上回退按钮
     */
    private void showMenuBackUp()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * 显示评论Fragment
     * 如果Fragment已经创建过,并当前是隐藏时,会显示它
     */
    public void  showCommentFragment()
    {
        mAppBarLayout.setExpanded(false,true);
        if (mCommentFragment==null)
        {
            mCommentFragment=new CommentFragment();
            Bundle bundle=new Bundle();
            bundle.putInt(CommentFragment.KEY_ID,newsId);
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
        if (mCommentFragment!=null) {
            if (mCommentFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left)
                        .hide(mCommentFragment).commit();
            } else {
                super.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }

    }

    private void showMenuItem()
    {
        //延时操作
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MenuItem item=  mToolbar.getMenu().findItem(R.id.menu_comment);
                if (item!=null)
                {
                    item.setVisible(true);
                }
                showMenuBackUp();
            }
        });
    }


    @Override
    public void onSuccess(final ZhihuNewsContent data) {
        newsId=data.getId();
        final String imgSource=data.getImageSource();
        Glide.with(this).load(data.getImage()).fitCenter().into(new ImageViewTarget<GlideDrawable>(mImageView) {
            @Override
            protected void setResource(GlideDrawable resource) {
                mImageView.setImageDrawable(resource);
                mImgSource.setText(imgSource);
                showMenuItem();

            }
        });

        //传递事件
        mReadContentFragment.onSuccess(data);
    }



    @Override
    public void onFailure(String msg) {
        //传递事件
        mReadContentFragment.onFailure(msg);
    }


    private static class TouchDirectionEvent {
        private boolean inPress;
        private float downX;
        private float downY;

        private MoveDirectionListener mListener;
        public interface MoveDirectionListener {
            //返回值代表是否拦截接下来事件

            boolean toRight(int offset);
            boolean toLeft(int offset);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (mListener==null)
            {
                return false;
            }
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
                    if (Math.abs(y-downY)>=80)
                    {
                        inPress=false;
                        return true;
                    }

                    int offsetX= (int) (x-downX);
                    if (offsetX>=0)
                    {
                        inPress=!mListener.toRight(offsetX);
                    }else {
                        inPress=!mListener.toLeft(Math.abs(offsetX));
                    }
                }
            }
            return true;
        }

        public MoveDirectionListener getListener() {
            return mListener;
        }

        public void setListener(MoveDirectionListener listener) {
            mListener = listener;
        }
    }



     private static class ReadContentFragment extends Fragment implements IService.Callback<ZhihuNewsContent> {

        private WebView mWebView;
        private ReaderActivity.HtmlHelper mHtmlHelper;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.fragment_read_content,container,false);
            mWebView= (WebView) view.findViewById(R.id.web_view);
            initWebView();
            return view;
        }


        private void initWebView(){
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setDisplayZoomControls(false);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        }

         /**
          * 此方法由 主Activity调用
           * @param data
          */
        @Override
        public void onSuccess(ZhihuNewsContent data) {
            if (mHtmlHelper==null)
            {
                mHtmlHelper=new ReaderActivity.HtmlHelper();
            }
            String html=mHtmlHelper.generateHtml(data.getBody(),data.getCss(),data.getJs());
            mWebView.loadDataWithBaseURL(null,html,"text/html","UTF-8",null);
        }

         /**
          * 此方法由 主Activity调用
          * @param message
          */
        @Override
        public void onFailure(String message) {

        }
    }




    public static class HtmlHelper{

        private StringBuilder mBuilder=new StringBuilder();

        public String generateHtml(String body,List<String> css, List<String> js)
        {
            mBuilder.append("<html lang='zh-CN'>");
            mBuilder.append(toHeadTag(css,js));
            mBuilder.append("<body>");
            mBuilder.append(body);
            mBuilder.append("</body>");
            mBuilder.append("</html>");
            return mBuilder.toString();
        }


        public String toHeadTag(List<String> css, List<String> js)
        {
            StringBuilder builder=new StringBuilder();
            builder.append("<head>");
            for (String str:css)
            {
                Log.d(TAG,str );
                builder.append(toLinkTag(str));
            }
            builder.append(getMyCss());
            for (String str:js)
            {
                builder.append(toScriptTag(str));
            }
            builder.append("</head>");
            return builder.toString();
        }

        /**
         * 知乎默认css会添加200px的头部图片高度,使用得到css覆盖原本的css属性
         * @return
         */
        public String getMyCss()
        {
            String str="<style type='text/css'>\n" +
                    ".headline .img-place-holder {\n" +
                    "  height: 0px;\n" +
                    "}"+
                    "</style>";
            return str;
        }


        public String toLinkTag(String css)
        {
            return "<link rel='stylesheet' type='text/css' href='"+css+"'>";
        }

        public String toScriptTag(String js)
        {
            return "<script src='"+js+"'>"+"</script>";
        }
    }
}
