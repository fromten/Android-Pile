package learn.example.joke;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;

import java.io.IOException;

import learn.example.joke.ui.SettingFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new SettingFragment())
//                .commit();
//        Glide.get(this).clearDiskCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    private void initView()
    {
        mTabLayout= (TabLayout) findViewById(R.id.tab_layout);
        mViewPager= (ViewPager) findViewById(R.id.page_layout);
        mViewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }
    public boolean isConnectionNet()
    {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if(info!=null&&info.isConnected())
        {

            return true;
        }else
        {
            Toast.makeText(this,"网络不可用",Toast.LENGTH_LONG).show();
            return false;
        }
    }


}
