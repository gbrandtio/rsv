package com.example.root.rsv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.adapters.AdapterFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author: Ioannis Brant Ioannidis
 * @email: giannisbr@gmail.com
 * @date: 15/10/2019
 */
public class NestedFragmentFeed  extends Fragment{
    private static final String TAG = NestedFragmentFeed .class.getName();
    private static final int LOADER_ID = 477;


    private static String response,urlToday,urlTomorrow;
    private Context context;
    public static ArrayList<RSV> list,list1,list2;
    AdapterFeed adapter,adapter2;
    ProgressBar progressBar;



    public NestedFragmentFeed(){}

    public static NestedFragmentFeed newInstance() {
        return new NestedFragmentFeed();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = Objects.requireNonNull(getActivity());
        urlToday = context.getResources().getString(R.string.base_url)+context.getResources().getString(R.string.todayReservations);
        urlTomorrow = context.getResources().getString(R.string.base_url)+context.getResources().getString(R.string.tomorrowReservations);

        // ButterKnife.bind(this,Objects.requireNonNull(getView()));

        progressBar = view.findViewById(R.id.progressBar);
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();


        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapter = new AdapterFeed(context, list1);
        recyclerView.setAdapter(adapter);

        // set up the RecyclerView
        RecyclerView recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapter2 = new AdapterFeed(context, list2);
        recyclerView2.setAdapter(adapter2);

        getTodayData();
        getTomorrowData();

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.nested_fragment_feed, container, false);

        return v;
    }

    private void getTodayData() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlToday, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        RSV rsv= new RSV(
                                jsonObject.getString("id"),jsonObject.getString("car_name"),
                                jsonObject.getString("car_name"),jsonObject.getString("client_id"),
                                jsonObject.getString("client_name"),jsonObject.getString("day_start"),
                                jsonObject.getString("hour_start"),jsonObject.getString("day_end"),
                                jsonObject.getString("hour_end"),jsonObject.getString("total_money")
                        );
                        list1.add(rsv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RSVs", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    private void getTomorrowData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlTomorrow, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        RSV rsv= new RSV(
                                jsonObject.getString("id"),jsonObject.getString("car_name"),
                                jsonObject.getString("car_name"),jsonObject.getString("client_id"),
                                jsonObject.getString("client_name"),jsonObject.getString("day_start"),
                                jsonObject.getString("hour_start"),jsonObject.getString("day_end"),
                                jsonObject.getString("hour_end"),jsonObject.getString("total_money")
                        );
                        list2.add(rsv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter2.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RSVs", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }




}



