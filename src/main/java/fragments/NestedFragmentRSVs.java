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
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.adapters.AdapterRSVs;

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
public class NestedFragmentRSVs extends Fragment implements SearchView.OnQueryTextListener,AdapterRSVs.ItemClickListener{
    private static final String TAG = FragmentRSVs.class.getName();
    private static final int LOADER_ID = 477;


    private static String response;
    private Context context;
    AdapterRSVs adapter;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    public static ArrayList<RSV> list,list1;
    private String url;
    private ProgressBar progressBar;

    public NestedFragmentRSVs(){}

    public static NestedFragmentRSVs newInstance() {
        return new NestedFragmentRSVs();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = Objects.requireNonNull(getActivity());
        progressBar = view.findViewById(R.id.progressBar);

        url = context.getResources().getString(R.string.base_url)+context.getResources().getString(R.string.reservations);

//        ButterKnife.bind(this,Objects.requireNonNull(getView()));
        // data to populate the RecyclerView with
        list = new ArrayList<>();
        list1 = new ArrayList<>();

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterRSVs(getContext(), list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        searchView = (SearchView) view.findViewById(R.id.search);
        searchView.setQueryHint("Search by rsv,car,client or date");
        searchView.setOnQueryTextListener(this);

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

    public ArrayList<RSV> populateList(){
        //ArrayList<RSV> list = new ArrayList<>();
        for(int i = 0; i < list1.size(); i++){
            RSV tempRsv = list1.get(i);
            list.add(tempRsv);
        }

        return list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.nested_fragment_rsvs, container, false);
        return v;
    }

    //This method is used to fetch all reservations
    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
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



