package myapp.dating.shravan.datingapp1.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.bean.Gift;

/**
 * Created by admin on 18/08/16.
 */
public class GiftRoseFragment extends Fragment
{
    ArrayList<Gift> rose_gifts_list;
    String TAG = "GiftRoseFragment";

    public GiftRoseFragment()
    {

    }

    public GiftRoseFragment(ArrayList<Gift> c_rose_gifts_list)
    {
     this.rose_gifts_list = c_rose_gifts_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_gifts_rose_layout, container, false);
        for(Gift gift : rose_gifts_list)
        {
            Log.e(TAG, gift.getGift_name());
        }
        return rootView;
    }

}
