package com.example.root.rsv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.rsv.fragments.FragmentClients;
import com.example.root.rsv.models.Client;
import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.NestedFragmentRSVs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterClients extends RecyclerView.Adapter<AdapterClients.ViewHolder> {

    private List<Client> mData;
    private List<Client> tempData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public AdapterClients(Context context, List<Client> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        tempData = new ArrayList<>();
        tempData.addAll(mData);

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_clients, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = mData.get(position).getId();
        String client_name = mData.get(position).getName();
        String client_email = mData.get(position).getEmail();
        String client_telephone = mData.get(position).getTelephone();
        String client_nationality = mData.get(position).getNationality();

        holder.tvName.setText(client_name);
        holder.tvEmail.setText(client_email);
        holder.tvTelephone.setText(client_telephone);
        holder.tvNationality.setText(client_nationality);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName,tvEmail,tvTelephone,tvNationality;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_list_client_name);
            tvEmail = (TextView) itemView.findViewById(R.id.item_list_client_email);
            tvTelephone = (TextView) itemView.findViewById(R.id.item_list_client_telephone);
            tvNationality = (TextView) itemView.findViewById(R.id.item_list_client_nationality);

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

        FragmentClients.list.clear();
        if (charText.length() == 0) {
            for(int i=0;i<tempData.size();i++){
                if(!FragmentClients.list.contains(tempData.get(i))) {
                    FragmentClients.list.add(tempData.get(i));
                }
            }
        } else {
            for (Client client : tempData) {
                if (client.getName().toLowerCase(Locale.ENGLISH).contains(charText)) {
                    if(!FragmentClients.list.contains(client)) {
                        FragmentClients.list.add(client);
                    }
                }
                else if(client.getEmail().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!FragmentClients.list.contains(client)) {
                        FragmentClients.list.add(client);
                    }
                }
                else if(client.getNationality().toLowerCase(Locale.ENGLISH).contains(charText)){
                    if(!FragmentClients.list.contains(client)) {
                        FragmentClients.list.add(client);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}

