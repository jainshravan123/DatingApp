package myapp.dating.shravan.datingapp1.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.activity.UserDashboardProfile;
import myapp.dating.shravan.datingapp1.bean.User;

/**
 * Created by admin on 09/08/16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewUserAdapter>
{

    private ArrayList<User> users;
    private Context         context;
    private Activity        activity;
    private User            singleUser;

    public UserAdapter(ArrayList<User> c_users, Context c_ctx, Activity c_activity){
        this.users    = c_users;
        this.context  = c_ctx;
        this.activity = c_activity;
        singleUser = new User();
    }

    @Override
    public ViewUserAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view_item, parent, false);
        ViewUserAdapter avh = new ViewUserAdapter(v, context, users);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewUserAdapter holder, int position)
    {

        if(!users.get(position).getFirstname().equals(""))
        {
            holder.v_UserTxtView.setText(users.get(position).getFirstname());
        }


        if(users.get(position).getIsActive().equals("true"))
        {
            holder.v_UserOnlineImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.v_UserOnlineImageView.setVisibility(View.INVISIBLE);
        }

        if(!users.get(position).getImageUrl().equals(""))
        {
            Picasso.with(context).load(users.get(position).getImageUrl()).into(holder.v_ProfileImageView);
        }

    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewUserAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        //  public TextView v_txtView;
        public ImageView v_ProfileImageView;
        public ImageView v_UserOnlineImageView;
        public TextView  v_UserTxtView;
        public CardView  v_UserCardView;
        public Context   v_ctx;
        public ArrayList<User> v_users = new ArrayList<User>();

        public ViewUserAdapter(View itemView, Context c_ctx, ArrayList<User> c_users)
        {
            super(itemView);

            v_ProfileImageView    = (ImageView) itemView.findViewById(R.id.user_profile_image);
            v_UserOnlineImageView = (ImageView) itemView.findViewById(R.id.user_online_icon);
            v_UserTxtView         = (TextView) itemView.findViewById(R.id.user_name);
            v_UserCardView        = (CardView) itemView.findViewById(R.id.userCard);


            this.v_ctx     = c_ctx;
            this.v_users = c_users;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            openUserDescDialog(position);


        }

    }

    //Opening Profile Picture Dialog
    public void openUserDescDialog(int pos)
    {


        singleUser.setUser_id(users.get(pos).getUser_id());
        singleUser.setFirstname(users.get(pos).getFirstname());
        singleUser.setLastname(users.get(pos).getLastname());
        singleUser.setEmail(users.get(pos).getEmail());
        singleUser.setUsername(users.get(pos).getUsername());
        singleUser.setImageUrl(users.get(pos).getImageUrl());



        if(users.get(pos).getFirstname().equals("")) {
            singleUser.setFirstname("Unknown");
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                    .title(singleUser.getFirstname() + " " + singleUser.getLastname())
                    .titleColor(Color.BLACK)
                    .customView(R.layout.user_profile_dialog_layout, true)
                    .positiveText("More Info")
                    .negativeText("Cancel")
                    .positiveColorRes(R.color.colorPrimary)
                    .negativeColorRes(R.color.gray_btn_bg_color)
                    .canceledOnTouchOutside(false)
                    .autoDismiss(false);


        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        CircleImageView imageView      = (CircleImageView) view1.findViewById(R.id.profilePicImageView);


        if(singleUser.getImageUrl().equals("NA") || singleUser.getImageUrl().equals("")){
            imageView.setImageResource(R.drawable.student);
        }else{
            Picasso.with(context).load(singleUser.getImageUrl()).into(imageView);

        }

        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });

        View positive  = materialDialog.getActionButton(DialogAction.POSITIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                Intent intent1 = new Intent(context, UserDashboardProfile.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("user_id", singleUser.getUser_id());
                context.startActivity(intent1);
            }
        });

    }

}


