package learn.example.pile;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import learn.example.joke.R;
import learn.example.pile.net.StringRequest;
import learn.example.pile.ui.NewsFragment;
import learn.example.pile.ui.VideoFragment;


public class MainActivity extends AppCompatActivity {
    private String URL="http://www.miaopai.com/show/4kU9dsswDerxmthxDsSPWg__.htm";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // initView();
       getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new VideoFragment()).commit();
//        new Thread(){
//            @Override
//            public void run() {
//                requestData();
//            }
//        }.start();
    }
    public void requestData()
    {
        try {
            Document doc= Jsoup.connect(URL).get();
            Log.e(TAG,doc.toString());
            Element element=doc.select("video").first();
            Element img=doc.select("div.video_img").first();
            Log.e(TAG,img.attr("data-url"));
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
