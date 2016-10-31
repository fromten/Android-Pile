package learn.example.pile.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import learn.example.pile.R;
import learn.example.pile.fragment.caterogy.JokeListFragment;
import learn.example.pile.fragment.caterogy.NewsListFragment;
import learn.example.pile.fragment.caterogy.ReadListFragment;
import learn.example.pile.fragment.caterogy.VideoListFragment;

/**
 * Created on 2016/5/6.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] title;

    public FragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        title=context.getResources().getStringArray(R.array.tab_category);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position)
        {
            case 0:fragment=new NewsListFragment();break;
            case 1:fragment=new ReadListFragment();break;
            case 2:fragment=new VideoListFragment();break;
            case 3:fragment=new JokeListFragment();break;
        }

       return fragment;
    }

    public String getTag(int position)
    {
        return "ListFragment"+position;
    }


    @Override
    public int getCount() {
        return title.length;
    }
}
