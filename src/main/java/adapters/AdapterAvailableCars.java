package com.example.root.rsv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.rsv.models.Car;
import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.NestedFragmentRSVs;
import com.example.root.rsv.views.ActivityAvailableCars;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterAvailableCars extends RecyclerView.Adapter<AdapterAvailableCars.ViewHolder> {

    private List<Car> mData;
    private List<Car> tempData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public AdapterAvailableCars(Context context, List<Car> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        tempData = new ArrayList<>();
        tempData.addAll(mData);

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_available_cars, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = mData.get(position).getId();
        String car_name = mData.get(position).getName();
        String car_category = mData.get(position).getCategory();
        String car_license_plate = mData.get(position).getLicense_plate();

        holder.tvCarName.setText(car_name);
        holder.tvCarCategory.setText("Category: " + car_category);
        holder.tvCarLicensePlate.setText(car_license_plate);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCarName,tvCarCategory,tvCarLicensePlate;

        ViewHolder(View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.item_list_available_car_name);
            tvCarCategory = itemView.findViewById(R.id.item_list_available_car_category);
            tvCarLicensePlate = itemView.findViewById(R.id.item_list_available_car_license_plate);

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

        ActivityAvailableCars.list.clear();
        if (charText.length() == 0) {
            for(int i=0;i<tempData.size();i++){
                if(!ActivityAvailableCars.list.contains(tempData.get(i))) {
                    ActivityAvailableCars.list.add(tempData.get(i));
                }
            }
        } else {
            for (Car car : tempData) {
                if(car.getName().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!ActivityAvailableCars.list.contains(car)) {
                        ActivityAvailableCars.list.add(car);
                    }
                }
                else if(car.getCategory().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!ActivityAvailableCars.list.contains(car)) {
                        ActivityAvailableCars.list.add(car);
                    }
                }
                else if(car.getLicense_plate().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!ActivityAvailableCars.list.contains(car)) {
                        ActivityAvailableCars.list.add(car);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }
}

