package myapp.dating.shravan.datingapp1.activity.nav_draw_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.activity.Dashboard;
import myapp.dating.shravan.datingapp1.activity.Signin;
import myapp.dating.shravan.datingapp1.adapter.GiftsViewPagerAdapter;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.Device;
import myapp.dating.shravan.datingapp1.bean.Gift;
import myapp.dating.shravan.datingapp1.bean.SignInUser;
import myapp.dating.shravan.datingapp1.bean.User;
import myapp.dating.shravan.datingapp1.fragment.GiftDiamondFragment;
import myapp.dating.shravan.datingapp1.fragment.GiftRoseFragment;
import myapp.dating.shravan.datingapp1.session.SessionManager;
import myapp.dating.shravan.datingapp1.sqlite_db.DBHelper;

public class Gifts extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    GiftsViewPagerAdapter giftsViewPagerAdapter;
    ProgressBar progressBar;
    ArrayList<Gift> all_gifts_list;
    ArrayList<Gift> diamond_gifts_list;
    ArrayList<Gift> rose_gifts_list;
    String TAG = "GiftsModule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

        progressBar     = (ProgressBar) findViewById(R.id.prgBar);
        tabLayout       = (TabLayout)findViewById(R.id.tabLayout);
        viewPager       = (ViewPager)findViewById(R.id.viewPager);
        toolbar         = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Gifts");

        all_gifts_list         = new ArrayList<Gift>();
        diamond_gifts_list = new ArrayList<Gift>();
        rose_gifts_list    = new ArrayList<Gift>();


        getGifts();

    }


    public void getGifts()
    {
        String url = Config_Web_API.all_gifts_api;

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!

                        Log.e(TAG, response.toString());
                        try {

                              String success = response.getString("success");

                            if(success.equals("success"))
                            {

                                JSONObject jsonObject = response.getJSONObject("result");
                                JSONArray  jsonDiamondArray  = jsonObject.getJSONArray("giftDiamondList");

                               for(int i=0; i<jsonDiamondArray.length(); i++)
                               {
                                   JSONObject jsonObject1 = (JSONObject) jsonDiamondArray.get(i);

                                   Gift gift = new Gift();
                                   gift.setId(jsonObject1.getString("_id"));
                                   gift.setGift_id(jsonObject1.getString("gift_id"));
                                   gift.setImage(jsonObject1.getString("image"));
                                   gift.setGift_name(jsonObject1.getString("gift_name"));
                                   gift.setPrice(jsonObject1.getString("price"));
                                   gift.setGift_type("gift_type");

                                   diamond_gifts_list.add(gift);

                               }

                                JSONArray jsonRoseArray = jsonObject.getJSONArray("giftRoseList");

                                for(int i=0; i<jsonRoseArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = (JSONObject) jsonRoseArray.get(i);

                                    Gift gift = new Gift();
                                    gift.setId(jsonObject1.getString("_id"));
                                    gift.setGift_id(jsonObject1.getString("gift_id"));
                                    gift.setImage(jsonObject1.getString("image"));
                                    gift.setGift_name(jsonObject1.getString("gift_name"));
                                    gift.setPrice(jsonObject1.getString("price"));
                                    gift.setGift_type("gift_type");

                                    rose_gifts_list.add(gift);

                                }


                                //Showing the fragments now
                                showFragments();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }

    public void showFragments()
    {

        progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        giftsViewPagerAdapter = new GiftsViewPagerAdapter(getSupportFragmentManager());
        giftsViewPagerAdapter.addFragments(new GiftDiamondFragment(diamond_gifts_list), "Diamonds");
        giftsViewPagerAdapter.addFragments(new GiftRoseFragment(rose_gifts_list), "Roses");
        viewPager.setAdapter(giftsViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
