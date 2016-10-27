package learn.example.pile.fragment.op;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created on 2016/10/26.
 */

public class Hide implements Action {
    private int enterAnimId;
    private int exitAnimId;

    public Hide() {
    }

    public Hide(int enterAnimId, int exitAnimId) {
        this.enterAnimId = enterAnimId;
        this.exitAnimId = exitAnimId;
    }

    public int getEnterAnimId() {
        return enterAnimId;
    }

    public void setEnterAnimId(int enterAnimId) {
        this.enterAnimId = enterAnimId;
    }

    public int getExitAnimId() {
        return exitAnimId;
    }

    public void setExitAnimId(int exitAnimId) {
        this.exitAnimId = exitAnimId;
    }

    @Override
    public void onAction(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction()
                .setCustomAnimations(enterAnimId,exitAnimId)
                .hide(fragment)
                .commit();
    }
}
