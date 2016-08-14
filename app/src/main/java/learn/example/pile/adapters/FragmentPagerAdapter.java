package learn.example.pile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import learn.example.pile.fragment.JokeCommentFragment;
import learn.example.pile.fragment.JokeListFragment;
import learn.example.pile.fragment.NewsListFragment;
import learn.example.pile.fragment.ReadListFragment;
import learn.example.pile.fragment.VideoListFragment;

/**
 * Created on 2016/5/6.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] title={"新闻","阅读","视频","笑话"};

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
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
