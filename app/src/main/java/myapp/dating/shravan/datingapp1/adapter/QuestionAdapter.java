package myapp.dating.shravan.datingapp1.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.bean.QuestionAsked;

/**
 * Created by admin on 24/07/16.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewQuestionAdapter>
{

    private ArrayList<QuestionAsked> question_asked_list;
    private Context context;


    public QuestionAdapter(ArrayList<QuestionAsked> c_question_asked_list, Context c_ctx){
        this.question_asked_list = c_question_asked_list;
        this.context = c_ctx;
    }

    @Override
    public ViewQuestionAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_qus_item, parent, false);
        ViewQuestionAdapter avh = new ViewQuestionAdapter(v, context, question_asked_list);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewQuestionAdapter holder, int position)
    {
        holder.v_qusTxt.setText(question_asked_list.get(position).getQuestion());

        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < question_asked_list.get(position).getAnswer().size(); i++) {
            lables.add(question_asked_list.get(position).getAnswer().get(i));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        holder.v_ans_spinner.setAdapter(spinnerAdapter);

    }

    @Override
    public int getItemCount() {
        return question_asked_list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewQuestionAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{



        public TextView v_qusTxt;
        public Spinner  v_ans_spinner;
        public Context  v_ctx;
        public Button   v_sub_btn;
        public ArrayList<QuestionAsked> v_question_asked_list = new ArrayList<QuestionAsked>();

        public ViewQuestionAdapter(View itemView, Context c_ctx, ArrayList<QuestionAsked> c_question_asked_list) {
            super(itemView);
            v_qusTxt      = (TextView) itemView.findViewById(R.id.question);
            v_ans_spinner = (Spinner)  itemView.findViewById(R.id.ansSpinner);
            //v_sub_btn     = (Button)   itemView.findViewById(R.id.submitAnsBtn);


            this.v_ctx     = c_ctx;
            this.v_question_asked_list = c_question_asked_list;

            int position1 = getAdapterPosition();
           if(v_question_asked_list.size() == (position1 + 1)){
               Toast.makeText(c_ctx, "Matched", Toast.LENGTH_LONG).show();
           }

            Log.e("Adapter Postion1 : ", String.valueOf(position1));
            Log.e("Size of List     : ", String.valueOf(v_question_asked_list.size()));


            itemView.setOnClickListener(this);
            //v_cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
           Log.e("Clicked : ", String.valueOf(position));
            String module_name = v_question_asked_list.get(position).getQuestion();
            Toast.makeText(context, "Clicked (Module Name) : " + module_name, Toast.LENGTH_LONG).show();



        }

    }
}



