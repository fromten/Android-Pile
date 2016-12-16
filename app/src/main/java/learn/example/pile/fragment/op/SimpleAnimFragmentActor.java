package learn.example.pile.fragment.op;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import learn.example.pile.R;

/**
 * Created on 2016/10/26.
 */

public class SimpleAnimFragmentActor implements FragmentActor{

    private FragmentManager mFragmentManager;

    public SimpleAnimFragmentActor(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public void show(Fragment fragment)
    {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_right_in,R.anim.anim_slide_right_out)
                .show(fragment)
                .commit();
    }

    public void hide(Fragment fragment)
    {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_left_in,R.anim.anim_slide_left_out)
                .hide(fragment)
                .commit();
    }

    @Override
    public void add(int layoutId, Fragment fragment, String tag) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_right_in,R.anim.anim_slide_right_out)
                .add(layoutId,fragment,tag)
                .commit();
    }

    @Override
    public void replace(int layoutId, Fragment fragment, String tag) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_right_in,R.anim.anim_slide_right_out)
                .replace(layoutId,fragment,tag)
                .commit();
    }

    @Override
    public void remove(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    public void attach(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .attach(fragment)
                .commit();
    }

    @Override
    public void detach(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .detach(fragment)
                .commit();
    }



}
