package com.example.root.rsv.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.NestedFragmentRSVs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.ViewHolder> {

    private List<RSV> mData;
    private List<RSV> tempData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public AdapterFeed(Context context, List<RSV> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        tempData = new ArrayList<>();
        tempData.addAll(mData);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_lists_rsvs_feed, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = mData.get(position).getId();
        String car_name = mData.get(position).getCar_id();
        String date = mData.get(position).getDay_start();
        date = date + " / " + mData.get(position).getDay_end();
        String client = mData.get(position).getClient_id();
        String date_start = mData.get(position).getDay_start();
        String date_end = mData.get(position).getDay_end();

        holder.tvRsvId.setText(id);
        holder.tvCar.setText(car_name);
        holder.tvClient.setText(client);
        holder.tvDate.setText(date);

        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if(date_start.equals(formatter.format(today).toString()) || date_start.equals(formatter1.format(tomorrow))){
            holder.tvStatus.setText("STARTING");
            holder.tvStatus.setTextColor(Color.GREEN);
        }
        else {
            holder.tvStatus.setTextColor(Color.RED);
            holder.tvStatus.setText("ENDING");
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvRsvId,tvCar,tvDate,tvClient,tvStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvRsvId = itemView.findViewById(R.id.label_rsv_id);
            tvCar  = itemView.findViewById(R.id.label_rsv_car);
            tvDate = itemView.findViewById(R.id.label_rsv_date);
            tvClient = itemView.findViewById(R.id.label_rsv_client);
            tvStatus = itemView.findViewById(R.id.label_rsv_status);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id).getId();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        NestedFragmentRSVs.list.clear();
        if (charText.length() == 0) {
            NestedFragmentRSVs.list.addAll(tempData);
        } else {
            for (RSV rsv : tempData) {
                if(rsv.getId().toLowerCase(Locale.ENGLISH).contains(charText)){
                    NestedFragmentRSVs.list.add(rsv);
                }
                else if (rsv.getCar_id().toLowerCase(Locale.ENGLISH).contains(charText)) {
                    NestedFragmentRSVs.list.add(rsv);
                }
                else if(rsv.getClient_id().toLowerCase(Locale.ENGLISH).contains(charText)){
                    NestedFragmentRSVs.list.add(rsv);
                }
                else if(rsv.getDay_start().toLowerCase(Locale.ENGLISH).contains(charText)){
                    NestedFragmentRSVs.list.add(rsv);
                }
                else if(rsv.getDay_end().toLowerCase(Locale.ENGLISH).contains(charText)){
                    NestedFragmentRSVs.list.add(rsv);
                }
            }
        }
        notifyDataSetChanged();
    }
}

