package learn.example.pile.activity.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import learn.example.pile.R;

import static android.R.attr.value;

/**
 * Created on 2016/6/23.
 */
public class ToolBarActivity extends AppCompatActivity {
    private LinearLayout mRootLayout;
    private Toolbar mToolbar;
    private View mStatusView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.toolbar);
        bindViews();
        setSupportActionBar(mToolbar);
        //setToolBarBackgroundColor(Color.WHITE);
        onEnableActionBarHome();
    }

    protected void bindViews()
    {
        mRootLayout = (LinearLayout) findViewById(R.id.root_linelayout);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
    }

    /**
     * 添加View覆盖系统状态栏,只能在系统版本等于19或20使用
     * 21以上直接使用Theme
     * @throws RuntimeException 大于20或小于19系统版本调用此方法
     */
    @TargetApi(19)
    public void coverSystemStatusBar() {
        if (Build.VERSION.SDK_INT==19||Build.VERSION.SDK_INT==20) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup group = (ViewGroup) getWindow().getDecorView();
            mStatusView = createStatusView();
            group.addView(mStatusView);
        }else {
            throw new RuntimeException("Method must in Api==19 or Api==20 call");
        }
    }


    public void setToolBarBackgroundColor(int color) {
        if (mToolbar != null)
            mToolbar.setBackgroundColor(color);
    }

    public void setStatusBarColor(int color) {
        if (mStatusView != null) {
            mStatusView.setBackgroundColor(color);
        } else if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(color);
        }
    }


    /**
     * 创建一个View ,背景颜色设置为Theme.colorPrimaryDark,高度设置为系统状态栏高度
     * @return view
     */
     @TargetApi(value = 19)
     private View createStatusView()
     {
         int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
         int statusBarHeight =getResources().getDimensionPixelSize(resourceId);
         View statusView = new View(this);
         LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                 statusBarHeight);
         statusView.setLayoutParams(params);
         statusView.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark,null));
         return statusView;
     }


    /**
     * 启用动作栏,Home按钮,子类可以覆盖此方法去禁用显示Home按钮
     */
    protected void onEnableActionBarHome()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 将需要的布局放入根布局中
     * @param layoutId
     * @see #setContentView(View view)
     * @see #setContentView(View view, ViewGroup.LayoutParams params)
     */
    @Override
    public final void setContentView(int layoutId) {
        LayoutInflater.from(this).inflate(layoutId, mRootLayout);
    }

    @Override
    public final void setContentView(View view) {
        mRootLayout.addView(view);
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        mRootLayout.addView(view,params);
    }

    public Toolbar getToolBar()
    {
        return mToolbar;
    }

    public View getStatusBar()
    {
        return mStatusView;
    }


}
