package learn.example.pile.fragment.op;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created on 2016/10/26.
 */

public class Add extends Show {
    private String tag;
    private int layoutId;

    public Add(int layoutId) {
        this.layoutId = layoutId;
    }

    public Add(int layoutId,int enterAnimId,int exitAnimId)
    {
        super(enterAnimId,exitAnimId);
        this.layoutId=layoutId;
    }

    public String getTag() {
        return tag;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public void onAction(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction()
                .setCustomAnimations(getEnterAnimId(),getExitAnimId())
                .add(layoutId,fragment,tag)
                .commit();
    }
}
