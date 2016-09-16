package learn.example.pile.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.CheckResult;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import learn.example.pile.R;
import learn.example.pile.activity.normal.CommentActivity;

/**
 * 帮助类,创建评论菜单,点击时会开启新的CommentActivity;
 * @see CommentActivity
 */
public abstract class CommentMenuActivity extends FullScreenActivity {


    public void startCommentActivity() {
        Intent intent=getIntent();
        intent.setClass(this,CommentActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_comment)
        {
            startCommentActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().hasExtra(CommentActivity.KEY_FRAGMENT_CLASS_NAME))
        {
            //支持评论将显示菜单评论按钮
            getMenuInflater().inflate(R.menu.video_menu,menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
