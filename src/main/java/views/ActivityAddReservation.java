package com.example.root.rsv.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.MainActivity;
import com.example.root.rsv.R;
import com.example.root.rsv.fragments.FragmentRSVs;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ActivityAddReservation extends AppCompatActivity {

    private TextView tvCar,tvDateStart,tvDateEnd;
    private EditText etClientName,etClientEmail,etClientTelephone,etClientNationality,etTotalMoney;
    private Button btnAddRsv;
    private Context context;
    private String client_name,client_email,client_telephone,client_nationality
            ,day_start,hour_start,day_end,hour_end,car_name,car_id,total_money;
    private String url;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        context = this;
        url = getResources().getString(R.string.base_url) + getResources().getString(R.string.addReservation);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        day_start = intent.getStringExtra("DATE_START");
        hour_start = intent.getStringExtra("HOUR_START");
        day_end = intent.getStringExtra("DATE_END");
        hour_end = intent.getStringExtra("HOUR_END");
        car_name = intent.getStringExtra("CAR_NAME");
        car_id = intent.getStringExtra("CAR_ID");

        tvCar = (TextView) findViewById(R.id.selected_car);
        tvDateStart = (TextView) findViewById(R.id.date_start);
        tvDateEnd = (TextView) findViewById(R.id.date_end);

        tvCar.setText("Selected Car: " + car_name);
        tvDateStart.setText("Starting Date: " + day_start);
        tvDateEnd.setText("Ending Date: " + day_end);

        etClientName = (EditText) findViewById(R.id.add_rsv_client_name);
        etClientEmail = (EditText) findViewById(R.id.add_rsv_client_email);
        etClientTelephone = (EditText) findViewById(R.id.add_rsv_client_telephone);
        etClientNationality = (EditText) findViewById(R.id.add_rsv_client_nationality);
        etTotalMoney = (EditText) findViewById(R.id.add_rsv_total_money);

        btnAddRsv = (Button) findViewById(R.id.btn_add_reservation);
        btnAddRsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client_name = etClientName.getText().toString().toUpperCase();
                client_email = etClientEmail.getText().toString().toLowerCase();
                client_telephone = etClientTelephone.getText().toString().toLowerCase();
                client_nationality = etClientNationality.getText().toString().toUpperCase();
                total_money = etTotalMoney.getText().toString();
                addReservation();
            }
        });
    }

    //POST request to the API to add the reservation inside the database.
    public void addReservation(){
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
                        Intent intent = new Intent(context, FragmentRSVs.class);
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
                        Toast.makeText(context, "Error.The reservation has not been added.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("car_id", car_id);
                params.put("client_name", client_name);
                params.put("client_email", client_email);
                params.put("client_telephone", client_telephone);
                params.put("client_nationality",client_nationality);
                params.put("total_money",total_money);
                params.put("day_start",day_start);
                params.put("hour_start",hour_start);
                params.put("day_end",day_end);
                params.put("hour_end",hour_end);

                return params;
            }

        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
}
