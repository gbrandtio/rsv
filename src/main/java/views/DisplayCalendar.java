package com.example.root.rsv.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.R;
import com.example.root.rsv.adapters.AdapterCalendar;
import com.example.root.rsv.models.RSV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DisplayCalendar extends AppCompatActivity {

    private static String response,url;
    private Context context;
    private TextView tvCarState;
    public static ArrayList<RSV> list1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_calendar);

        Intent intent = getIntent();
        String month = intent.getStringExtra("MONTH");

        context = Objects.requireNonNull(this);
        url = context.getResources().getString(R.string.base_url)
                +context.getResources().getString(R.string.reservationsByCar) +month;
        // ButterKnife.bind(this,Objects.requireNonNull(getView()));

        System.out.println(url);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        list1 = new ArrayList<>();
        getData();

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
                        RSV rsv= new RSV(
                                jsonObject.getString("id"),jsonObject.getString("car_id"),
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
                populate_grid();
                //adapter.notifyDataSetChanged();
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

    private void populate_grid() {

        GridLayout gridLayout = (GridLayout) findViewById(R.id.custom_calendar_grid_layout);
        //Inflating custom calendar layout with the appropriate information
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setColumnCount(32);
        gridLayout.setRowCount(4);

        TextView tvDayNo;
        int rows = 0;
        for (int i = 0; i < 3; i++) {
            rows++;
            for (int m = 0; m < 32; m++) {
                tvDayNo = new TextView(context);
                tvDayNo.setTextColor(Color.BLACK);
                tvDayNo.setTextSize(15);

                tvCarState = new TextView(context);
                tvCarState.setBackgroundResource(R.color.green);
                for (int r = 0; r < list1.size(); r++) {
                    RSV current_rsv = list1.get(r);
                    if (current_rsv.getCar_id().equals(String.valueOf(i))) {
                        if (m == 0) {
                            tvDayNo.setText(current_rsv.getCar_name());
                            tvDayNo.setTypeface(tvDayNo.getTypeface(), Typeface.BOLD);
                            tvCarState.setBackgroundResource(R.color.white);
                        } else {
                            tvDayNo.setTypeface(tvDayNo.getTypeface());
                            //if (current_rsv.getCar_id().equals(String.valueOf(i))) {
                            String[] date_start = current_rsv.getDay_start().split("-");
                            String day_start = date_start[2];
                            String[] date_end = current_rsv.getDay_end().split("-");
                            String day_end = date_end[2];
                            String month_start = date_start[1];
                            String month_end = date_end[1];

                            int start = Integer.valueOf(day_start);
                            int end = Integer.valueOf(day_end);

                            if (m >= start && m <= end ){
                                tvCarState.setBackgroundResource(R.color.red);
                            }
                            tvDayNo.setText(m + "");
                        }
                    }
                }

                gridLayout.addView(tvDayNo, i);
                gridLayout.addView(tvCarState, i);
                //titleText.setCompoundDrawablesWithIntrinsicBounds(10, 0, 0, 0);
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = 125;
                param.width = 125;
                param.rightMargin = 10;
                param.topMargin = 30;
                param.setGravity(Gravity.CENTER);
                param.columnSpec = GridLayout.spec(m);
                param.rowSpec = GridLayout.spec(i);
                tvCarState.setLayoutParams(param);
                tvDayNo.setLayoutParams(param);
            }
        }
    }
}
