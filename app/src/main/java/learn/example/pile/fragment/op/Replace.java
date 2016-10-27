package learn.example.pile.fragment.op;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created on 2016/10/27.
 */

public class Replace extends Add {

    public Replace(int layoutId) {
        super(layoutId);
    }

    public Replace(int layoutId, int enterAnimId, int exitAnimId) {
        super(layoutId, enterAnimId, exitAnimId);
    }

    @Override
    public void onAction(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction()
                .setCustomAnimations(getEnterAnimId(),getExitAnimId())
                .add(getLayoutId(),fragment,getTag())
                .commit();
    }
}
