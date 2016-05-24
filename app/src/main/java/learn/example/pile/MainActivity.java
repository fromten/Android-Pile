package learn.example.pile;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import learn.example.joke.R;
import learn.example.pile.ui.JokeListFragment;
import learn.example.pile.ui.NewsFragment;


public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // initView();
       getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new JokeListFragment()).commit();

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
//        mViewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager()));
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setupWithViewPager(mViewPager);
//    }


}
