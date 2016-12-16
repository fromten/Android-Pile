package learn.example.pile.html.plugin;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created on 2016/11/27.
 */

public abstract class ContextPlugin implements JavaScriptPlugin {
    protected WeakReference<Context> mWeakReference;

    public ContextPlugin(Context context) {
        mWeakReference = new WeakReference<Context>(context);
    }
}
