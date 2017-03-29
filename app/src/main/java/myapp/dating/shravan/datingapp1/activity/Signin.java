package myapp.dating.shravan.datingapp1.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.Device;
import myapp.dating.shravan.datingapp1.bean.SignInUser;
import myapp.dating.shravan.datingapp1.bean.User;
import myapp.dating.shravan.datingapp1.network.CheckInternetConnection;
import myapp.dating.shravan.datingapp1.session.SessionManager;
import myapp.dating.shravan.datingapp1.sqlite_db.DBHelper;

public class Signin extends AppCompatActivity{


    EditText editTxt1, editTxt2;
    Button   signInButton;
    Button   socialLoginBtn, fb_login_btn, google_login_btn, twitter_login_btn;
    SweetAlertDialog sAlertDialog;
    String TAG = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


            //getitng the view
            View decorView = getWindow().getDecorView();
            // Hide the status bar
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);


        TextView link_signup = (TextView) findViewById(R.id.link_signup);

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signup_intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(signup_intent);
            }
        });





/*
        //Getting Screen Size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        //Setting Linear Layout position
        LinearLayout sign_in_back_linear_layout = (LinearLayout) findViewById(R.id.sign_in_back_linear_layout);
        //sign_in_back_linear_layout.setMinimumHeight((deviceHeight * 90) / 100);
        sign_in_back_linear_layout.getLayoutParams().height = (deviceHeight * 90) / 100;

        ImageView imageView = (ImageView) findViewById(R.id.imageLogo);
        imageView.getLayoutParams().height = (deviceHeight * 20) / 100;
        //imageView.setMaxHeight((deviceHeight * 50) / 100);

*/
        editTxt1       = (EditText) findViewById(R.id.input_username);
        editTxt2       = (EditText) findViewById(R.id.input_password);
        signInButton   = (Button) findViewById(R.id.btn_login);
       // socialLoginBtn = (Button) findViewById(R.id.btn_scoial_login);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTxt1.getText().toString();
                String password = editTxt2.getText().toString();


                SignInUser signInUser = new SignInUser();
                User             user = new User();
                user.setUsername(username);
                user.setPassword(password);
                signInUser.setUser(user);


                //Checking Internet Connection for SignIn Activity
                CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                checkInternetConnection.showNetworkIdentifier(getApplicationContext(), Signin.this);


                if(checkInternetConnection.checkingInternetConnection(getApplicationContext()))
                {
                    if(checkSignInFields(signInUser.getUser()))
                    {
                        sAlertDialog = new SweetAlertDialog(Signin.this, SweetAlertDialog.PROGRESS_TYPE);
                        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sAlertDialog.setTitleText("Authenticating...");
                        sAlertDialog.setCancelable(false);
                        sAlertDialog.show();

                        //calling function for signing process
                        try {

                            signInProcess(signInUser);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        String msg = checkSignInFieldsErrorMsg(signInUser.getUser());
                        new SweetAlertDialog(Signin.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(msg)
                                .show();
                    }
                }

            }
        });


      /*  socialLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialLoginDialog();
            }
        });
*/
    }


    //Sign In Process using Volley
    public void signInProcess(final SignInUser sign_in_user) throws JSONException {

        String url = Config_Web_API.sign_in_api;

        User s_user = sign_in_user.getUser();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", s_user.getUsername());
        jsonObject.put("password", s_user.getPassword());



        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            sAlertDialog.dismiss();
                            String status = response.getString("status").toString();


                            if(status.equals("success"))
                            {

                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);


                                String _id          = jsonObject1.getString("_id");
                                String userName     = jsonObject1.getString("userName");
                                String password     = jsonObject1.getString("password");
                                String email        = jsonObject1.getString("email");
                                String phoneNumber  = jsonObject1.getString("phoneNumber");
                                String dob          = jsonObject1.getString("DOB");
                                String user_id      = jsonObject1.getString("user_id");
                                String isActive     = String.valueOf(jsonObject1.getBoolean("isActive"));
                                String deviceType   = jsonObject1.getString("deviceType");
                                String deviceToken  = jsonObject1.getString("deviceToken");



                                SignInUser signInUser = new SignInUser();

                                User user             = new User();
                                user.setUsername(userName);
                                user.setPassword(password);
                                user.setEmail(email);
                                user.setPhoneNumber(phoneNumber);
                                user.setDob(dob);
                                user.setUser_id(user_id);
                                user.setIsActive(isActive);

                                Device device = new Device();
                                device.setDeviceType(deviceType);
                                device.setDeviceToken(deviceToken);

                                sign_in_user.setUser(user);
                                sign_in_user.setDevice(device);

                                Log.e(TAG, "User ID (Object) : " + sign_in_user.getUser().getUser_id());

                                //Storing User Data into SQLite
                                DBHelper dbHelper = new DBHelper(getApplicationContext());
                                dbHelper.addUserData(sign_in_user.getUser());

                                //Set session
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.setLogin(true);

                                //Start Dashboard Activity
                                Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(dashboardIntent);
                                finish();

                            }else{

                                //Getting Error Message
                                String errorMsg = response.getString("errMsg");


                                new SweetAlertDialog(Signin.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(errorMsg)
                                        .setContentText("Try Again")
                                        .show();

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



    //Checking Empty Fields Error message in Signin Activity
    public String checkSignInFieldsErrorMsg(User user){

        String msg = "";
        if((user.getUsername().equals("")) && (user.getPassword().equals(""))){
            msg = "Please fill both of the fields";
        }
        else if((user.getUsername().equals(""))){
            msg = "Username is empty";
        }
        else if((user.getPassword().equals(""))){
            msg = "Password is empty";
        }

        return msg;
    }

    //Checking Whether the SignIn fields are empty or not
    public boolean checkSignInFields(User user){
        if(user.getUsername().equals("") || user.getPassword().equals(""))
        {
            return false;
        }
        return true;
    }


    //to get first letter of each word capitalize
    public String getCapitalizeString(String str){
        StringBuffer stringbf = new StringBuffer();
        Matcher m = Pattern.compile("([a-z])([a-z]*)",
                Pattern.CASE_INSENSITIVE).matcher(str);
        while (m.find()) {
            m.appendReplacement(stringbf,
                    m.group(1).toUpperCase() + m.group(2).toLowerCase());
        }
        return m.appendTail(stringbf).toString();
    }


    public void openSocialLoginDialog()
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(Signin.this)
                .title("Social Login")
                .titleColor(Color.BLACK)
                .customView(R.layout.custom_dialog_social_login, true)
                .negativeText("Cancel")
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();



        View view1        = materialDialog.getCustomView();
        fb_login_btn      = (Button) view1.findViewById(R.id.fb_login_btn);
        google_login_btn  = (Button) view1.findViewById(R.id.google_login_btn);
        twitter_login_btn = (Button) view1.findViewById(R.id.twitter_login_btn);



        fb_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "FB Login Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        google_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Google Login Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        twitter_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Twitter Login Clicked", Toast.LENGTH_SHORT).show();
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




}
