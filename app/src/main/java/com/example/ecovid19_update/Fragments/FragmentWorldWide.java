package com.example.ecovid19_update.Fragments;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.ecovid19_update.Adapters.TopCountriesAdapter;
import com.example.ecovid19_update.Data.TopCountriesData;
import com.example.ecovid19_update.FragmentContainer;
import com.example.ecovid19_update.R;
import com.example.ecovid19_update.URLS;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class FragmentWorldWide extends Fragment {
    RequestQueue requestQueue;
    TextView textCases, textDeaths,textRecovered, textUpdated, textActive, textAffected,
            textCountry, textCasesPh, textDeathsPh,textRecoveredPh, textActivePh,
            textTodayDeaths, textTodayCases, textTest, textCritical;
    ImageView imgFlag;
    TextView textSeeAll, textViewDeatails;
    RecyclerView recyclerView;
    TopCountriesAdapter adapter;
    List<TopCountriesData>topCountriesDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_wordwide, container, false);

        class InitAllComponents extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(getActivity());
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pdLoading.setMessage("\tLoading...");
                pdLoading.setCancelable(false);
                pdLoading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                initWorldwideComponents(view);
                initRecyclerView(view);
                initPHComponents(view);
                pdLoading.dismiss();
            }

        }
        InitAllComponents initAllComponents = new InitAllComponents();
        initAllComponents.execute();
        return view;
    }

    private void initWorldwideComponents(View view){
        textCases = view.findViewById(R.id.textCases);
        textDeaths = view.findViewById(R.id.textDeaths);
        textRecovered = view.findViewById(R.id.textRecovered);
        textUpdated = view.findViewById(R.id.textUpdated);
        textActive = view.findViewById(R.id.textActive);
        textAffected = view.findViewById(R.id.textAffectedCountries);
        textCritical = view.findViewById(R.id.textCritical);

        requestQueue = Volley.newRequestQueue(getActivity());
        displayWorldwide(view);
    }

    private void initRecyclerView(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        topCountriesDataList = new ArrayList<>();
        displayTopCountries();
    }

    private void initPHComponents(View view){
        textCountry = view.findViewById(R.id.textCountry);
        textCasesPh = view.findViewById(R.id.textCasesPh);
        textDeathsPh = view.findViewById(R.id.textDeathsPh);
        textRecoveredPh = view.findViewById(R.id.textRecoveredPh);
        textActivePh = view.findViewById(R.id.textActivePh);
        textTodayCases = view.findViewById(R.id.textTodayCasesPh);
        textTodayDeaths = view.findViewById(R.id.textTodayDeathsPh);
        textSeeAll = view.findViewById(R.id.textSeaAll);
        textTest = view.findViewById(R.id.textTestPh);
        textViewDeatails = view.findViewById(R.id.textViewDetails);
        imgFlag = view.findViewById(R.id.imgFlag);
        displayMyCountry();
        onClick();
    }

    private void onClick(){
        textViewDeatails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FragmentContainer.class);
                intent.putExtra("country", "PH");
                startActivity(intent);
            }
        });

        textSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentCountries())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void displayTopCountries(){
        String sParam = "?sort=cases"; //Sort countries by cases
        StringRequest request = new StringRequest(Request.Method.GET,
                URLS.displayCountries + sParam,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject countryInfo = jsonArray.getJSONObject(i).getJSONObject("countryInfo");
                                JSONObject object = jsonArray.getJSONObject(i);
                                TopCountriesData data = new  TopCountriesData(object.getString("country"),
                                        countryInfo.getString("flag"),
                                        object.getInt("cases"),
                                        object.getInt("deaths"),
                                        object.getInt("recovered"),
                                        object.getInt("active"));
                                topCountriesDataList.add(data);
                            }

                            adapter = new TopCountriesAdapter(topCountriesDataList, getActivity());
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }

    private void startCountAnimationWorldwide(final int cases, final int deaths, final int recovered, final int affected, final int active, final int critical) {
        ValueAnimator animator = ValueAnimator.ofInt(0, cases);
        ValueAnimator animator1 = ValueAnimator.ofInt(0, deaths);
        ValueAnimator animator2 = ValueAnimator.ofInt(0, recovered);
        ValueAnimator animator3 = ValueAnimator.ofInt(0, affected);
        ValueAnimator animator4 = ValueAnimator.ofInt(0, active);
        ValueAnimator animator5 = ValueAnimator.ofInt(0, critical);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textCases.setText(decimalFormatter((int)animation.getAnimatedValue()));
            }
        });
        animator.start();

        animator1.setDuration(2000);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textDeaths.setText(decimalFormatter((int)animation.getAnimatedValue()));
            }
        });
        animator1.start();

        animator2.setDuration(2000);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textRecovered.setText(decimalFormatter((int)animation.getAnimatedValue()));
            }
        });
        animator2.start();

        animator3.setDuration(2000);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textAffected.setText(decimalFormatter((int)animation.getAnimatedValue()));
            }
        });
        animator3.start();

        animator4.setDuration(2000);
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textActive.setText(decimalFormatter((int)animation.getAnimatedValue()));
            }
        });
        animator4.start();

        animator5.setDuration(2000);
        animator5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textCritical.setText(decimalFormatter((int)animation.getAnimatedValue()));
            }
        });
        animator5.start();
    }

    private void displayWorldwide(final View view){

            StringRequest request = new StringRequest(Request.Method.GET,
                    URLS.displayWorldwide,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int cases = jsonObject.getInt("cases");
                            int deaths = jsonObject.getInt("deaths");
                            int recovered = jsonObject.getInt("recovered");
                            long updated = jsonObject.getLong("updated");
                            int active = jsonObject.getInt("active");
                            int affected = jsonObject.getInt("affectedCountries");
                            int critical = jsonObject.getInt("critical");

                            startCountAnimationWorldwide(cases, deaths, recovered, affected, active, critical);

                            textUpdated.setText(toDate(updated));

                            initLineChart(view);
                            initBarChart(view, active, deaths, recovered);
                            initPieChart(view, active, deaths, recovered);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void initLineChart(View view){
        final AnyChartView anyChartView = view.findViewById(R.id.lineChartWorld);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        final Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        //   cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");
        //   cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        final List<DataEntry> seriesData = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET,
                URLS.displayWorldTimeline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject jsonObject = object.getJSONObject("cases");
                            JSONObject jsonObject1 = object.getJSONObject("deaths");
                            JSONObject jsonObject2 = object.getJSONObject("recovered");

                            Iterator<String> iter = jsonObject.keys();
                            Iterator<String> iter1 = jsonObject1.keys();
                            Iterator<String> iter2 = jsonObject2.keys();

                            while (iter.hasNext()) {
                                while (iter1.hasNext()){
                                    while (iter2.hasNext()){
                                        String key = iter.next();
                                        String key1 = iter1.next();
                                        String key2 = iter2.next();
                                        int cases = jsonObject.getInt(key);
                                        int deaths = jsonObject1.getInt(key1);
                                        int recovered = jsonObject2.getInt(key2);

                                        seriesData.add(new CustomDataEntry(key, cases, deaths, recovered));
                                    }
                                }
                            }

                            Set set = Set.instantiate();
                            set.data(seriesData);

                            Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                            Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
                            Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

                            Line series1 = cartesian.line(series1Mapping);
                            series1.name("Cases");
                            series1.hovered().markers().enabled(true);
                            series1.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series1.tooltip()
                                    .position("right")
                                    .anchor(Anchor.RIGHT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            Line series2 = cartesian.line(series2Mapping);
                            series2.name("Deaths");
                            series2.hovered().markers().enabled(true);
                            series2.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series2.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            Line series3 = cartesian.line(series3Mapping);
                            series3.name("Recovered");
                            series3.hovered().markers().enabled(true);
                            series3.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series3.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            cartesian.legend().enabled(true);
                            cartesian.legend().fontSize(11d);
                            cartesian.legend().padding(0d, 0d, 10d, 0d);

                            anyChartView.setChart(cartesian);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
    }

    private void initBarChart(View view, int active, int deaths, int recovered){
        BarChart chart = view.findViewById(R.id.barchart);
        ArrayList dataList = new ArrayList();

        dataList.add(new BarEntry(active, 0));
        dataList.add(new BarEntry(deaths, 1));
        dataList.add(new BarEntry(recovered, 2));

        ArrayList labelList = new ArrayList();

        labelList.add("Active");
        labelList.add("Deaths");
        labelList.add("Recovered");

        BarDataSet bardataset = new BarDataSet(dataList, "");
        bardataset.setValueTextSize(11);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
        chart.animateY(4000);
        BarData data = new BarData(labelList, bardataset);
        int[] colors = new int[] {
                Color.parseColor("#FE9500"),
                Color.parseColor("#AA0000"),
                Color.parseColor("#28B463")
        };
        bardataset.setColors(colors);
        chart.setData(data);
    }

    private void initPieChart(View view, int active, int deaths, int recovered) {
        PieChart pieChart = view.findViewById(R.id.pieChart);
        ArrayList dataList = new ArrayList();

        dataList.add(new Entry(active, 0));
        dataList.add(new Entry(deaths, 1));
        dataList.add(new Entry(recovered, 2));
        PieDataSet dataSet = new PieDataSet(dataList, "");

        ArrayList labelList = new ArrayList();

        labelList.add("Active");
        labelList.add("Deaths");
        labelList.add("Recovered");
        PieData data = new PieData(labelList, dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(11);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDescription(null);
        pieChart.setHoleRadius(50);
        pieChart.setData(data);
        int[] colors = new int[] {Color.parseColor("#FE9500"),
                Color.parseColor("#AA0000"),
                Color.parseColor("#28B463")};
        dataSet.setColors(colors);
        pieChart.animateXY(5000, 5000);
    }

    private void displayMyCountry(){
        StringRequest request = new StringRequest(Request.Method.GET,
                URLS.displayMyCountry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject object2 = object.getJSONObject("countryInfo");

                            String country = object.getString("country");
                            String flagURL = object2.getString("flag");
                            int cases = object.getInt("cases");
                            int deaths = object.getInt("deaths");
                            int recovered = object.getInt("recovered");
                            int active = object.getInt("active");
                            int todayCases = object.getInt("todayCases");
                            int todayDeaths = object.getInt("todayDeaths");
                            int test = object.getInt("tests");

                            textCountry.setText(country);
                            textCasesPh.setText(decimalFormatter(cases));
                            textDeathsPh.setText(decimalFormatter(deaths));
                            textRecoveredPh.setText(decimalFormatter(recovered));
                            textActivePh.setText(decimalFormatter(active));
                            textTodayCases.setText(decimalFormatter(todayCases));
                            textTodayDeaths.setText(decimalFormatter(todayDeaths));
                            textTest.setText(decimalFormatter(test));
                            Picasso.with(getActivity()).load(flagURL).fit().centerInside().into(imgFlag);

                          }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }


    private String decimalFormatter(int decimal){
        String val = String.format("%,d", decimal);
        return val;
    }

    private String toDate(long val){
        Date dt = new Date(val);
        return String.valueOf(dt);
    }
}