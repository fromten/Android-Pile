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

    public ViewPagerAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(getView(position));
        return getView(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return mViews.indexOf(object);
    }

    public View getView(int position)
    {
        return mViews.get(position);
    }
}
