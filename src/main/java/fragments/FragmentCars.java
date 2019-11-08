package com.example.root.rsv.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.example.root.rsv.R;
import com.example.root.rsv.views.ActivityAddCar;
import com.example.root.rsv.views.ActivityCategoryCars;
import com.example.root.rsv.views.ActivitySetDates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: Ioannis Brant Ioannidis
 * @email: giannisbr@gmail.com
 * @date: 15/10/2019
 */
public class FragmentCars extends Fragment {
    private static final String TAG = FragmentCars.class.getName();
    private static final int LOADER_ID = 477;


    private static String response;
    private Context context;


    public FragmentCars(){}

    public static FragmentCars newInstance() {
        return new FragmentCars();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = Objects.requireNonNull(getActivity());
        CardView tvCatA =(CardView) view.findViewById(R.id.category_a);
        CardView tvCatB =(CardView) view.findViewById(R.id.category_b);
        CardView tvCatC =(CardView) view.findViewById(R.id.category_c);
        CardView tvCatD =(CardView) view.findViewById(R.id.category_d);
        CardView tvCatE =(CardView) view.findViewById(R.id.category_e);
        CardView tvCatF =(CardView) view.findViewById(R.id.category_f);
        CardView tvCatS =(CardView) view.findViewById(R.id.category_s);
        CardView tvCatM =(CardView) view.findViewById(R.id.category_m);
        Button btnShowAllCars = (Button) view.findViewById(R.id.btn_show_all_cars);

        FloatingActionButton btnUpload = (FloatingActionButton)  view.findViewById(R.id.fab_upload_rsv);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAddCar.class);
                startActivity(intent);
            }
        });

        btnShowAllCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","");
                startActivity(intent);
            }
        });

        tvCatA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","A");
                startActivity(intent);
            }
        });

        tvCatB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","B");
                startActivity(intent);
            }
        });

        tvCatC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","C");
                startActivity(intent);
            }
        });

        tvCatD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","D");
                startActivity(intent);
            }
        });

        tvCatE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","E");
                startActivity(intent);
            }
        });

        tvCatF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","F");
                startActivity(intent);
            }
        });

        tvCatS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","S");
                startActivity(intent);
            }
        });

        tvCatM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCategoryCars.class);
                intent.putExtra("CATEGORY","M" +
                        "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_cars, container, false);
       /* btnUploadContest = (FloatingActionButton)  v.findViewById(R.id.fab_upload_contest);
        btnUploadContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadContestActivity.class);
                startActivity(intent);
            }
        });*/
        return v;
    }
}


