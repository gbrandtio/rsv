package com.example.root.rsv.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.MainActivity;
import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.NestedFragmentRSVs;
import com.example.root.rsv.views.ActivitySetDates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdapterRSVs extends RecyclerView.Adapter<AdapterRSVs.ViewHolder> {

    private List<RSV> mData;
    private List<RSV> tempData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public AdapterRSVs(Context context, List<RSV> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
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
        String car_id = mData.get(position).getCar_id();
        String car_name = mData.get(position).getCar_name();
        String date = mData.get(position).getDay_start();
        date = date + " / " + mData.get(position).getDay_end();
        String client_id = mData.get(position).getClient_id();
        String client = mData.get(position).getClient_name();
        String time = mData.get(position).getHour_start();
        time = time + " / " + mData.get(position).getHour_end();
        String day_start = mData.get(position).getDay_start();
        String hour_start = mData.get(position).getHour_start();
        String day_end = mData.get(position).getDay_end();
        String hour_end = mData.get(position).getHour_end();
        String total_money = mData.get(position).getTotal_money();

        holder.tvRsvId.setText(id);
        holder.tvCar.setText(car_name);
        holder.tvClient.setText(client);
        holder.tvDate.setText(date);
        holder.tvTime.setText(time);

        holder.btnArchiveRSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archiveRSV(id,car_id,client_id,day_start,hour_start,day_end,hour_end,total_money);
            }
        });

        holder.btnEditRSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRSV(client_id,id);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvRsvId,tvCar,tvDate,tvTime,tvClient;
        ImageButton btnEditRSV,btnArchiveRSV;

        ViewHolder(View itemView) {
            super(itemView);
            tvRsvId = itemView.findViewById(R.id.label_rsv_id);
            tvCar  = itemView.findViewById(R.id.label_rsv_car);
            tvDate = itemView.findViewById(R.id.label_rsv_date);
            tvClient = itemView.findViewById(R.id.label_rsv_client);
            tvTime = itemView.findViewById(R.id.label_rsv_time);
            btnEditRSV = itemView.findViewById(R.id.btn_rsv_edit);
            btnArchiveRSV = itemView.findViewById(R.id.btn_rsv_archive);

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

    //This function is used to edit the selected reservation.
    public void editRSV(String client_id,String rsv_id){
        Intent intent = new Intent(context,ActivitySetDates.class);
        intent.putExtra("EDIT_RSV","YES");
        intent.putExtra("EDIT_CLIENT_ID",client_id);
        intent.putExtra("EDIT_RSV_ID",rsv_id);
        context.startActivity(intent);
    }

    //This function is used to make a post request to the API to archive the selected RSV.
    public void archiveRSV(String id,String car_id,String client_id,String day_start,String hour_start,String day_end,
                          String hour_end,String total_money){
        String url = context.getResources().getString(R.string.base_url) + context.getResources().getString(R.string.archiveReservation);
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("SUCCESS","YES");
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Showing error message if something goes wrong.
                        Toast.makeText(context, "Error.The reservation has not been deleted.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("car_id",car_id);
                params.put("client_id",client_id);
                params.put("day_start",day_start);
                params.put("hour_start",hour_start);
                params.put("day_end",day_end);
                params.put("hour_end",hour_end);
                params.put("total_money",total_money);

                return params;
            }

        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        tempData.addAll(mData);
        NestedFragmentRSVs.list.clear();
        if (charText.length() == 0) {
            for(int i=0;i<tempData.size();i++){
                if(!NestedFragmentRSVs.list.contains(tempData.get(i))) {
                    NestedFragmentRSVs.list.add(tempData.get(i));
                }
            }
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
