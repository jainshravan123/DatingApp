package myapp.dating.shravan.datingapp1.activity.nav_draw_activity;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.util.EntityUtils;

import myapp.dating.shravan.datingapp1.Manifest;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.activity.SignUp;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.User;
import myapp.dating.shravan.datingapp1.sqlite_db.DBHelper;

public class Profile extends AppCompatActivity
{


    private AppBarLayout app_bar;
    private NestedScrollView nested_scroll;
    private FloatingActionButton floatingActionButton;
    private CircleImageView circleImageView;
    private ProgressBar prgBar1;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    User user;
    int PICK_IMAGE_REQUEST = 101;
    Bitmap bitmap;
    String imagePath;
    SweetAlertDialog sAlertDialog;
    String TAG = "Profile";
    MaterialDialog.Builder updatePicBuilder;
    MaterialDialog updatePicMaterialDialog;
    String picUpdateResult, picUpdateSuccess, updatedPicPath;
    Uri image_uri;


    private TextView txt_first_name, txt_last_name, txt_email, txt_username, txt_dob, txt_age, txt_country, txt_mobile_number;

    ImageView updatedImageView, profile_back;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        app_bar              = (AppBarLayout) findViewById(R.id.app_bar);
        nested_scroll        = (NestedScrollView) findViewById(R.id.nested_scroll);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_edit);
        circleImageView      = (CircleImageView) findViewById(R.id.user_profile_photo);
        prgBar1              = (ProgressBar) findViewById(R.id.prgBar);
        toolbar              = (Toolbar) findViewById(R.id.toolbar);
        picUpdateResult      = "";
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Profile");


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

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Config_Web_API.profile_api + dbHelper.getUserID();

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
                        floatingActionButton.setVisibility(View.VISIBLE);
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

        if(!user.getImageUrl().equals(""))
        {
            Picasso.with(getApplicationContext()).load(user.getImageUrl()).into(circleImageView);
        }
    }



    public void openProfilePicDialog()
    {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(Profile.this)
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
            Picasso.with(getApplicationContext()).load(user.getImageUrl()).error(R.drawable.student).into(imageView);
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

            image_uri = data.getData();
            getReadPermissison();

            //imagePath = getPath(uri);
          /*  final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            if(isKitKat)
            {
                boolean isMam = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
                if(isMam)
                {
                    imagePath = getPath2(image_uri);
                    Log.e(TAG, "image Path :" + imagePath);
                }

            }else
            {

                imagePath = getRealPathFromURI(getApplicationContext(), image_uri);
                Log.e(TAG, "image Path :" + imagePath);
            }
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                // Log.d(TAG, String.valueOf(bitmap));
                openUpdatedProfilePivDialog();
                updatedImageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }
    }

    private void openUpdatedProfilePivDialog(){
         updatePicBuilder = new MaterialDialog.Builder(Profile.this)
                .title("New Picture")
                .titleColor(Color.BLACK)
                .customView(R.layout.profile_pic_update_dialog, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        updatePicMaterialDialog = updatePicBuilder.build();
        updatePicMaterialDialog.show();

        View view1 = updatePicMaterialDialog.getCustomView();

        updatedImageView         = (ImageView) view1.findViewById(R.id.updatedProfilePicImageView);
        Button    changeImageBtn = (Button) view1.findViewById(R.id.profilePicUpdateBtn);

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new PicUpdate().execute();
            }
        });

        View negative  = updatePicMaterialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePicMaterialDialog.dismiss();
            }
        });
    }

    public String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public class PicUpdate extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            dialogVisible();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.e(TAG, "Image Path from Background Method : " + imagePath);

            DBHelper dbHelper = new DBHelper(getApplicationContext());


            picUpdationProcess(imagePath, dbHelper.getUserID());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            //Updating Dashboard data using broadcast receiver
             Intent intent = new Intent();
            intent.setAction("dating.app.REFRESH_DASHBOARD_INTENT");

            try {

                JSONObject jsonObject = new JSONObject(picUpdateResult);
                picUpdateSuccess      = jsonObject.getString("error");
                updatedPicPath        = jsonObject.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            intent.putExtra("updatedImagePath", updatedPicPath);
            sendBroadcast(intent);

            updatePicMaterialDialog.dismiss();
            dialogInvisible();
            imageUploadSuccess();


        }
    }



    private void picUpdationProcess(String imagePath, String pUserId) {
        try
        {
            HttpClient client = new DefaultHttpClient();
            File file = new File(imagePath);
            HttpPost post = new HttpPost(Config_Web_API.update_profile_image_api);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addPart("image_type", new StringBody("profile"));
            entityBuilder.addBinaryBody("file", file);
            entityBuilder.addPart("user_id", new StringBody(pUserId));
            // add more key/value pairs here as needed

            HttpEntity entity = entityBuilder.build();
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();

            //Log.d("result", EntityUtils.toString(httpEntity));
            picUpdateResult = EntityUtils.toString(httpEntity);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void dialogVisible()
    {
        sAlertDialog = new SweetAlertDialog(Profile.this, SweetAlertDialog.PROGRESS_TYPE);
        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sAlertDialog.setTitleText("Loading...");
        sAlertDialog.setCancelable(false);
        sAlertDialog.show();
    }


    public void dialogInvisible()
    {
        sAlertDialog.dismiss();
    }


    public void imageUploadSuccess()
    {
        SweetAlertDialog profilePicUpdatedSuccessfully;
        profilePicUpdatedSuccessfully = new SweetAlertDialog(Profile.this, SweetAlertDialog.SUCCESS_TYPE);
        profilePicUpdatedSuccessfully.setTitleText("You Have Successfully Registered");
        profilePicUpdatedSuccessfully.setContentText("Login With Your Credentials");
        profilePicUpdatedSuccessfully.show();
        profilePicUpdatedSuccessfully.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                startActivity(profileIntent);
                finish();
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPath2(Uri selectedImage)
    {

        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(selectedImage);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);



        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }


    public void getReadPermissison()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }else
        {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            if(isKitKat)
            {
                    imagePath = getPath2(image_uri);
                    Log.e(TAG, "image Path :" + imagePath);
            }else
            {

                imagePath = getRealPathFromURI(getApplicationContext(), image_uri);
                Log.e(TAG, "image Path :" + imagePath);
            }

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                // Log.d(TAG, String.valueOf(bitmap));
                openUpdatedProfilePivDialog();
                updatedImageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.e("Permission", "Granted");

                    imagePath = getPath2(image_uri);
                    Log.e(TAG, "image Path :" + imagePath);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                        // Log.d(TAG, String.valueOf(bitmap));
                        openUpdatedProfilePivDialog();
                        updatedImageView.setImageBitmap(bitmap);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

}
