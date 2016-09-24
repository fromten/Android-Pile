package learn.example.pile.activity.normal.reader;

import android.support.v4.app.Fragment;

public interface ContentRenderer {
    /**
     * 当Activity 创建时调
     * @param activity {@link ReaderActivity}
     */
    void onActivityCreated(ReaderActivity activity);

    /**
     * 当Activity 销毁时调用
     */
    void onActivityDestroy();


    /**
     * 是否支持评论菜单项
     * @return true 会调用{@link ContentRenderer#onCreateCommentFragment()},else wise
     */
    boolean onHasCommentMenu();

    /**
     * 创建评论Fragment,在ReaderActivity 界面上显示评论菜单项
     * @return fragment
     */
    Fragment onCreateCommentFragment();
}