package learn.example.pile;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Locale;

import learn.example.net.OkHttpRequest;
import learn.example.pile.fragment.CommentFragment;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;
import learn.example.pile.object.NetEase;
import okhttp3.Request;

/**
 * Created on 2016/7/10.
 */
public class ReaderActivity extends AppCompatActivity  {
    private static final String TAG = "ReaderActivity";


    public static final String KEY_ZHIHU_CONTENT_ID ="key_zhihu_content_id";
    public static final String KEY_NETEASE_CONTENT_ID ="key_netease_content_id";
    private boolean isNetEaseContent;
    private int zhihuID;

    private String netEaseDocId;
    private String netEaseBoradId;

    private ZhihuContentService mService;


    private ImageView mImageView;
    private TextView mImgSource;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;


    private ReadContentFragment mReadContentFragment;
    private CommentFragment mCommentFragment;
    private MoveEvent mMoveEventHandle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        setView();
        setTouchHandle();
        setSupportActionBar(mToolbar);
        initPages();
    }

    private void initPages()
    {
        mReadContentFragment=new ReadContentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.read_main_show,mReadContentFragment).commit();

        Intent intent=getIntent();
        isNetEaseContent=intent.hasExtra(KEY_NETEASE_CONTENT_ID);
        if (isNetEaseContent)
        {
            initNetEasePage();

        }else if (intent.hasExtra(KEY_ZHIHU_CONTENT_ID)){
            initZhihuPage();
        }
    }


    private void initNetEasePage()
    {
        String[] array=getIntent().getStringArrayExtra(KEY_NETEASE_CONTENT_ID);
        netEaseBoradId=array[0];
        netEaseDocId =array[1];
        Request request=new Request.Builder().url(String.format(Locale.CHINA,NetEase.ARTICLE_URL2, netEaseDocId)).build();

        OkHttpRequest.getInstance(this).newStringRequest(request, new OkHttpRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String res) {
                 Gson gson=new Gson();
                 JsonObject object=gson.fromJson(res,JsonObject.class);
                 String html= object.getAsJsonObject(netEaseDocId).get("body").getAsString();
                 mReadContentFragment.onSetData(addHead()+html);
                 showMenuItem();
            }
            private String addHead()
            {
                return "<head>\n" +
                        "<style type=\"text/css\">\n" +
                        "\n" +
                        "  p {margin-left: 15px;\n" +
                        "     margin-right: 15px;\n" +
                        "     text-indent: 1%;\n" +
                        "     color: #333333;}\n" +
                        "</style>\n" +
                        "</head>";
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }




    private void initZhihuPage(){

        zhihuID=getIntent().getIntExtra(KEY_ZHIHU_CONTENT_ID,0);
        if (zhihuID==0)
        {
            return;
        }
        performZhihuRequest();

    }


    private void performZhihuRequest()
    {
        mService=new ZhihuContentService();
        mService.getContent(zhihuID, new IService.Callback<ZhihuNewsContent>() {
            @Override
            public void onSuccess(ZhihuNewsContent data) {
                final String imgSource=data.getImageSource();
                Glide.with(ReaderActivity.this).load(data.getImage()).fitCenter().into(new ImageViewTarget<GlideDrawable>(mImageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        mImageView.setImageDrawable(resource);
                        mImgSource.setText(imgSource);
                        showMenuItem();
                    }
                });
                mReadContentFragment.onSetData(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });
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
        mMoveEventHandle=new MoveEvent();
        //管理屏幕手势移动方向
        mMoveEventHandle.setListener(new MoveEvent.MoveListener() {
            @Override
            public boolean onMove(float x, float dx, float y, float dy) {

                int offsetY= (int) (y-dy);
                if (Math.abs(offsetY)<=30)
                {
                    int offsetX= (int) (x-dx);
                    if (Math.abs(offsetX)>=200)
                   {
                       if (offsetX>0)
                       {
                           onBackPressed();
                       }else {
                           showCommentFragment();
                       }
                       return true;
                   }
                }
                return false;
            }
        });

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
        if (mService!=null)
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
        //监听Activity屏幕,左右移动,
        mMoveEventHandle.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
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
            getSupportActionBar().setHomeAsUpIndicator(upArrow);;
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
            if (isNetEaseContent)
            {
                bundle.putStringArray(CommentFragment.KEY_NETEASE_ID,new String[]{netEaseBoradId, netEaseDocId});
            }else {
                bundle.putInt(CommentFragment.KEY_ZHIHU_ID,zhihuID);
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



    private static class MoveEvent {
        private boolean inPress;
        private float downX;
        private float downY;

        private MoveListener mListener;
        public interface MoveListener {
            /**
             *
             * @param x 当前x
             * @param dx 按下的x
             * @param y 当前y
             * @param dy 按下的y
             * @return 是否拦截接下来事件
             */
            boolean onMove(float x,float dx,float y,float dy);
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
                    inPress=!mListener.onMove(x,downX,y,downY);
                }
            }
            return true;
        }

        public MoveListener getListener() {
            return mListener;
        }

        public void setListener(MoveListener listener) {
            mListener = listener;
        }
    }



     private class ReadContentFragment extends Fragment {

        private WebView mWebView;

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
            mWebView.setVerticalScrollBarEnabled(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.addJavascriptInterface(new HtmlCommentClick(),"ReaderActivity");
        }

         /**
          * 此方法由 主Activity调用
           *
          */
        public void onSetData(String html) {
            mWebView.loadDataWithBaseURL(null,html,"text/html","UTF-8",null);
        }

        public void onLoadUrl(String url) {
             mWebView.loadUrl(url);
         }

         /**
          * 此方法由 主Activity调用
          */
         public void onSetData(ZhihuNewsContent data) {
             HtmlHelper htmlHelper=new HtmlHelper();
             String html=htmlHelper.generateHtml(data.getBody(),data.getCss(),data.getJs());
             mWebView.loadDataWithBaseURL(null,html,"text/html","UTF-8",null);
         }

         @Override
         public void onPause() {
             super.onPause();
             mWebView.onPause();
         }

         @Override
         public void onResume() {
             super.onStart();
             mWebView.onResume();
         }

         @Override
         public void onDestroy() {
             mWebView.removeAllViews();
             mWebView.destroy();
             super.onDestroy();
         }
     }

    private class HtmlCommentClick{
        public HtmlCommentClick() {
        }

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
    }



    public static class HtmlHelper{

        private StringBuilder mBuilder=new StringBuilder();

        /**
         * 生成完整的Html页面
         * @param body
         * @param css
         * @param js
         * @return Html
         */
        public String generateHtml(String body,List<String> css, List<String> js)
        {
            mBuilder.append("<html lang='zh-CN'>");
            mBuilder.append(toHeadTag(css,js));
            mBuilder.append("<body>");
            mBuilder.append(body);
            mBuilder.append(getMyJs());//插入一段代码
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

        /**
         * 替换知乎页面的评论点击
         *
         * @see #showCommentFragment();
         * @return javascript text
         */
        public String getMyJs()
        {
            String script="<script type=\"text/javascript\">\n" +
                    "\n" +
                    "var aTag=document.getElementsByTagName('a');\n" +
                    "aTag=aTag[aTag.length-1];\n" +
                    "aTag.removeAttribute('href');" +
                    "aTag.setAttribute('onClick',\"showAndroidComment()\");\n" +
                    "\n" +
                    "function showAndroidComment()\n" +
                    "{\n" +
                    "  ReaderActivity.showCommentFragment();\n" +
                    "}\n" +
                    "</script>";
            return script;
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
