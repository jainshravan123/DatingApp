package myapp.dating.shravan.datingapp1.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.User;
import myapp.dating.shravan.datingapp1.sqlite_db.DBHelper;

public class UserDashboardProfile extends AppCompatActivity {

    private AppBarLayout app_bar;
    private NestedScrollView nested_scroll;
    private CircleImageView circleImageView;
    private ProgressBar prgBar1;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    User user;
    int PICK_IMAGE_REQUEST = 101;
    Bitmap bitmap;
    String imagePath;
    SweetAlertDialog sAlertDialog;

    private TextView txt_first_name, txt_last_name, txt_email, txt_username, txt_dob, txt_age, txt_country, txt_mobile_number;

    ImageView updatedImageView, profile_back;

    private User userDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard_profile);

        userDashboard           = new User();
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        app_bar                 = (AppBarLayout) findViewById(R.id.app_bar);
        nested_scroll           = (NestedScrollView) findViewById(R.id.nested_scroll);
        profile_back            = (ImageView) findViewById(R.id.profile_back);
        circleImageView         = (CircleImageView) findViewById(R.id.user_profile_photo);
        prgBar1                 = (ProgressBar) findViewById(R.id.prgBar);
        toolbar                 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       // getSupportActionBar().setTitle("Profile");


        Intent intent1 = getIntent();
        userDashboard.setUser_id(intent1.getStringExtra("user_id"));

        txt_first_name    = (TextView) findViewById(R.id.txt_first_name);
        txt_last_name     = (TextView) findViewById(R.id.txt_last_name);
        txt_email         = (TextView) findViewById(R.id.txt_email);
        txt_username      = (TextView) findViewById(R.id.txt_user_name);
        txt_dob           = (TextView) findViewById(R.id.txt_dob);
        txt_age           = (TextView) findViewById(R.id.txt_age);
        txt_country       = (TextView) findViewById(R.id.txt_country);
        txt_mobile_number = (TextView) findViewById(R.id.txt_contact_number);

        getUserProfileData();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilePicDialog();
            }
        });
    }


    public void getUserProfileData()
    {


        String url = Config_Web_API.profile_api + userDashboard.getUser_id();

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                            Log.e("Profile Response : ", response.toString());

                            app_bar.setVisibility(View.VISIBLE);
                            nested_scroll.setVisibility(View.VISIBLE);
                            circleImageView.setVisibility(View.VISIBLE);
                            prgBar1.setVisibility(View.GONE);

                            JSONObject jsonObject = response.getJSONObject("result");
                            user = new User();
                            user.setFirstname(jsonObject.getString("firstName"));
                            user.setLastname(jsonObject.getString("lastName"));
                            user.setUsername(jsonObject.getString("userName"));
                            user.setEmail(jsonObject.getString("email"));
                            user.setDob(jsonObject.getString("DOB"));
                            user.setAge(jsonObject.getString("age"));
                            user.setCountry(jsonObject.getString("country"));
                            user.setPhoneNumber(jsonObject.getString("phoneNumber"));
                            user.setImageUrl(jsonObject.getString("imageUrl"));
                            user.setCoverImage(jsonObject.getString("coverImage"));

                            setProfileData(user);

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

    public void setProfileData(User user)
    {
        Log.e("FirstName : ", user.getFirstname().toString());
        if(!user.getFirstname().toString().equals(""))
        {
            txt_first_name.setText(user.getFirstname().toString());
        }
        if(!user.getLastname().toString().equals(""))
        {
            txt_last_name.setText(user.getLastname().toString());
        }
        if(!user.getUsername().toString().equals(""))
        {
            txt_username.setText(user.getUsername().toString());
        }
        if(!user.getEmail().toString().equals(""))
        {
            txt_email.setText(user.getEmail().toString());
        }
        if(!user.getDob().toString().equals(""))
        {
            txt_dob.setText(user.getDob().toString());
        }
        if(!user.getAge().toString().equals(""))
        {
            txt_age.setText(user.getAge().toString());
        }
        if(!user.getCountry().toString().equals("")) {
            txt_country.setText(user.getCountry().toString());
        }
        if(!user.getPhoneNumber().toString().equals(""))
        {
            txt_mobile_number.setText(user.getPhoneNumber().toString());
        }

        if(!user.getCoverImage().equals(""))
        {
            Picasso.with(getApplicationContext()).load(user.getCoverImage()).into(profile_back);
        }
        if(!user.getImageUrl().equals(""))
        {
            Picasso.with(getApplicationContext()).load(user.getImageUrl()).into(circleImageView);
        }

        if(!user.getFirstname().toString().equals(""))
        {
            collapsingToolbarLayout.setTitle(user.getFirstname().toString());
        }
        else
        {
            collapsingToolbarLayout.setTitle("Unknown");
        }

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }



    public void openProfilePicDialog()
    {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(UserDashboardProfile.this)
                .title("Profile Picture")
                .titleColor(Color.BLACK)
                .customView(R.layout.profile_pic_dialog, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        ImageView imageView      = (ImageView) view1.findViewById(R.id.profilePicImageView);
        Button changeImageBtn = (Button) view1.findViewById(R.id.profilePicChangeBtn);

        if(user.getImageUrl().equals(""))
        {
            imageView.setImageResource(R.drawable.student);
        }
        else{
            Picasso.with(getApplicationContext()).load(user.getImageUrl()).into(imageView);
            //imageView.setImageResource();
        }



        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });


        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                openUpdatedProfilePivDialog();
                updatedImageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openUpdatedProfilePivDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(UserDashboardProfile.this)
                .title("New Picture")
                .titleColor(Color.BLACK)
                .customView(R.layout.profile_pic_update_dialog, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        updatedImageView         = (ImageView) view1.findViewById(R.id.updatedProfilePicImageView);
        Button    changeImageBtn = (Button) view1.findViewById(R.id.profilePicUpdateBtn);

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //new PicUpdate().execute();
            }
        });

        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    public class PicUpdate extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            dialogVisible();
        }

        @Override
        protected Void doInBackground(Void... params) {

            picUpdationProcess();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialogInvisible();
        }
    }

    public void picUpdationProcess()
    {
        try
        {
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            String user_id    = dbHelper.getUserID();

            /*HttpClient client = new DefaultHttpClient();
            File file = new File(imagePath);
            HttpPost post = new HttpPost(Config_Web_API.update_profile_image);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            ///entityBuilder.setContentType(org.apache.http.entity.ContentType.MULTIPART_FORM_DATA);
            entityBuilder.addBinaryBody("file", file);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("image_type", "profile"));
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // add more key/value pairs here as needed

            HttpEntity entity = entityBuilder.build();
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();

            Log.v("result", EntityUtils.toString(httpEntity));*/



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    public void dialogVisible()
    {
        sAlertDialog = new SweetAlertDialog(UserDashboardProfile.this, SweetAlertDialog.PROGRESS_TYPE);
        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sAlertDialog.setTitleText("Loading...");
        sAlertDialog.setCancelable(false);
        sAlertDialog.show();
    }


    public void dialogInvisible()
    {
        sAlertDialog.dismiss();
    }

}
