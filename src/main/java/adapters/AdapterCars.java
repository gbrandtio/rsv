package com.example.root.rsv.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.MainActivity;
import com.example.root.rsv.fragments.FragmentCars;
import com.example.root.rsv.models.Car;
import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.NestedFragmentRSVs;
import com.example.root.rsv.views.ActivityCategoryCars;
import com.example.root.rsv.views.ActivityEditCar;

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
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCarName,tvKms,tvLicensePlate,tvCategory;
        ImageButton btnEditCar,btnDeleteCar;

        ViewHolder(View itemView) {
            super(itemView);
            tvCarName = (TextView) itemView.findViewById(R.id.item_list_car_name);
            tvCategory = (TextView) itemView.findViewById(R.id.item_list_car_category);
            tvKms = (TextView) itemView.findViewById(R.id.item_list_car_kms);
            tvLicensePlate = (TextView) itemView.findViewById(R.id.item_list_car_license_plate);
            btnEditCar = (ImageButton) itemView.findViewById(R.id.btn_item_list_edit_car);
            btnDeleteCar = (ImageButton) itemView.findViewById(R.id.btn_item_list_delete_car);

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
            //NestedFragmentRSVs.list.addAll(tempData);
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

