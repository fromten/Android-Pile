package learn.example.pile.activity.normal;

import android.Manifest;
import android.annotation.TargetApi;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import learn.example.net.OkHttpRequest;
import learn.example.pile.R;
import learn.example.pile.activity.base.ToolBarActivity;
import learn.example.pile.adapters.FragmentPagerAdapter;
import learn.example.pile.fragment.base.RVListFragment;
import learn.example.pile.fragment.SettingFragment;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.DeviceInfo;


public class MainActivity extends ToolBarActivity {

    private final int permissionResultCode=99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化okhttp
        OkHttpRequest.getInstance(this);
        initView();
    }

    public void initView()
    {
        //如果版本大于23和外部存储可以用时,需要请求权限
        if (Build.VERSION.SDK_INT>=23&& DeviceInfo.checkExternalStorageState())
        {
            requirePermission();
        }else {
            showMainFragment();
        }
    }


    @TargetApi(value = 23)
    public void requirePermission()
    {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Do nothing
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionResultCode);
            }
        }else {
            showMainFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==permissionResultCode)
        {
            showMainFragment();
        }
    }

    /**
     * 显示FragmentViewPager
     */
    public void showMainFragment()
    {
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager.findFragmentByTag(ViewPagerFragment.TAG);
        if (fragment==null)
        {
            fragment=new ViewPagerFragment();
            manager.beginTransaction()
                    .add(R.id.main_show,fragment,ViewPagerFragment.TAG)
                    .commitAllowingStateLoss();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_user_setting)
        {
            //判断当前的SettingFragment是否显示
            if (getSupportFragmentManager().findFragmentByTag(SettingFragment.TAG)==null)
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_show, new SettingFragment(),SettingFragment.TAG)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        }else if (item.getItemId()==R.id.menu_machine)
        {
            ActivityLauncher.startChatActivity(this);
        }
        return true;
    }

    @Override
    protected void onEnableActionBarHome() {
        //取消显示菜单向上回退按钮
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class ViewPagerFragment extends Fragment  {
        private TabLayout mTabLayout;
        private ViewPager mViewPager;
        private FragmentStatePagerAdapter mFragmentPagerAdapter;
        public static final String  TAG="ViewPagerFragment";
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

            mFragmentPagerAdapter=new FragmentPagerAdapter(getContext(),getFragmentManager());
            mViewPager.setAdapter(mFragmentPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager)
            {
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    Fragment fragment= (Fragment) mFragmentPagerAdapter.instantiateItem(mViewPager,tab.getPosition());
                    if (fragment!=null&&fragment instanceof RVListFragment)
                    {    //滚动到顶部
                        ((RVListFragment) fragment).getRecyclerView().scrollToPosition(0);
                    }
                }
            });
        }
    }


}
