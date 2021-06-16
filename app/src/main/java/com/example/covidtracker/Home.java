package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.os.Bundle;


import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class Home extends AppCompatActivity {
    private TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_recovered, tv_recovered_new, tv_death,
            tv_death_new, tv_tot_vaccine, tv_1st_dose, tv_2nd_dose, tv_tot_vaccinated;
    private String str_confirmed, str_confirmed_new, str_active, str_active_new, str_recovered, str_recovered_new,
            str_death, str_death_new, str_tot_vaccine, str_1st_Dose, str_2nd_Dose,str_tot_vaccinated,str_prev_tot_vaccine,str_prev_1st_dose,str_prev_2nd_dose,str_prev_tot_vaccinated;
    private SwipeRefreshLayout swipeRefreshLayout;

    private PieChart pieChart;
    private int int_active_new;
    private ProgressDialog progressDialog;
    private Button statebutton,wordwidebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Covid-19 India");
        Init();
        FetchData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        statebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,State_data.class);
                startActivity(intent);
            }
        });
        wordwidebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this,Worldwide_Data.class);
                startActivity(intent);
            }
        });

    }

    private void FetchData() {

        ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl="https://api.covid19india.org/data.json";
        pieChart.clearChart();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,apiUrl,null,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray all_state_jsonArray=null;
                       JSONArray testData_jsonArray = null;

                        try {
                            all_state_jsonArray=response.getJSONArray("statewise");
                           testData_jsonArray=response.getJSONArray("tested");
                            JSONObject data_india = all_state_jsonArray.getJSONObject(0);
                           JSONObject test_data_india = testData_jsonArray.getJSONObject(testData_jsonArray.length()-1);
                           JSONObject test_prev_data_india=testData_jsonArray.getJSONObject(testData_jsonArray.length()-2);
                            str_confirmed = data_india.getString("confirmed");   //Confirmed cases in India
                            str_confirmed_new = data_india.getString("deltaconfirmed");   //New Confirmed cases from last update time

                            str_active = data_india.getString("active");    //Active cases in India

                            str_recovered = data_india.getString("recovered");  //Total recovered cased in India
                            str_recovered_new = data_india.getString("deltarecovered"); //New recovered cases from last update time

                            str_death = data_india.getString("deaths");     //Total deaths in India
                            str_death_new = data_india.getString("deltadeaths");

                            str_tot_vaccine=test_data_india.getString("totaldosesadministered");
                            str_1st_Dose=test_data_india.getString("firstdoseadministered");
                            str_2nd_Dose=test_data_india.getString("seconddoseadministered");
                            str_tot_vaccinated=test_data_india.getString("totalindividualsvaccinated");

                            str_prev_tot_vaccine=test_prev_data_india.getString("totaldosesadministered");
                            str_prev_1st_dose=test_prev_data_india.getString("firstdoseadministered");
                            str_prev_2nd_dose=test_prev_data_india.getString("seconddoseadministered");
                            str_prev_tot_vaccinated=test_prev_data_india.getString("totalindividualsvaccinated");



                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Your Code
                                    tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
                                    tv_confirmed_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));

                                    tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));

                                    int_active_new = Integer.parseInt(str_confirmed_new)
                                            - (Integer.parseInt(str_recovered_new) + Integer.parseInt(str_death_new));
                                    tv_active_new.setText("+"+NumberFormat.getInstance().format(int_active_new));

                                    tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                                    tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_recovered_new)));

                                    tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
                                    tv_death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));

                                    if(str_1st_Dose.equals(""))
                                        tv_1st_dose.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_prev_1st_dose)));
                                    else
                                        tv_1st_dose.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_1st_Dose)));
                                    if(str_2nd_Dose.equals(""))
                                        tv_2nd_dose.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_prev_2nd_dose)));
                                    else
                                        tv_2nd_dose.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_2nd_Dose)));
                                    if(str_tot_vaccinated.equals(""))
                                        tv_tot_vaccinated.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_prev_tot_vaccinated)));
                                    else
                                        tv_tot_vaccinated.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_tot_vaccinated)));
                                    if(str_tot_vaccine.equals(""))
                                        tv_tot_vaccine.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_prev_tot_vaccine)));
                                    else
                                    tv_tot_vaccine.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_tot_vaccine)));
//                                  
                                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
                                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
                                    pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(str_death), Color.parseColor("#F6404F")));
                                    pieChart.startAnimation();
                                    DismissDialog();


                                }
                            }, 3000);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
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
        tv_confirmed=findViewById(R.id.confirmed_textView);
        tv_confirmed_new=findViewById(R.id.confirmed_new_textView);
        tv_active=findViewById(R.id.active_textView);
        tv_active_new=findViewById(R.id.active_new_textView);
        tv_recovered=findViewById(R.id.recovered_textView);
        tv_recovered_new=findViewById(R.id.recovered_new_textView);
        tv_death=findViewById(R.id.death_textView);
        tv_death_new=findViewById(R.id.death_new_textView);
        tv_tot_vaccine=findViewById(R.id.tot_vaccination_View);
        tv_1st_dose=findViewById(R.id.ist_Dose_View);
        tv_2nd_dose=findViewById(R.id.sec_Dose_View);
        tv_tot_vaccinated=findViewById(R.id.tot_vaccinatedView);
        statebutton=findViewById(R.id.button_stateWise);
        wordwidebutton=findViewById(R.id.button_worldWide);
        pieChart=findViewById(R.id.piechart);
        swipeRefreshLayout=findViewById(R.id.main_refreshLayout);

    }
}