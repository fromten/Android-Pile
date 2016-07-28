package learn.example.pile.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created on 2016/7/28.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mViews;
    private onViewShowListener mOnViewShowListener;
    public ViewPagerAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    public onViewShowListener getOnViewShowListener() {
        return mOnViewShowListener;
    }

    public void setOnViewShowListener(onViewShowListener onViewShowListener) {
        mOnViewShowListener = onViewShowListener;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        if (mOnViewShowListener!=null)
        {
            mOnViewShowListener.onBindView(mViews.get(position),position);
        }
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    public interface onViewShowListener{
        void onBindView(View view,int position);
    }
}
