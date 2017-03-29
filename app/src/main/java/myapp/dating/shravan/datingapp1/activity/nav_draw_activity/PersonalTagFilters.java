package myapp.dating.shravan.datingapp1.activity.nav_draw_activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import myapp.dating.shravan.datingapp1.R;

public class PersonalTagFilters extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_tag_filters);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent1);*/
                finish();
            }
        });
        getSupportActionBar().setTitle("Personal Tags Filters");

    }
}
