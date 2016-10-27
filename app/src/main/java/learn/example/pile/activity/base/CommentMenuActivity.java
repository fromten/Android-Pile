package learn.example.pile.activity.base;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import learn.example.pile.R;
import learn.example.pile.activity.normal.FragmentActivity;

/**
 * 帮助类,创建评论菜单,点击时会开启新的FragmentActivity;
 * @see FragmentActivity
 */
public abstract class CommentMenuActivity extends FullScreenActivity {


    public void startCommentActivity() {
        Intent intent=getIntent();
        intent.setClass(this,FragmentActivity.class);
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
        if (getIntent().hasExtra(FragmentActivity.KEY_FRAGMENT_CLASS_NAME))
        {
            getMenuInflater().inflate(R.menu.video_menu,menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
