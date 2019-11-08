package com.example.root.rsv.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.FragmentCars;
import com.example.root.rsv.fragments.FragmentRSVs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityEditCar extends AppCompatActivity {

    private Context context;
    private ProgressBar progressBar;
    private Spinner spCarCategory;
    private EditText etCarName,etCarTotalRsvs,etCarKms,etCarLicensePlate;
    private Button btnUploadCar;
    private ArrayList<String> listCategories;
    private String car_name,car_category,car_license_plate,car_kms,car_total_rsvs,url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        listCategories = new ArrayList<>();

        spCarCategory = (Spinner) findViewById(R.id.add_car_category);
        etCarName = (EditText) findViewById(R.id.add_car_name);
        etCarLicensePlate = (EditText) findViewById(R.id.add_car_license_plate);
        etCarKms = (EditText) findViewById(R.id.add_car_kms);
        etCarTotalRsvs = (EditText) findViewById(R.id.add_car_total_rsvs);

        Intent intent = getIntent();
        car_name = intent.getStringExtra("CAR_NAME");
        car_category = intent.getStringExtra("CAR_CATEGORY");
        car_kms = intent.getStringExtra("CAR_KMS");
        car_license_plate = intent.getStringExtra("CAR_LICENSE_PLATE");
        car_total_rsvs = intent.getStringExtra("CAR_TOTAL_RSVS");

        etCarName.setText(car_name);
        etCarLicensePlate.setText(car_license_plate);
        etCarKms.setText(car_kms);
        etCarTotalRsvs.setText(car_total_rsvs);

        addCategoriesToSpinner();
        url = getResources().getString(R.string.base_url) + getResources().getString(R.string.editcar);

        btnUploadCar = (Button) findViewById(R.id.btn_add_car);
        inputsListener();
    }

    public void addCategoriesToSpinner(){
        listCategories.add(car_category);
        listCategories.add("A");
        listCategories.add("B");
        listCategories.add("C");
        listCategories.add("D");
        listCategories.add("E");
        listCategories.add("F");
        listCategories.add("S");
        listCategories.add("M");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listCategories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCarCategory.setAdapter(dataAdapter);
    }

    public void inputsListener(){
        btnUploadCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_name = etCarName.getText().toString();
                car_category = spCarCategory.getSelectedItem().toString();
                car_license_plate = etCarLicensePlate.getText().toString();
                car_kms = etCarKms.getText().toString();
                car_total_rsvs = etCarTotalRsvs.getText().toString();

                addCar();
            }
        });
    }

    public void addCar(){
        // Showing progress bar while adding reservation.
        progressBar.setVisibility(View.VISIBLE);

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress bar after all tasks complete.
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                        Intent intent = new Intent(context, FragmentCars.class);
                        intent.putExtra("SUCCESS","YES");
                        startActivity(intent);
                        // Showing Echo Response Message Coming From Server.
                        // Toast.makeText(context, ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress bar after all task complete.
                        progressBar.setVisibility(View.INVISIBLE);

                        // Showing error message if something goes wrong.
                        Toast.makeText(context, "Error.The car has not been added.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("name", car_name);
                params.put("category", car_category);
                params.put("kms", car_kms);
                params.put("license_plate", car_license_plate);
                params.put("total_rsvs", car_total_rsvs);

                return params;
            }

        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }
}

