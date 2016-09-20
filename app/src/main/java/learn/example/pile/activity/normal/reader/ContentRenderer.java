package learn.example.pile.activity.normal.reader;

import android.support.v4.app.Fragment;

public interface ContentRenderer {
    void onActivityCreated(ReaderActivity activity);
    void onActivityDestroy();
    Fragment onCreateCommentFragment();
}