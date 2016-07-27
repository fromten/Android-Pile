package learn.example.pile;

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

/**
 * Created on 2016/6/23.
 */
public class BaseActivity extends AppCompatActivity {
    private LinearLayout mRootLayout;
    private Toolbar mToolbar;
    private View mStatusView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.toolbar);
      //  statusBarCompat();
        mRootLayout = (LinearLayout) findViewById(R.id.root_linelayout);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        onEnableActionBarHome();
    }

    public void statusBarCompat() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup group = (ViewGroup) getWindow().getDecorView();
            mStatusView = createStatusView();
            group.addView(mStatusView);
        }
        //else do nothing
    }


    public void setActionBarBackgroundColor(int color) {
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
     *
     * @return 状态栏,只会在Api==19时调用
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
    public void setContentView(int layoutId) {
        LayoutInflater.from(this).inflate(layoutId, mRootLayout);
    }

    @Override
    public void setContentView(View view) {
        mRootLayout.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
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

    @Override
    public void finish() {
        super.finish();
        onPerformExitAnim();
    }

    /**
     * 退出动画,子类可以覆盖此方法
     */
    protected void onPerformExitAnim()
    {
        overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);//添加一个右边退出动画
    }
}
