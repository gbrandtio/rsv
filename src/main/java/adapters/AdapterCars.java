package com.example.root.rsv.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.MainActivity;
import com.example.root.rsv.fragments.FragmentCars;
import com.example.root.rsv.models.Car;
import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.NestedFragmentRSVs;
import com.example.root.rsv.models.SingleCarStats;
import com.example.root.rsv.views.ActivityCategoryCars;
import com.example.root.rsv.views.ActivityEditCar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdapterCars extends RecyclerView.Adapter<AdapterCars.ViewHolder> {

    private List<Car> mData;
    private List<Car> tempData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public AdapterCars(Context context, List<Car> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        tempData = new ArrayList<>();
        tempData.addAll(mData);

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_category_cars, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = mData.get(position).getId();
        String car_name = mData.get(position).getName();
        String kms = mData.get(position).getKms();
        String license_plate = mData.get(position).getLicense_plate();
        String category = mData.get(position).getCategory();
        String total_rsvs = mData.get(position).getTotal_rsvs();


        holder.tvCarName.setText(car_name);
        holder.tvCategory.setText("Category " + category);
        holder.tvKms.setText("Kms: " + kms);
        holder.tvLicensePlate.setText(license_plate);

        holder.btnEditCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCar(car_name,category,kms,license_plate,total_rsvs);
            }
        });


        holder.btnDeleteCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCar(id);
            }
        });

        holder.btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog statsDialog = new Dialog(context);
                statsDialog.setContentView(R.layout.car_stats_popup);
                holder.tvStatsName = (TextView) statsDialog.findViewById(R.id.car_stats_name);
                holder.tvStatsCategory = (TextView) statsDialog.findViewById(R.id.car_stats_category);
                holder.tvYearlyRsvs = (TextView) statsDialog.findViewById(R.id.car_stats_total_rsvs_year);
                holder.tvMonthlyRsvs = (TextView) statsDialog.findViewById(R.id.car_stats_total_rsvs_month);
                holder.tvPreviousService = (TextView) statsDialog.findViewById(R.id.car_stats_previous_service);
                holder.tvNextService = (TextView) statsDialog.findViewById(R.id.car_stats_next_service);
                holder.tvNationalityPercentage = (TextView) statsDialog.findViewById(R.id.nationality_percentage);
                holder.tvCloseDialog = (TextView) statsDialog.findViewById(R.id.car_stats_close_dialog);
                holder.tvCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statsDialog.dismiss();
                    }
                });
                statsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                statsDialog.show();

                getStats(id,holder);
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
        TextView tvCarName,tvKms,tvLicensePlate,tvCategory;
        TextView tvStatsName,tvStatsCategory,tvYearlyRsvs,tvMonthlyRsvs,tvPreviousService,tvNextService,tvNationalityPercentage,tvCloseDialog;
        ImageButton btnEditCar,btnDeleteCar,btnStats,btnService;

        ViewHolder(View itemView) {
            super(itemView);
            tvCarName = (TextView) itemView.findViewById(R.id.item_list_car_name);
            tvCategory = (TextView) itemView.findViewById(R.id.item_list_car_category);
            tvKms = (TextView) itemView.findViewById(R.id.item_list_car_kms);
            tvLicensePlate = (TextView) itemView.findViewById(R.id.item_list_car_license_plate);
            tvCloseDialog = (TextView) itemView.findViewById(R.id.car_stats_close_dialog);
            btnEditCar = (ImageButton) itemView.findViewById(R.id.btn_item_list_edit_car);
            btnDeleteCar = (ImageButton) itemView.findViewById(R.id.btn_item_list_delete_car);
            btnStats = (ImageButton) itemView.findViewById(R.id.btn_item_list_car_stats);
            btnService = (ImageButton) itemView.findViewById(R.id.btn_item_list_car_alarm);

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

    public void getStats(String id,ViewHolder holder){
        String url = context.getResources().getString(R.string.base_url) +
                     context.getResources().getString(R.string.singleCarStats) + id;
        System.out.println("EXECUTED: " + id);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        SingleCarStats stats = new SingleCarStats(
                                jsonObject.getString("id"),jsonObject.getString("name"),
                                jsonObject.getString("category"),jsonObject.getString("kms"),
                                jsonObject.getString("license_plate"),jsonObject.getString("total_rsvs"),
                                jsonObject.getString("image"),jsonObject.getString("monthly_rsvs"),
                                jsonObject.getString("yearly_rsvs"),jsonObject.getString("nationality_percentage"),
                                jsonObject.getString("nam_nationality"),jsonObject.getString("previous_service_date"),
                                jsonObject.getString("upcoming_service_date")
                        );
                        holder.tvStatsName.setText(stats.getName()+"\n"+stats.getLicense_plate());
                        holder.tvStatsCategory.setText("Category: " + stats.getCategory());
                        holder.tvMonthlyRsvs.setText(stats.getMonthly_rsvs());
                        holder.tvYearlyRsvs.setText(stats.getYearly_rsvs());
                        holder.tvPreviousService.setText(stats.getPrevious_service_date());
                        holder.tvNextService.setText(stats.getUpcoming_service_date());
                        holder.tvNationalityPercentage.setText(stats.getNationality_percentage()+"\n"+stats.getNam_nationality());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SiCarSt", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }


    public void editCar(String car_name,String car_category,String car_kms,String car_license_plate,
                        String car_total_rsvs){
        Intent intent = new Intent(context, ActivityEditCar.class);
        intent.putExtra("CAR_NAME",car_name);
        intent.putExtra("CAR_CATEGORY",car_category);
        intent.putExtra("CAR_KMS",car_kms);
        intent.putExtra("CAR_LICENSE_PLATE",car_license_plate);
        intent.putExtra("CAR_TOTAL_RSVS",car_total_rsvs);
        context.startActivity(intent);
    }

    //This function is used to make a post request to the API to delete the given car.
    public void deleteCar(String car_id){
        String url = context.getResources().getString(R.string.base_url) + context.getResources().getString(R.string.deletecar);
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
                        Toast.makeText(context, "Error.The car has not been deleted.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", car_id);
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

        ActivityCategoryCars.list.clear();
        if (charText.length() == 0) {
            for(int i=0;i<tempData.size();i++){
                if(!ActivityCategoryCars.list.contains(tempData.get(i))) {
                    ActivityCategoryCars.list.add(tempData.get(i));
                }
            }
        } else {
            for (Car car : tempData) {
                if (car.getName().toLowerCase(Locale.ENGLISH).contains(charText)) {
                    if(!ActivityCategoryCars.list.contains(car)) {
                        ActivityCategoryCars.list.add(car);
                    }
                }
                else if(car.getLicense_plate().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!ActivityCategoryCars.list.contains(car)) {
                        ActivityCategoryCars.list.add(car);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}

