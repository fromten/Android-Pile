package learn.example.pile.activity.normal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import learn.example.pile.R;
import learn.example.pile.activity.base.ToolBarActivity;

/**
 * Created on 2016/9/16.
 */
public class CommentActivity extends ToolBarActivity {

    //fragment类的名字
    public static final String KEY_FRAGMENT_CLASS_NAME="fragment_class_name";

    /**
     *   fragment 的参数 {@code  Fragment.setArguments(argus);}
     */
    public static final String KEY_FRAGMENT_ARGUMENTS="fragment_argus";



    private FrameLayout mFrameLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrameLayout=new FrameLayout(this);
        mFrameLayout.setId(R.id.root);
        setContentView(mFrameLayout);
        showAndCreateCommentFragment();
    }

    public void showAndCreateCommentFragment() {
        Intent intent=getIntent();
        if (intent.hasExtra(KEY_FRAGMENT_CLASS_NAME))
        {
            String name=intent.getStringExtra(KEY_FRAGMENT_CLASS_NAME);
            Bundle argus=intent.getBundleExtra(KEY_FRAGMENT_ARGUMENTS);
            try {
                Class<Fragment> fragment= (Class<Fragment>) Class.forName(name);
                Fragment comment= fragment.newInstance();
                comment.setArguments(argus);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.root,comment)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
