package learn.example.pile;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import learn.example.joke.R;
import learn.example.net.Service;
import learn.example.pile.fragment.NewsListFragment;
import learn.example.pile.fragment.ReadContentFragment;
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
    private ReadContentFragment mReadContentFragment;
    private AppBarLayout mAppBarLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        setView();
        setSupportActionBar(mToolbar);
       // initWebView();

        mService=new ZhihuContentService();
        int id=getIntent().getIntExtra(KEY_CONTENT_ID,0);
        if (id!=0){
            mReadContentFragment=new ReadContentFragment();
            Bundle bundle=new Bundle();
            bundle.putInt(KEY_CONTENT_ID,id);
            mReadContentFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.read_main_show,mReadContentFragment).commit();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reader_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_comment)
        {

        }

        return super.onOptionsItemSelected(item);
    }

    private void showCommentMenu()
    {
        MenuItem item=  mToolbar.getMenu().findItem(R.id.menu_comment);
        if (item!=null)
        {
            item.setVisible(true);
        }
    }

    @Override
    protected void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onSuccess(final ZhihuNewsContent data) {

        final String imgSource=data.getImageSource();
        Glide.with(this).load(data.getImage()).fitCenter().into(new ImageViewTarget<GlideDrawable>(mImageView) {
            @Override
            protected void setResource(GlideDrawable resource) {
                mImageView.setImageDrawable(resource);
                mImgSource.setText(imgSource);
                showCommentMenu();
            }
        });
        mReadContentFragment.onSuccess(data);

    }



    @Override
    public void onFailure(String msg) {
        mReadContentFragment.onFailure(msg);

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
