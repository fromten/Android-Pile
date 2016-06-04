package learn.example.pile;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import learn.example.joke.R;
import learn.example.pile.adapters.ViewPagerAdapter;
import learn.example.pile.fragment.SettingFragment;
import learn.example.pile.util.AppCheck;


public class MainActivity extends AppCompatActivity {

    private final int permissionResultCode=99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView()
    {
        //如果版本大于23和外部存储可以用时需要请求权限
        if (Build.VERSION.SDK_INT>=23&& AppCheck.checkExternalStorageState())
        {
            maybeRequirePermission();
        }else {
            showView();
        }
    }


    @TargetApi(value = 23)
    public void maybeRequirePermission()
    {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //no do something
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionResultCode);
            }
        }else {
            showView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==permissionResultCode)
        {
            showView();
        }
    }

    public void showView()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_show,new ViewPagerFragment()).commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_setting)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_show, new SettingFragment())
                    .addToBackStack(null)
                    .commit();
        }
        return true;
    }

    public static class ViewPagerFragment extends Fragment {
        private TabLayout mTabLayout;
        private ViewPager mViewPager;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.view_page,container,false);
        }
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mTabLayout= (TabLayout)view.findViewById(R.id.tab_layout);
            mViewPager= (ViewPager) view.findViewById(R.id.page_layout);
            mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }


}
