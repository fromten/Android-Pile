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

import learn.example.joke.R;

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
        toolbarCompat();
        mRootLayout= (LinearLayout) findViewById(R.id.root_linelayout);
        mToolbar= (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        enableActionBarHome();
    }

    public void toolbarCompat()
    {
        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup group= (ViewGroup) getWindow().getDecorView();
            mStatusView=createStatusView();
            group.addView(mStatusView);
        }
        //else do nothing
    }


    /**
     *
     * @return 状态栏,只会在Api==19时调用
     */
    @TargetApi(value = 19)
     public View createStatusView()
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
    protected void enableActionBarHome()
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
     */
    @Override
    public void setContentView(int layoutId) {
        LayoutInflater.from(this).inflate(layoutId, mRootLayout);
    }

    @Override
    public void setContentView(View view) {
        throw new RuntimeException("you can call this method to use setContentView(int layoutId) instead)");
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("you can call this method to use setContentView(int layoutId) instead)");
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
        overridePendingTransition(0,android.R.anim.slide_out_right);//添加一个右边退出动画
    }
}
