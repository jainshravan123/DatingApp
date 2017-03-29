package myapp.dating.shravan.datingapp1.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.Device;
import myapp.dating.shravan.datingapp1.bean.SignInUser;
import myapp.dating.shravan.datingapp1.bean.User;
import myapp.dating.shravan.datingapp1.session.SessionManager;
import myapp.dating.shravan.datingapp1.sqlite_db.DBHelper;

public class SigningUp extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtConfirmPassword, edtMobileNumber, edtDOB;
    Button   btn_signing_up;
    SweetAlertDialog sign_up_sweet_alert_dialog_process, sign_up_sweet_dialog_success;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_up);


        btn_signing_up     = (Button)   findViewById(R.id.btn_signup);
        edtUsername        = (EditText) findViewById(R.id.input_username);
        edtPassword        = (EditText) findViewById(R.id.input_password);
        edtConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        edtMobileNumber    = (EditText) findViewById(R.id.input_mobile_number);
        edtDOB             = (EditText) findViewById(R.id.input_dob);


        btn_signing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                User user = new User();
                user.setUsername(edtUsername.getText().toString());
                user.setEmail(edtUsername.getText().toString());
                user.setPassword(edtPassword.getText().toString());
                user.setConfirm_password(edtConfirmPassword.getText().toString());
                user.setPhoneNumber(edtMobileNumber.getText().toString());
                user.setDob(edtDOB.getText().toString());


                sign_up_sweet_alert_dialog_process = new SweetAlertDialog(SigningUp.this, SweetAlertDialog.PROGRESS_TYPE);
                sign_up_sweet_alert_dialog_process.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                sign_up_sweet_alert_dialog_process.setTitleText("Authenticating...");
                sign_up_sweet_alert_dialog_process.setCancelable(false);
                sign_up_sweet_alert_dialog_process.show();

                try {
                    signingUpProcess(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }



    public void signingUpProcess(User user) throws JSONException
    {
        String url = Config_Web_API.sign_up_api;


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", user.getUsername());
        jsonObject.put("password", user.getPassword());
        jsonObject.put("email", user.getEmail());
        jsonObject.put("phoneNumber", user.getPhoneNumber());
        jsonObject.put("DOB", user.getDob());
        jsonObject.put("deviceType", "android");
        jsonObject.put("deviceToken", "12345");


        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                            Log.e("SignUp Response : ", response.toString());
                            sign_up_sweet_alert_dialog_process.dismiss();
                            String status = response.getString("status").toString();


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
}
