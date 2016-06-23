package learn.example.pile;

import android.support.v7.app.AppCompatActivity;

/**
 * Created on 2016/6/23.
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,android.R.anim.slide_out_right);//添加一个右边退出动画
    }
}
