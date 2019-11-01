package com.example.root.rsv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.NestedFragmentRSVs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterRSVs extends RecyclerView.Adapter<AdapterRSVs.ViewHolder> {

    private List<RSV> mData;
    private List<RSV> tempData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public AdapterRSVs(Context context, List<RSV> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        tempData = new ArrayList<>();
        tempData.addAll(mData);

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_reservations, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = mData.get(position).getId();
        String car_name = mData.get(position).getCar_id();
        String date = mData.get(position).getDay_start();
        date = date + " / " + mData.get(position).getDay_end();
        String client = mData.get(position).getClient_name();

        holder.tvRsvId.setText(id);
        holder.tvCar.setText(car_name);
        holder.tvClient.setText(client);
        holder.tvDate.setText(date);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvRsvId,tvCar,tvDate,tvClient;

        ViewHolder(View itemView) {
            super(itemView);
            tvRsvId = itemView.findViewById(R.id.label_rsv_id);
            tvCar  = itemView.findViewById(R.id.label_rsv_car);
            tvDate = itemView.findViewById(R.id.label_rsv_date);
            tvClient = itemView.findViewById(R.id.label_rsv_client);

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
        tempData.addAll(mData);
       /* for(int i=0;i<mData.size();i++){
            if(!tempData.contains(mData.get(i))){
                tempData.add(mData.get(i));
            }
        }*/
        NestedFragmentRSVs.list.clear();
        if (charText.length() == 0) {
            for(int i=0;i<tempData.size();i++){
                if(!NestedFragmentRSVs.list.contains(tempData.get(i))) {
                    NestedFragmentRSVs.list.add(tempData.get(i));
                }
            }
            //NestedFragmentRSVs.list.addAll(tempData);
        } else {
            for (RSV rsv : tempData) {
                if(rsv.getId().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!NestedFragmentRSVs.list.contains(rsv)) {
                        NestedFragmentRSVs.list.add(rsv);
                    }
                }
                else if (rsv.getCar_name().toLowerCase(Locale.ENGLISH).contains(charText)) {
                    if(!NestedFragmentRSVs.list.contains(rsv)) {
                        NestedFragmentRSVs.list.add(rsv);
                    }
                }
                else if(rsv.getClient_name().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!NestedFragmentRSVs.list.contains(rsv)) {
                        NestedFragmentRSVs.list.add(rsv);
                    }
                }
                else if(rsv.getDay_start().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!NestedFragmentRSVs.list.contains(rsv)) {
                        NestedFragmentRSVs.list.add(rsv);
                    }
                }
                else if(rsv.getDay_end().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!NestedFragmentRSVs.list.contains(rsv)) {
                        NestedFragmentRSVs.list.add(rsv);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}