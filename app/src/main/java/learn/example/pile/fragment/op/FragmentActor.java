package learn.example.pile.fragment.op;

import android.support.v4.app.Fragment;

/**
 * Created on 2016/10/27.
 */

public interface FragmentActor  {

    void hide(Fragment fragment);
    void show(Fragment fragment);
    void add(int layoutId,Fragment fragment,String tag);
    void replace(int layoutId,Fragment fragment,String tag);
    void remove(Fragment fragment);
    void attach(Fragment fragment);
    void detach(Fragment fragment);
}
