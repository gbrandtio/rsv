package com.example.root.rsv.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.R;
import com.example.root.rsv.adapters.AdapterAvailableCars;
import com.example.root.rsv.adapters.AdapterRSVs;
import com.example.root.rsv.models.Car;
import com.example.root.rsv.models.RSV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityAvailableCars extends AppCompatActivity implements SearchView.OnQueryTextListener,AdapterAvailableCars.ItemClickListener{

    public static ArrayList<Car> list,list1;
    private String url;
    private ProgressBar progressBar;
    private Context context;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    AdapterAvailableCars adapter;
    private String day_start,hour_start,day_end,hour_end;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_cars);

        progressBar = findViewById(R.id.progressBar);
        context=this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        day_start = intent.getStringExtra("DATE_START");
        hour_start = intent.getStringExtra("START_HOUR");
        day_end = intent.getStringExtra("DATE_END");
        hour_end = intent.getStringExtra("END_HOUR");

        url = context.getResources().getString(R.string.base_url)
                +context.getResources().getString(R.string.returnAvailableCars)+day_start
                +context.getResources().getString(R.string.param_hour_start)+hour_start
                +context.getResources().getString(R.string.param_day_end)+day_end
                +context.getResources().getString(R.string.param_hour_end)+hour_end;

        System.out.println(url);

        // data to populate the RecyclerView with
        list = new ArrayList<>();
        list1 = new ArrayList<>();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AdapterAvailableCars(context, list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint("Search by car name,category or license plate");
        searchView.setOnQueryTextListener(this);

        getData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    public ArrayList<Car> populateList(){
        for(int i = 0; i < list1.size(); i++){
            Car tempCar = list1.get(i);
            list.add(tempCar);
        }

        return list;
    }

    @Override
    public void onItemClick(View view, int position) {
        String car_name = list.get(position).getName();
        String car_id = list.get(position).getId();
        Intent intent = new Intent(ActivityAvailableCars.this,ActivityAddReservation.class);
        intent.putExtra("CAR_NAME",car_name);
        intent.putExtra("CAR_ID",car_id);
        intent.putExtra("DATE_START",day_start);
        intent.putExtra("HOUR_START",hour_start);
        intent.putExtra("DATE_END",day_end);
        intent.putExtra("HOUR_END",hour_end);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    //This method is used to fetch all available cars for the current reservation.
    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Car car = new Car(
                            jsonObject.getString("id"),jsonObject.getString("name"),
                            jsonObject.getString("category"),jsonObject.getString("kms"),
                                jsonObject.getString("license_plate"),jsonObject.getString("total_rsvs"),
                                jsonObject.getString("monthly_rsvs")
                        );
                        list1.add(car);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                list=populateList();
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
}
