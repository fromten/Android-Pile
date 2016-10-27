package learn.example.pile.fragment.op;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Objects;

import learn.example.pile.R;

/**
 * Created on 2016/10/26.
 */

public class SimpleFragmentActor implements FragmentActor{
    private Show mShowActon;
    private Hide mHideAction;
    private FragmentManager mFragmentManager;

    public SimpleFragmentActor(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void show(Fragment fragment)
    {
        if (mShowActon==null)
        {
            mShowActon=new Show(R.anim.anim_slide_right_in,R.anim.anim_slide_right_out);
        }
        mShowActon.onAction(mFragmentManager,fragment);
    }

    public void hide(Fragment fragment)
    {
        if (mHideAction==null)
        {
            mHideAction=new Hide(R.anim.anim_slide_left_in,R.anim.anim_slide_left_out);
        }
        mHideAction.onAction(mFragmentManager,fragment);
    }

    @Override
    public void add(int layoutId, Fragment fragment, String tag) {
        Add add= new Add(layoutId,R.anim.anim_slide_right_in,R.anim.anim_slide_right_out);
        add.setTag(tag);
        add.onAction(mFragmentManager,fragment);
    }

    @Override
    public void replace(int layoutId, Fragment fragment, String tag) {
        Replace replace=new Replace(layoutId,R.anim.anim_slide_right_in,R.anim.anim_slide_right_out);
        replace.setTag(tag);
        replace.onAction(mFragmentManager,fragment);
    }

    @Override
    public void remove(Fragment fragment) {

    }

    @Override
    public void attach(Fragment fragment) {

    }

    @Override
    public void detach(Fragment fragment) {

    }



}
