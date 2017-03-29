package myapp.dating.shravan.datingapp1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 18/08/16.
 */
public class GiftsViewPagerAdapter extends FragmentPagerAdapter
{
    ArrayList<Fragment> fragments   = new ArrayList<>();
    ArrayList<String>   tabTitles   = new ArrayList<>();

    public void addFragments(Fragment fragments,String titles)
    {
        this.fragments.add(fragments);
        this.tabTitles.add(titles);

    }


    public GiftsViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitles.get(position);
    }
}
