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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.rsv.R;
import com.example.root.rsv.adapters.AdapterClients;
import com.example.root.rsv.adapters.AdapterRSVs;
import com.example.root.rsv.models.Client;
import com.example.root.rsv.models.RSV;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: Ioannis Brant Ioannidis
 * @email: giannisbr@gmail.com
 * @date: 23/10/2019
 */
public class FragmentClients extends Fragment implements SearchView.OnQueryTextListener, AdapterClients.ItemClickListener{
    private static final String TAG = FragmentClients.class.getName();
    private static final int LOADER_ID = 477;


    private static String response;
    private Context context;
    AdapterClients adapter;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    public static ArrayList<Client> list,list1;
    private String url;
    private ProgressBar progressBar;


    public FragmentClients(){}

    public static FragmentClients newInstance() {
        return new FragmentClients();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = Objects.requireNonNull(getActivity());
        progressBar = view.findViewById(R.id.progressBar);

        url = context.getResources().getString(R.string.base_url)+context.getResources().getString(R.string.clients);

//        ButterKnife.bind(this,Objects.requireNonNull(getView()));
        // data to populate the RecyclerView with
        list = new ArrayList<>();
        list1 = new ArrayList<>();

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterClients(getContext(), list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        searchView = (SearchView) view.findViewById(R.id.search);
        searchView.setQueryHint("Search by client's name,e-mail or nationality");
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

    public ArrayList<Client> populateList(){
        //ArrayList<RSV> list = new ArrayList<>();
        for(int i = 0; i < list1.size(); i++){
            Client tempClient = list1.get(i);
            list.add(tempClient);
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
        View v= inflater.inflate(R.layout.fragment_clients, container, false);
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
                        Client client = new Client(
                                jsonObject.getString("id"),jsonObject.getString("name"),
                                jsonObject.getString("email"),jsonObject.getString("telephone"),
                                jsonObject.getString("nationality")
                        );
                        list1.add(client);
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
                Log.e("Clts", error.toString());
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context,"No clients",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }
}



