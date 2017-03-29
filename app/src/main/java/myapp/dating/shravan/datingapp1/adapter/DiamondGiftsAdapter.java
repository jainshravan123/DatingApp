package myapp.dating.shravan.datingapp1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import myapp.dating.shravan.datingapp1.R;
import myapp.dating.shravan.datingapp1.activity.UserDashboardProfile;
import myapp.dating.shravan.datingapp1.activity.nav_draw_activity.Gifts;
import myapp.dating.shravan.datingapp1.bean.Config_Web_API;
import myapp.dating.shravan.datingapp1.bean.Gift;
import myapp.dating.shravan.datingapp1.bean.User;

/**
 * Created by admin on 18/08/16.
 */
public class DiamondGiftsAdapter extends RecyclerView.Adapter<DiamondGiftsAdapter.ViewGiftDiamondAdapter>
{

    private ArrayList<Gift> gift_diamond_list;
    private Context context;
    String TAG = "DiamondGiftsAdapter";



    public DiamondGiftsAdapter(ArrayList<Gift> c_gift_diamond_list, Context c_ctx){
        this.gift_diamond_list    = c_gift_diamond_list;
        this.context  = c_ctx;
    }

    @Override
    public ViewGiftDiamondAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_item_layout, parent, false);
        ViewGiftDiamondAdapter avh = new ViewGiftDiamondAdapter(v, context, gift_diamond_list);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewGiftDiamondAdapter holder, int position)
    {

        holder.v_gift_name.setText(gift_diamond_list.get(position).getGift_name());
        holder.v_gift_cost.setText(gift_diamond_list.get(position).getPrice());
        Picasso.with(context).load(Config_Web_API.root_image_uri + gift_diamond_list.get(position).getImage()).into(holder.v_gift_image_view);
       Log.e(TAG, Config_Web_API.all_gifts_api + gift_diamond_list.get(position).getImage());
    }

    @Override
    public int getItemCount()
    {
        return gift_diamond_list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewGiftDiamondAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        //  public TextView v_txtView;
        public ImageView v_gift_image_view;
        public TextView  v_gift_name;
        public TextView  v_gift_cost;
        public Context   v_ctx;
        public ArrayList<Gift> v_gifts = new ArrayList<Gift>();

        public ViewGiftDiamondAdapter(View itemView, Context c_ctx, ArrayList<Gift> c_gift_diamond_list)
        {
            super(itemView);

            v_gift_image_view    = (ImageView) itemView.findViewById(R.id.gift_image);
            v_gift_name          = (TextView) itemView.findViewById(R.id.txtGiftName);
            v_gift_cost          = (TextView) itemView.findViewById(R.id.txtGiftPrice);


            this.v_ctx     = c_ctx;
            this.v_gifts   = c_gift_diamond_list;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

        }

    }


}


