package learn.example.pile.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import learn.example.pile.R;

/**
 * Created on 2016/9/9.
 */
public abstract class SupportCommentActivity extends FullScreenActivity {
    /**
     *  应用评论菜单,必须传递一个Bundle值
     *  这个Bundle必须放入一个 KEY_FRAGMENT_CLASS_NAME 键 对应一个fragment类名
     *  这个bundle同时会传递给 Fragment.setArguments();
     */
    public static final String  KEY_APPLY_COMMENT ="video_comment";

    //fragment 类的名字
    public static final String  KEY_FRAGMENT_CLASS_NAME ="fragment_name";


    private static final String TAG_COMMENT_FRAGMENT="comment_fragment";

    private Fragment.SavedState mSavedCommentState;



    private void showAndCreateCommentFragment() {
        Intent intent=getIntent();
        if (intent.hasExtra(KEY_APPLY_COMMENT))
        {
            Bundle bundle=intent.getBundleExtra(KEY_APPLY_COMMENT);
            String fragmentClassName=bundle.getString(KEY_FRAGMENT_CLASS_NAME);
            if (fragmentClassName==null)
            {
                throw new RuntimeException("Must pass on a fragment class name,KEY is SupportCommentActivity.KEY_FRAGMENT_CLASS_NAME");
            }
            try {
                Class<Fragment> fragment= (Class<Fragment>) Class.forName(fragmentClassName);
                Fragment comment= fragment.newInstance();

                comment.setArguments(bundle);
                comment.setInitialSavedState(mSavedCommentState);
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .add(getReplaceId(),comment,TAG_COMMENT_FRAGMENT)
                        .commit();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return fragment 要替换的布局ID
     */
    protected abstract int getReplaceId();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_comment)
        {
            Fragment fragment= getCommentFragmentIfShowing();
            if (fragment==null)//当前fragment没有显示
            {
                showAndCreateCommentFragment();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().hasExtra(KEY_APPLY_COMMENT))
        {
            //支持评论将显示菜单评论按钮
            getMenuInflater().inflate(R.menu.video_menu,menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment= getCommentFragmentIfShowing();
        if (fragment!=null)
        {
            mSavedCommentState=getSupportFragmentManager().saveFragmentInstanceState(fragment);
        }
        super.onBackPressed();
    }

    /**
     * 获得评论fragment
     * @return 如果当前fragment存在,返回这个Fragment,否则 返回Null
     */
    public Fragment getCommentFragmentIfShowing()
    {
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager.findFragmentByTag(TAG_COMMENT_FRAGMENT);
        return fragment;
    }

}
