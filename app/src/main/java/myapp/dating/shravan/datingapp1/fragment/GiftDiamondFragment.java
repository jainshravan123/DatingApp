package myapp.dating.shravan.datingapp1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.activity.Dashboard;
import myapp.dating.shravan.datingapp1.adapter.DiamondGiftsAdapter;
import myapp.dating.shravan.datingapp1.adapter.UserAdapter;
import myapp.dating.shravan.datingapp1.bean.Gift;

/**
 * Created by admin on 18/08/16.
 */
public class GiftDiamondFragment extends Fragment
{

    ArrayList<Gift> diamond_gifts_list;
    String TAG = "GiftDiamondFragment";
    RecyclerView gift_diamond_recycle_view;
    StaggeredGridLayoutManager mStaggeredLayoutManager;

    public GiftDiamondFragment()
    {

    }

    public GiftDiamondFragment(ArrayList<Gift> c_diamond_gifts_list)
    {
       this.diamond_gifts_list = c_diamond_gifts_list;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_gifts_diamond_layout, container, false);
        for(Gift gift : diamond_gifts_list)
        {
            Log.e(TAG, gift.getGift_name());
        }

        gift_diamond_recycle_view = (RecyclerView) rootView.findViewById(R.id.gift_diamond_recycle_view);

        gift_diamond_recycle_view.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        gift_diamond_recycle_view.setLayoutManager(mStaggeredLayoutManager);

        DiamondGiftsAdapter diamondGiftsAdapter = new DiamondGiftsAdapter(diamond_gifts_list, getActivity());

        gift_diamond_recycle_view.setAdapter(diamondGiftsAdapter);

        return rootView;
    }
}
