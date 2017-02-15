package learn.example.pile.activity.normal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;

import learn.example.pile.R;
import learn.example.pile.activity.base.ToolBarActivity;

/**
 * Created on 2016/9/16.
 */
public class FragmentActivity extends ToolBarActivity {

    //fragment类的名字
    public static final String EXTRA_FRAGMENT_CLASS_NAME ="EXTRA_FRAGMENT_CLASS_NAME";

    /**
     *   fragment 的参数 {@code  Fragment.setArguments(argus);}
     */
    public static final String EXTRA_FRAGMENT_ARGUMENTS ="EXTRA_FRAGMENT_ARGUMENTS";

    private static final String TAG_FRAGMENT="FRAGMENT_ACTIVITY_TAG_FRAGMENT";
    private static final String TAG = "FragmentActivity";

    private FrameLayout mFrameLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setToolBarBackgroundColor(Color.TRANSPARENT);
        mFrameLayout=new FrameLayout(this);
        mFrameLayout.setId(R.id.root);
        setContentView(mFrameLayout);
        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            showAndCreateFragment();
        }
    }

    public void showAndCreateFragment() {
        Intent intent=getIntent();
        if (intent.hasExtra(EXTRA_FRAGMENT_CLASS_NAME))
        {
            String name=intent.getStringExtra(EXTRA_FRAGMENT_CLASS_NAME);
            Bundle argus=intent.getBundleExtra(EXTRA_FRAGMENT_ARGUMENTS);
            try{
                Fragment fragment=Fragment.instantiate(this,name,argus);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.root,fragment,TAG_FRAGMENT)
                        .commit();
            }catch (Fragment.InstantiationException e)
            {
                Log.d(TAG,"instance error,class ："+name);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    public static Intent makeIntent(Context context,String fragmentClassName, Bundle fragmentArgus)
    {
        Intent intent=new Intent(context,FragmentActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_CLASS_NAME,fragmentClassName);
        intent.putExtra(EXTRA_FRAGMENT_ARGUMENTS,fragmentArgus);
        return intent;
    }



}
