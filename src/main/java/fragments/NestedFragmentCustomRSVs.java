package com.example.root.rsv.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.adapters.AdapterCalendar;
import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.adapters.AdapterFeed;
import com.example.root.rsv.views.DisplayCalendar;

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
public class NestedFragmentCustomRSVs  extends Fragment{
    private static final String TAG = NestedFragmentFeed .class.getName();
    private static final int LOADER_ID = 477;

    private Context context;
    private Button btnJanuary,btnFebruary,btnMarch,btnApril,btnMay,btnJune,
            btnJuly,btnAugust,btnSeptember,btnOctober,btnNovember,btnDecember;


    public NestedFragmentCustomRSVs(){}

    public static NestedFragmentCustomRSVs newInstance() {
        return new NestedFragmentCustomRSVs();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = Objects.requireNonNull(getActivity());
        btnJanuary = view.findViewById(R.id.january);
        btnFebruary = view.findViewById(R.id.february);
        btnMarch = view.findViewById(R.id.march);
        btnApril = view.findViewById(R.id.april);
        btnMay = view.findViewById(R.id.may);
        btnJune = view.findViewById(R.id.june);
        btnJuly = view.findViewById(R.id.july);
        btnAugust = view.findViewById(R.id.august);
        btnSeptember = view.findViewById(R.id.september);
        btnOctober = view.findViewById(R.id.october);
        btnNovember = view.findViewById(R.id.november);
        btnDecember = view.findViewById(R.id.december);

        btnJanuary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","01");
                startActivity(intent);
            }
        });

        btnFebruary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","02");
                startActivity(intent);
            }
        });

        btnMarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","03");
                startActivity(intent);
            }
        });

        btnApril.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","04");
                startActivity(intent);
            }
        });

        btnMay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","05");
                startActivity(intent);
            }
        });

        btnJune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","06");
                startActivity(intent);
            }
        });

        btnJuly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","07");
                startActivity(intent);
            }
        });

        btnAugust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","08");
                startActivity(intent);
            }
        });

        btnSeptember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","09");
                startActivity(intent);
            }
        });

        btnOctober.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","10");
                startActivity(intent);
            }
        });

        btnNovember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","11");
                startActivity(intent);
            }
        });

        btnDecember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayCalendar.class);
                intent.putExtra("MONTH","12");
                startActivity(intent);
            }
        });

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
        View v= inflater.inflate(R.layout.nested_fragment_custom_rsvs, container, false);
        return v;
    }



}




