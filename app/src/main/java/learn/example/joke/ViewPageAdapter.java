package learn.example.joke;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import learn.example.joke.ui.JokeFragment;
import learn.example.joke.ui.NewsFragment;
import learn.example.joke.ui.SettingFragment;

/**
 * Created on 2016/5/6.
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private final String[] title={"笑话","新闻","设置"};
    public ViewPageAdapter(FragmentManager fm) {
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
            case 0:fragment=new JokeFragment();break;
            case 1:fragment=new NewsFragment();break;
            case 2:fragment=new SettingFragment();break;
        }
       return fragment;
    }


    @Override
    public int getCount() {
        return title.length;
    }
}
