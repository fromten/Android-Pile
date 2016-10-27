package learn.example.pile.fragment.op;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created on 2016/10/26.
 */

public interface Action {

    void onAction(FragmentManager manager,Fragment fragment);
}
