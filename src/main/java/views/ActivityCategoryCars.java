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
import com.example.root.rsv.adapters.AdapterCars;
import com.example.root.rsv.models.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityCategoryCars extends AppCompatActivity implements SearchView.OnQueryTextListener,AdapterCars.ItemClickListener{

    private ProgressBar progressBar;
    private Context context;
    private String url;
    AdapterCars adapter;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    public static ArrayList<Car> list,list1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_cars);

        context=this;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String category = intent.getStringExtra("CATEGORY");

        list = new ArrayList();
        list1 = new ArrayList<>();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AdapterCars(context, list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint("Search by car name or license plate");
        searchView.setOnQueryTextListener(this);

        url = getResources().getString(R.string.base_url) + getResources().getString(R.string.cars) + category;
        getData();

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
        Toast.makeText(context, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Car car = new Car(
                                jsonObject.getString("id"),
                                jsonObject.getString("name"),jsonObject.getString("category"),
                                jsonObject.getString("kms"),jsonObject.getString("license_plate"),
                                jsonObject.getString("total_rsvs"),jsonObject.getString("image")
                        );
                        list1.add(car);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                list = populateList();
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CatCars", error.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }
}
