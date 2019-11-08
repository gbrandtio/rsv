package com.example.root.rsv.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.root.rsv.R;
import com.example.root.rsv.adapters.AdapterRSVs;

import java.util.ArrayList;

public class ActivitySetDates extends AppCompatActivity {

    private ArrayList<String> listDays,listMonths,listYears,listHours,listMinutes;
    Spinner spinnerDayStart,spinnerMonthStart,spinnerYearStart,spinnerHourStart,spinnerMinutesStart;
    Spinner spinnerDayEnd,spinnerMonthEnd,spinnerYearEnd,spinnerHourEnd,spinnerMinutesEnd;
    private String edit_rsv,edit_client_id,edit_rsv_id;
    Button btnCheckAvailability;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dates);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        btnCheckAvailability = (Button) findViewById(R.id.btn_check_available_cars);

        Intent intent = getIntent();
        edit_rsv = intent.getStringExtra("EDIT_RSV");
        edit_client_id = intent.getStringExtra("EDIT_CLIENT_ID");
        edit_rsv_id = intent.getStringExtra("EDIT_RSV_ID");

        listDays = new ArrayList<>();
        listMonths = new ArrayList<>();
        listYears = new ArrayList<>();
        listHours = new ArrayList<>();
        listMinutes = new ArrayList<>();

        addDaysToSpinners();
        addMonthsToSpinners();
        addYearsToSpinners();
        addHoursToSpinners();
        addMinutesToSpinners();

        getValuesListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void addDaysToSpinners(){
        spinnerDayStart = (Spinner) findViewById(R.id.day_start_spinner);
        spinnerDayEnd = (Spinner) findViewById(R.id.day_end_spinner);

        listDays.add("01");
        listDays.add("02");
        listDays.add("03");
        listDays.add("04");
        listDays.add("05");
        listDays.add("06");
        listDays.add("07");
        listDays.add("08");
        listDays.add("09");
        listDays.add("10");
        listDays.add("11");
        listDays.add("12");
        listDays.add("13");
        listDays.add("14");
        listDays.add("15");
        listDays.add("16");
        listDays.add("17");
        listDays.add("18");
        listDays.add("19");
        listDays.add("20");
        listDays.add("21");
        listDays.add("22");
        listDays.add("23");
        listDays.add("24");
        listDays.add("25");
        listDays.add("26");
        listDays.add("27");
        listDays.add("28");
        listDays.add("29");
        listDays.add("30");
        listDays.add("31");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listDays);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDayStart.setAdapter(dataAdapter);
        spinnerDayEnd.setAdapter(dataAdapter);
    }

    private void addMonthsToSpinners(){
        spinnerMonthStart = (Spinner) findViewById(R.id.month_start_spinner);
        spinnerMonthEnd = (Spinner) findViewById(R.id.month_end_spinner);

        listMonths.add("January");listMonths.add("February");
        listMonths.add("March");listMonths.add("April");
        listMonths.add("May");listMonths.add("June");
        listMonths.add("July");listMonths.add("August");
        listMonths.add("September");listMonths.add("October");
        listMonths.add("November");listMonths.add("December");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listMonths);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonthStart.setAdapter(dataAdapter);
        spinnerMonthEnd.setAdapter(dataAdapter);
    }

    private void addYearsToSpinners(){
        spinnerYearStart = (Spinner) findViewById(R.id.year_start_spinner);
        spinnerYearEnd = (Spinner) findViewById(R.id.year_end_spinner);

        listYears.add("2019");listYears.add("2020");
        listYears.add("2021");listYears.add("2022");
        listYears.add("2023");listYears.add("2024");
        listYears.add("2025");listYears.add("2026");
        listYears.add("2027");listYears.add("2028");
        listYears.add("2029");listYears.add("2030");listYears.add("2031");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listYears);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYearStart.setAdapter(dataAdapter);
        spinnerYearEnd.setAdapter(dataAdapter);
    }

    private void addHoursToSpinners(){
        spinnerHourStart = (Spinner) findViewById(R.id.hour_start_spinner);
        spinnerHourEnd = (Spinner) findViewById(R.id.hour_end_spinner);

        listHours.add("01");listHours.add("02");listHours.add("03");listHours.add("04");
        listHours.add("05");listHours.add("06");listHours.add("07");listHours.add("08");listHours.add("09");
        listHours.add("10");listHours.add("11");listHours.add("12");listHours.add("13");listHours.add("14");
        listHours.add("15");listHours.add("16");listHours.add("17");listHours.add("18");listHours.add("19");
        listHours.add("20");listHours.add("21");listHours.add("22");listHours.add("23");listHours.add("24");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listHours);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerHourStart.setAdapter(dataAdapter);
        spinnerHourEnd.setAdapter(dataAdapter);
    }

    private void addMinutesToSpinners(){
        spinnerMinutesStart = (Spinner) findViewById(R.id.minutes_start_spinner);
        spinnerMinutesEnd = (Spinner) findViewById(R.id.minutes_end_spinner);

        listMinutes.add("00:00");listMinutes.add("15:00");listMinutes.add("30:00");listMinutes.add("45:00");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listMinutes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMinutesStart.setAdapter(dataAdapter);
        spinnerMinutesEnd.setAdapter(dataAdapter);
    }

    private void getValuesListener(){
        btnCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day_start = spinnerDayStart.getSelectedItem().toString();
                String day_end = spinnerDayEnd.getSelectedItem().toString();

                String month_start = spinnerMonthStart.getSelectedItem().toString();
                if(month_start.equals("January")){
                    month_start="01";
                }else if (month_start.equals("February")){
                    month_start="02";
                }else if (month_start.equals("March")){
                    month_start="03";
                }else if (month_start.equals("April")){
                    month_start="04";
                }else if (month_start.equals("May")){
                    month_start="05";
                }else if (month_start.equals("June")){
                    month_start="06";
                }else if (month_start.equals("July")){
                    month_start="07";
                }else if (month_start.equals("August")){
                    month_start="08";
                }else if (month_start.equals("September")){
                    month_start="09";
                }else if (month_start.equals("October")){
                    month_start="10";
                }else if (month_start.equals("November")){
                    month_start="11";
                }else if(month_start.equals("December")){
                    month_start="12";
                }

                String month_end = spinnerMonthEnd.getSelectedItem().toString();
                if(month_end.equals("January")){
                    month_end="01";
                }else if (month_end.equals("February")){
                    month_end="02";
                }else if (month_end.equals("March")){
                    month_end="03";
                }else if (month_end.equals("April")){
                    month_end="04";
                }else if (month_end.equals("May")){
                    month_end="05";
                }else if (month_end.equals("June")){
                    month_end="06";
                }else if (month_end.equals("July")){
                    month_end="07";
                }else if (month_end.equals("August")){
                    month_end="08";
                }else if (month_end.equals("September")){
                    month_end="09";
                }else if (month_end.equals("October")){
                    month_end="10";
                }else if (month_end.equals("November")){
                    month_end="11";
                }else if(month_end.equals("December")){
                    month_end="12";
                }

                String year_start = spinnerYearStart.getSelectedItem().toString();
                String year_end = spinnerYearEnd.getSelectedItem().toString();

                String hour_start = spinnerHourStart.getSelectedItem().toString();
                String hour_end = spinnerHourEnd.getSelectedItem().toString();

                String minute_start = spinnerMinutesStart.getSelectedItem().toString();
                String minute_end = spinnerMinutesEnd.getSelectedItem().toString();

                Intent intent = new Intent(ActivitySetDates.this,ActivityAvailableCars.class);
                String date_start = year_start+"-"+month_start+"-"+day_start;
                String date_end = year_end + "-" + month_end + "-" + day_end;
                String start_hour = hour_start + ":" +minute_start;
                String end_hour = hour_end + ":" + minute_end;

                System.out.println("DATE START: " + date_start);
                System.out.println("DATE END: " + date_end);

                intent.putExtra("DATE_START",date_start);
                intent.putExtra("START_HOUR",start_hour);
                intent.putExtra("DATE_END",date_end);
                intent.putExtra("END_HOUR",end_hour);
                intent.putExtra("EDIT_RSV",edit_rsv);
                intent.putExtra("EDIT_CLIENT_ID",edit_client_id);
                intent.putExtra("EDIT_RSV_ID",edit_rsv_id);

                startActivity(intent);

            }
        });
    }

}
