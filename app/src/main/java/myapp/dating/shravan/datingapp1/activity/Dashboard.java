package myapp.dating.shravan.datingapp1.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.DiscoverMode;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.Gifts;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.Messages;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.NewsFeed;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.OnDemandDating;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.PersonalTagFilters;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.Profile;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.SearchPeople;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.Setting;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.Upgrade;
import myapp.dating.shravan.datingapp1.adapter.UserAdapter;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.Device;
import myapp.dating.shravan.datingapp1.bean.SignInUser;
import myapp.dating.shravan.datingapp1.bean.User;
import myapp.dating.shravan.datingapp1.session.SessionManager;
import myapp.dating.shravan.datingapp1.sqlite_db.DBHelper;

public class Dashboard extends AppCompatActivity {

    Toolbar      toolbar1;
    DrawerLayout mDrawerLayout;
    ProgressBar  prgBar;
    FloatingActionButton fab_add;
    NavigationView navigationView;
    User user;
    RecyclerView usersRecyclerView;
    ArrayList<User> users;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    private IntentFilter filter = new IntentFilter("dating.app.REFRESH_DASHBOARD_INTENT");

    String TAG = "Dashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        prgBar            = (ProgressBar) findViewById(R.id.prgBar);
        usersRecyclerView = (RecyclerView) findViewById(R.id.usersRecyclerView);
        fab_add           = (FloatingActionButton) findViewById(R.id.fab_add);
        toolbar1          = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout     = (DrawerLayout) findViewById(R.id.drawer_layout);
        users             = new ArrayList<User>();

        setSupportActionBar(toolbar1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                switch (id) {

                    case R.id.navigation_item_home:
                                                                            return true;

                    case R.id.navigation_item_search_people:
                                                                            Intent intent1 = new Intent(getApplicationContext(), SearchPeople.class);
                                                                            startActivity(intent1);
                                                                            return true;

                    case R.id.navigation_item_personal_tags_filters:
                                                                            Intent intent2 = new Intent(getApplicationContext(), PersonalTagFilters.class);
                                                                            startActivity(intent2);
                                                                            return true;

                    case R.id.navigation_item_messages:
                                                                            Intent intent3 = new Intent(getApplicationContext(), Messages.class);
                                                                            startActivity(intent3);
                                                                            return true;

                    case R.id.navigation_item_news_feed:
                                                                            Intent intent4 = new Intent(getApplicationContext(), NewsFeed.class);
                                                                            startActivity(intent4);
                                                                            return true;

                    case R.id.navigation_item_gifts:
                                                                            Intent intent5 = new Intent(getApplicationContext(), Gifts.class);
                                                                            startActivity(intent5);
                                                                            return true;

                    case R.id.navigation_item_on_demand_dating:
                                                                            Intent intent6 = new Intent(getApplicationContext(), OnDemandDating.class);
                                                                            startActivity(intent6);
                                                                            return true;

                    case R.id.navigation_item_discover_mode:
                                                                            Intent intent7 = new Intent(getApplicationContext(), DiscoverMode.class);
                                                                            startActivity(intent7);
                                                                            return true;

                    case R.id.navigation_item_upgrade:
                                                                            Intent intent8 = new Intent(getApplicationContext(), Upgrade.class);
                                                                            startActivity(intent8);
                                                                            return true;

                    case R.id.navigation_item_setting:
                                                                            Intent intent9 = new Intent(getApplicationContext(), Setting.class);
                                                                            startActivity(intent9);
                                                                            return true;


                    case R.id.navigation_item_logout:
                                                                            logoutProcess();
                                                                            return true;

                    default:
                                                                            return true;

                }


            }
        });

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        View headerLayout = navigationView.getHeaderView(0);
        TextView txtViewD = (TextView) headerLayout.findViewById(R.id.username);
        txtViewD.setText(dbHelper.getUsername());

        //OnClick Listener on Profile View
        RelativeLayout drawer_layout_relative = (RelativeLayout) headerLayout.findViewById(R.id.drawer_layout_relative);
        drawer_layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent1 = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent1);
            }
        });

        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Complete Process for logout
    public void logoutProcess(){

        final SweetAlertDialog sAlertDialog;
        sAlertDialog = new SweetAlertDialog(Dashboard.this, SweetAlertDialog.PROGRESS_TYPE);
        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sAlertDialog.setTitleText("Logging Out...");
        sAlertDialog.setCancelable(false);
        sAlertDialog.show();
        //Counter for holding splash screens
        new CountDownTimer(2000,1000) {

            @Override
            public void onFinish() {


                sAlertDialog.dismiss();
                //Deleting User Data
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                dbHelper.deleteUserTable();

                //Deleting Session
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.setLogin(false);

                //Redirecting to SignIn Activity
                Intent intent1 = new Intent(getApplicationContext(), Signin.class);
                startActivity(intent1);
                finish();

            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }

    public void getUsers()
    {

        String url = Config_Web_API.all_users_api;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        prgBar.setVisibility(View.GONE);
                        usersRecyclerView.setVisibility(View.VISIBLE);
                        try
                        {
                            Log.e("Users API(Dashboard)", response.toString());
                            JSONArray jsonArray = response.getJSONArray("data");
                            for(int i=0; i < jsonArray.length(); i++)
                            {
                               User user1 = new User();
                               JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                               user1.setUser_id(jsonObject.getString("user_id"));
                               user1.setEmail(jsonObject.getString("email"));
                               user1.setFirstname(jsonObject.getString("firstName"));
                               user1.setLastname(jsonObject.getString("lastName"));
                               user1.setImageUrl(jsonObject.getString("imageUrl"));
                               user1.setIsActive(jsonObject.getString("isActive"));
                               users.add(user1);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        getProfileInfo();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

        setDataAtDashboard();
    }


    public void getProfileInfo()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Config_Web_API.profile_api + dbHelper.getUserID();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                        user = new User();
                        Log.e("Profile at Dashobard : ", response.toString());
                        prgBar.setVisibility(View.GONE);
                        fab_add.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = response.getJSONObject("result");
                        user.setImageUrl(jsonObject.getString("imageUrl"));
                        setDataAtNavigationDrawer();

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

    public void setDataAtNavigationDrawer()
    {
        View headerLayout = navigationView.getHeaderView(0);
        CircleImageView circleImageView = (CircleImageView) headerLayout.findViewById(R.id.profile_image);
        if(!user.getImageUrl().equals(""))
        {
            Picasso.with(getApplicationContext()).load(user.getImageUrl()).error(R.drawable.student).into(circleImageView);
        }
    }

    public void setDataAtDashboard()
    {
        usersRecyclerView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(3);
        usersRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        UserAdapter userAdapter = new UserAdapter(users, getApplicationContext(), Dashboard.this);

        usersRecyclerView.setAdapter(userAdapter);
    }

    @Override
    protected void onResume()
    {
        this.registerReceiver(receiver, filter);
        super.onResume();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT);
            Log.e(TAG, "Broadcast Received Successfully.");

            String updatedPicPath = intent.getStringExtra("updatedImagePath");
            user.setImageUrl(updatedPicPath);
            Log.e(TAG, ": Updated Pic Path in Brodcast receiver : "+updatedPicPath);
            View headerLayout = navigationView.getHeaderView(0);
            CircleImageView circleImageView = (CircleImageView) headerLayout.findViewById(R.id.profile_image);
            Picasso.with(getApplicationContext()).load(updatedPicPath).error(R.drawable.student).into(circleImageView);
        }
    };

}
