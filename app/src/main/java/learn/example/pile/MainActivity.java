package learn.example.pile;

import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import learn.example.joke.R;
import learn.example.pile.net.UrlRequestManager;
import learn.example.pile.ui.JokeListFragment;
import learn.example.pile.ui.VideoFragment;


public class MainActivity extends AppCompatActivity {
    private String URL="http://www.wandoujia.com/eyepetizer/detail.html?vid=5868";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initView();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new JokeListFragment()).commit();
//        new Thread(){
//            @Override
//            public void run() {
//                Glide.get(getApplicationContext()).clearDiskCache();
//            }
//        }.start();
    }
    public void requestData()
    {
        try {
            Document doc= Jsoup.connect(URL).get();
            Log.e(TAG,doc.toString());
            Element element=doc.select("video").first();
            Element img=doc.select("#content-container").first();
            Log.e(TAG,img.attr("style"));
            Log.e(TAG,element.attr("src"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

//    private void initView()
//    {
//        mTabLayout= (TabLayout) findViewById(R.id.tab_layout);
//        mViewPager= (ViewPager) findViewById(R.id.page_layout);
//        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setupWithViewPager(mViewPager);
//    }


}
