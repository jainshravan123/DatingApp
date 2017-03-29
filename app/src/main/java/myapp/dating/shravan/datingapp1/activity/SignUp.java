package myapp.dating.shravan.datingapp1.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.SignUpUser;
import myapp.dating.shravan.datingapp1.bean.User;
import myapp.dating.shravan.datingapp1.network.CheckInternetConnection;

public class SignUp extends AppCompatActivity {

     TextView link_login;
     EditText edtUsername, edtPassword, edtConfirmPassword, edtMobileNumber;
     static EditText edtDob;
     SweetAlertDialog signUpAlertDialog, sign_up_sweet_dialog_success;
     Button   signUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //getitng the view
        View decorView = getWindow().getDecorView();
        // Hide the status bar
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        link_login = (TextView) findViewById(R.id.link_login);
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin_intent = new Intent(getApplicationContext(), Signin.class);
                startActivity(signin_intent);
            }
        });

        edtUsername        = (EditText) findViewById(R.id.input_username);
        edtPassword        = (EditText) findViewById(R.id.input_password);
        edtConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        edtMobileNumber    = (EditText) findViewById(R.id.input_mobile_number);
        edtDob             = (EditText) findViewById(R.id.input_dob);
        signUpBtn          = (Button) findViewById(R.id.btn_signup);

        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String confirm_password = edtConfirmPassword.getText().toString();
                String mobile_number = edtMobileNumber.getText().toString();
                String dob   = edtDob.getText().toString();


                Log.e("(1)SignUp Username : ", username);
                Log.e("(1)SignUp Password : ", password);
                Log.e("(1)Confirm Password : ", confirm_password);
                Log.e("(1)Mobile Number    : ", mobile_number);
                Log.e("(1)DOB              : ", dob);

                SignUpUser signUpUser = new SignUpUser();
                User s_user = new User();
                s_user.setUsername(username);
                s_user.setPassword(password);
                s_user.setEmail(username);
                s_user.setConfirm_password(confirm_password);
                s_user.setPhoneNumber(mobile_number);
                s_user.setDob(dob);
                signUpUser.setUser(s_user);

                //Checking Internet Connection
                CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                checkInternetConnection.showNetworkIdentifier(getApplicationContext(), SignUp.this);

                if (checkInternetConnection.checkingInternetConnection(getApplicationContext())) {

                    if (checkSignUpFields(signUpUser.getUser())) {

                        if (checkPasswordEquality(signUpUser.getUser())) {

                            signUpAlertDialog = new SweetAlertDialog(SignUp.this, SweetAlertDialog.PROGRESS_TYPE);
                            signUpAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            signUpAlertDialog.setTitleText("Authenticating...");
                            signUpAlertDialog.setCancelable(false);
                            signUpAlertDialog.show();

                            //calling function for signing process
                            try {
                                signUpProcess(signUpUser);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            new SweetAlertDialog(SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Wrong Password")
                                    .setContentText("Confirm Password must same as Password")
                                    .show();
                        }
                    } else {

                        String msg = checkSignUpFieldsErrorMsg(signUpUser.getUser());
                        new SweetAlertDialog(SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(msg)
                                .show();
                    }
                }

            }
        });

    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            //txt1.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(monthOfYear)+"/"+String.valueOf(year) );
            edtDob.setText(String.valueOf(dayOfMonth) + " - " + String.valueOf(monthOfYear+1)+" - "+String.valueOf(year) );
        }
    }



    //Sign Up Process using Volley Library
    public void signUpProcess(SignUpUser sign_up_user) throws JSONException {

      String url = Config_Web_API.sign_up_api;


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", edtUsername.getText().toString());
        jsonObject.put("password", edtConfirmPassword.getText().toString());
        jsonObject.put("email",    edtUsername.getText().toString());
        jsonObject.put("phoneNumber", edtMobileNumber.getText().toString());
        jsonObject.put("DOB", edtDob.getText().toString());
        jsonObject.put("deviceType", "android");
        jsonObject.put("deviceToken", "12345");


        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {


                            Log.e("Sign Up Response : ", response.toString());

                            signUpAlertDialog.dismiss();
                            String status = response.getString("status");

                            if(status.equals("success"))
                            {

                                JSONObject jsonObject1 = response.getJSONObject("data");
                                String username = jsonObject1.getString("userName");
                                String password = jsonObject1.getString("password");
                                String email    = jsonObject1.getString("email");
                                String phoneNumber = jsonObject1.getString("phoneNumber");
                                String DOB         = jsonObject1.getString("DOB");
                                String user_id     = jsonObject1.getString("user_id");
                                String isActive    = jsonObject1.getString("isActive");
                                String deviceType  = jsonObject1.getString("deviceType");
                                String deviceToken = jsonObject1.getString("deviceToken");
                                String _id         = jsonObject1.getString("_id");



                                sign_up_sweet_dialog_success = new SweetAlertDialog(SignUp.this, SweetAlertDialog.SUCCESS_TYPE);
                                sign_up_sweet_dialog_success.setTitleText("You Have Successfully Registered");
                                sign_up_sweet_dialog_success.setContentText("Login With Your Credentials");
                                sign_up_sweet_dialog_success.show();

                                sign_up_sweet_dialog_success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent dashboardIntent = new Intent(getApplicationContext(), Signin.class);
                                        startActivity(dashboardIntent);
                                    }
                                });

                            }else{

                               String errMsg = response.getString("errMsg");
                                new SweetAlertDialog(SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(errMsg)
                                        .setContentText("Try Again")
                                        .show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);


}







    //Checking Empty Fields Error Message in Sign Up Activity
    public String checkSignUpFieldsErrorMsg(User user){

        String msg = "";

        if(user.getUsername().equals("") && user.getPassword().equals("") && user.getConfirm_password().equals("") && user.getPhoneNumber().equals("") && user.getDob().equals("") ){
            msg = "Please fill all of the fields";
        }
        else if(user.getUsername().equals("")){
            msg = "Username is empty";
        }else if(user.getPassword().equals("")){
            msg = "Password is empty";
        }
        else if(user.getConfirm_password().equals("")){
            msg = "Confirm Password is empty";
        }
        else if(user.getPhoneNumber().equals("")){
            msg = "Contact Number is empty";
        }
        else if(user.getDob().equals("")){
            msg = "DOB is empty";
        }
        return msg;
    }

    //Checking Whether the SignUpUser fields are empty or not
    public boolean checkSignUpFields(User user){
        if(user.getUsername().equals("") || user.getPassword().equals("") || user.getConfirm_password().equals("") || user.getPhoneNumber().equals("") || user.getDob().equals(""))
        {
            return false;
        }
        return true;
    }

    //Checking Whether Password and Confirm Password are same or not
    public boolean checkPasswordEquality(User user){
        if(user.getPassword().equals(user.getConfirm_password())){
            return true;
        }
        return false;
    }


}
