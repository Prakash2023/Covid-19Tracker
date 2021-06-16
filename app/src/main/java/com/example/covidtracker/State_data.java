package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class State_data extends AppCompatActivity {
    private String str_state, str_confirmed, str_confirmed_new, str_active, str_active_new, str_recovered, str_recovered_new,
            str_death, str_death_new, str_lastupdatedate;
    private RecyclerView rv_state_wise;
    private StatewiseAdapter stateWiseAdapter;
    private ArrayList<Statewisemodel> stateWiseModelArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText et_search;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_data);
        getSupportActionBar().setTitle("Covid-19 States");
        Init();
        FetchDataStateWise();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchDataStateWise();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Filter(s.toString());
            }
        });

    }
    private void Filter(String text) {
        ArrayList<Statewisemodel> filteredList = new ArrayList<>();
        for (Statewisemodel item : stateWiseModelArrayList) {
            if (item.getState().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        stateWiseAdapter.filterList(filteredList);
    }

    private void FetchDataStateWise() {
        ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiURL = "https://api.covid19india.org/data.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    stateWiseModelArrayList.clear();
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject statewise = jsonArray.getJSONObject(i);
                        str_state = statewise.getString("state");

                        str_confirmed = statewise.getString("confirmed");
                        str_confirmed_new = statewise.getString("deltaconfirmed");

                        str_active = statewise.getString("active");

                        str_death = statewise.getString("deaths");
                        str_death_new = statewise.getString("deltadeaths");

                        str_recovered = statewise.getString("recovered");
                        str_recovered_new = statewise.getString("deltarecovered");

                        Statewisemodel stateWiseModel = new Statewisemodel(str_state, str_confirmed, str_confirmed_new, str_active,
                                str_death, str_death_new, str_recovered, str_recovered_new, str_lastupdatedate);
                        stateWiseModelArrayList.add(stateWiseModel);
                        DismissDialog();

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stateWiseAdapter.notifyDataSetChanged();
                            }
                        }, 1000);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void DismissDialog() {
        progressDialog.dismiss();
    }

    private void ShowDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void Init() {
        et_search = findViewById(R.id.activity_state_wise_search_editText);

        rv_state_wise = findViewById(R.id.activity_state_wise_recyclerview);
        swipeRefreshLayout=findViewById(R.id.activity_state_wise_swipe_refresh_layout);
        rv_state_wise.setHasFixedSize(true);
        rv_state_wise.setLayoutManager(new LinearLayoutManager(this));

        stateWiseModelArrayList = new ArrayList<>();
        stateWiseAdapter = new StatewiseAdapter(State_data.this, stateWiseModelArrayList);
        rv_state_wise.setAdapter(stateWiseAdapter);
    }
}