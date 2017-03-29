package myapp.dating.shravan.datingapp1.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.session.SessionManager;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //getitng the view
        View decorView = getWindow().getDecorView();
        // Hide the status bar
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        //counter for holding splash screens
        new CountDownTimer(3000,1000) {

            @Override
            public void onFinish() {

                SessionManager sessionManager = new SessionManager(getApplicationContext());

                //Checking Session
                if(sessionManager.isLoggedIn()){

                    //Opening Dashboard
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                }else{
                    //Opening User SignIn Activity
                    Intent intent = new Intent(getApplicationContext(), Signin.class);
                    startActivity(intent);
                    finish();
                }


            }


            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }
}
