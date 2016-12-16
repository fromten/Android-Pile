package learn.example.pile.activity.normal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;

import learn.example.pile.R;
import learn.example.pile.fragment.comment.ZhihuCommentFragment;
import learn.example.pile.html.plugin.ImageClickPlugin;
import learn.example.pile.html.plugin.JavaScriptPlugin;
import learn.example.pile.html.ZhihuHtml;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuContentService;

/**
 * Created on 2016/10/26.
 */

public class ZhihuReaderActivity extends AppCompatActivity implements IService.Callback<ZhihuNewsContent> {

    public static final String EXTRA_ZHIHU_DOC_ID ="EXTRA_ZHIHU_DOC_ID";


    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private FrameLayout mContentLayout;
    private Toolbar mToolbar;
    private TextView mImageOwner;
    private ImageView mImage;
    private WebView mWebView;
    private ZhihuContentService mZhihuContentService;
    private int docId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setContentView(R.layout.activity_zhihu_reader);
        bindView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState==null)
        {
            requestService();
        }
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
            startCommentFragment();
            return true;
        }else if (item.getItemId()==android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void requestService()
    {
        Intent intent=getIntent();
        docId=intent.getIntExtra(EXTRA_ZHIHU_DOC_ID,0);
        if (docId!=0)
        {
            mZhihuContentService=new ZhihuContentService();
            mZhihuContentService.getContent(docId,this);
        }
    }

    private void bindView()
    {
        mCoordinatorLayout= (CoordinatorLayout) findViewById(R.id.reader_root);
        mAppBarLayout= (AppBarLayout) findViewById(R.id.app_bar_layout);
        mImage= (ImageView) findViewById(R.id.image);
        mImageOwner= (TextView) findViewById(R.id.image_owner);
        mToolbar= (Toolbar) findViewById(R.id.tool_bar);
        mWebView= (WebView) findViewById(R.id.web_view);
        mContentLayout= (FrameLayout) findViewById(R.id.content_wrap);
    }


    private void loadImage(String url, final String imgOwner)
    {
        Glide.with(this).load(url)
                .fitCenter()
                .into(new ImageViewTarget<GlideDrawable>(mImage) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        mImage.setImageDrawable(resource);
                        mImageOwner.setText(imgOwner);
                    }
                });
    }


    public void startCommentFragment()
    {
        Bundle args = new Bundle();
        args.putInt(ZhihuCommentFragment.ARGUMENT_DOCID, docId);
        String name= ZhihuCommentFragment.class.getCanonicalName();
        Intent intent=FragmentActivity.makeIntent(this,name,args);
        startActivity(intent);
    }

    @Override
    public void onSuccess(ZhihuNewsContent data) {
        loadImage(data.getImage(),data.getImageSource());

        ZhihuCommentClickListener commentClickListener=new ZhihuCommentClickListener();
        mWebView.addJavascriptInterface(commentClickListener,commentClickListener.getName());

        //添加图片点击监听
        ImageClickPlugin imageClickInserter =new ImageClickPlugin(this);
        mWebView.addJavascriptInterface(imageClickInserter, imageClickInserter.getName());

        ZhihuHtml zhihuHtml=new ZhihuHtml(data.getBody(),data.getCss(),data.getJs());
        String commentClickJs=commentClickListener.getJavaScript();
        String imageClickJs=imageClickInserter.getJavaScript();
        zhihuHtml.setExtraJs(imageClickJs+"\n"+commentClickJs);

        mWebView.loadDataWithBaseURL(null, zhihuHtml.getHtml(), "text/html", "UTF-8", null);
    }

    @Override
    public void onFailure(String message) {

    }


    /**
     * 替换知乎页面的评论点击
     */
    public class ZhihuCommentClickListener implements JavaScriptPlugin
    {

        @JavascriptInterface()
        public void showCommentFragment()
        {
            startCommentFragment();
        }

        @Override
        public String getName() {
            return "TagClickInsert";
        }

        @Override
        public String getJavaScript() {
            return  "var aTag=document.getElementsByTagName('a');\n" +
                    "aTag=aTag[aTag.length-1];\n" +
                    "aTag.removeAttribute('href');" +
                    "aTag.setAttribute('onClick',\"showAndroidComment()\");\n" +
                    "\n" +
                    "function showAndroidComment()\n" +
                    "{\n" +
                    "  TagClickInsert.showCommentFragment();\n" +
                    "}\n";
        }
    }
}
