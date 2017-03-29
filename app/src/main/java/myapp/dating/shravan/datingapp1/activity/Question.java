package myapp.dating.shravan.datingapp1.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import myapp.dating.shravan.datingapp1.adapter.QuestionAdapter;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.Device;
import myapp.dating.shravan.datingapp1.bean.QuestionAsked;
import myapp.dating.shravan.datingapp1.bean.SignInUser;
import myapp.dating.shravan.datingapp1.bean.User;
import myapp.dating.shravan.datingapp1.session.SessionManager;
import myapp.dating.shravan.datingapp1.sqlite_db.DBHelper;


public class Question extends Activity {

    private TextView skipText;

    ArrayList<QuestionAsked> question_asked_list;
    ArrayList<QuestionAsked> question_asked_list_temp;
    LinearLayout linearLayout;
    RecyclerView recyclerView1;
    HorizontalScrollView horizontalScrollView;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    ProgressBar prgBar1;
    Button btnSubmit;
    int rowSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question_asked_list           = new ArrayList<QuestionAsked>();
        question_asked_list_temp      = new ArrayList<QuestionAsked>();
        recyclerView1                 = (RecyclerView) findViewById(R.id.recycleView1);
        linearLayout                  = (LinearLayout) findViewById(R.id.linearScroll1);
        horizontalScrollView          = (HorizontalScrollView) findViewById(R.id.scroll);
        skipText                      = (TextView)     findViewById(R.id.textSkip);
        prgBar1                       = (ProgressBar)  findViewById(R.id.prgBar1);
        btnSubmit                     = (Button) findViewById(R.id.btnSubmit);


        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent1);

            }
        });


        getQuestionFromWeb();



    }


    public void getQuestionFromWeb(){

        String url = Config_Web_API.question_api;

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!

                        Log.e("Question", response.toString());
                        try {

                            String status = response.getString("status").toString();

                            if(status.equals("success")){

                                prgBar1.setVisibility(View.GONE);
                                recyclerView1.setVisibility(View.VISIBLE);
                                horizontalScrollView.setVisibility(View.VISIBLE);
                                btnSubmit.setVisibility(View.VISIBLE);

                                JSONArray jsonArray = response.getJSONArray("data");
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String qus_id = jsonObject.getString("_id");
                                    String qus    = jsonObject.getString("quest");

                                    Log.d("Questions : ", "("+qus_id + ")("+qus+")");
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("ans");
                                    ArrayList<String> ans_ist = new ArrayList<String>();
                                    for(int j=0; j< jsonArray1.length(); j++){
                                        ans_ist.add(String.valueOf(jsonArray1.get(j)));
                                    }

                                    QuestionAsked questionAsked = new QuestionAsked();
                                    questionAsked.setId(qus_id);
                                    questionAsked.setQuestion(qus);
                                    questionAsked.setAnswer(ans_ist);
                                    question_asked_list.add(questionAsked);
                                }

                                startFurtherProcess();

                            }else{

                                String errMsg = response.getString("errMsg").toString();
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


    public void startFurtherProcess(){
        int rem = question_asked_list.size() % rowSize;


        if(rem > 0){

            for(int i=0; i< rowSize - rem; i++ ){

                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add("");

                QuestionAsked questionAsked = new QuestionAsked();
                questionAsked.setQuestion("");
                questionAsked.setId("");
                questionAsked.setAnswer(arrayList);


                question_asked_list.add(questionAsked);

            }
        }

        addItem(0);

        int size = question_asked_list.size() / rowSize;

        for(int j= 0; j< size; j++){
            final int k;
            k=j;
            final Button btnPage = new Button(Question.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 2, 2 ,2);
            btnPage.setTextColor(Color.WHITE);
            btnPage.setTextSize(20.0f);
            btnPage.setId(j);
            btnPage.setText(String.valueOf(j + 1));
            btnPage.setBackgroundColor(Color.GRAY);
            btnPage.setTextColor(Color.WHITE);
            linearLayout.addView(btnPage, lp);


            btnPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItem(k);
                }
            });
        }
    }

    public void addItem(int i){
        question_asked_list_temp.clear();
        i= i * rowSize;
        for(int j=0; j< rowSize; j++){
            question_asked_list_temp.add(j, question_asked_list.get(i));
            i = i + 1;
        }

        setView();
    }

    public void setView(){
        recyclerView1.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        recyclerView1.setLayoutManager(mStaggeredLayoutManager);
        QuestionAdapter questionAdapter = new QuestionAdapter(question_asked_list_temp, getApplicationContext());
        recyclerView1.setAdapter(questionAdapter);
    }

}
