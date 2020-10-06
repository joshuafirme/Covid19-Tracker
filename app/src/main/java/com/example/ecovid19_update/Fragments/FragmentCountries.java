package com.example.ecovid19_update.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecovid19_update.Adapters.CountriesTotalAdapter;
import com.example.ecovid19_update.Data.CountriesTotal;
import com.example.ecovid19_update.FragmentContainer;
import com.example.ecovid19_update.MainActivity;
import com.example.ecovid19_update.R;
import com.example.ecovid19_update.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentCountries extends Fragment {
    RecyclerView recyclerView;
    List<CountriesTotal> countriesTotalList;
    RequestQueue requestQueue;
    CountriesTotalAdapter adapter;
    Spinner spinnerSortBy;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countries, container, false);

        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        countriesTotalList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getActivity());

        return view;
    }


    private void displayCountries(){


        class displayCountries extends AsyncTask<Void, Void, String>{
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
                pdLoading.dismiss();

                String sParam = "?yesterday=true&sort=" + spinnerSortBy.getSelectedItem().toString().toLowerCase().trim();
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
                                        CountriesTotal data = new CountriesTotal(object.getString("country"),
                                                countryInfo.getString("flag"),
                                                object.getInt("cases"),
                                                object.getInt("deaths"),
                                                object.getInt("recovered"),
                                                object.getInt("active"),
                                                object.getInt("todayCases"));
                                        countriesTotalList.add(data);
                                    }

                                    adapter = new CountriesTotalAdapter(countriesTotalList, getActivity());
                                    recyclerView.setAdapter(adapter);

                                    adapter.setTapListener(new CountriesTotalAdapter.TapListener() {
                                        @Override
                                        public void itemTap(View view, int position, String country) {
                                            registerForContextMenu(recyclerView);

                                            Intent intent = new Intent(getActivity(), FragmentContainer.class);
                                            intent.putExtra("country", country);
                                            getActivity().startActivity(intent);
                                        }
                                    });
                                    adapter.notifyDataSetChanged();

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
        }

        displayCountries d = new displayCountries();
        d.execute();
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.top_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        searchItem.setActionView(searchView);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {
                    adapter.getFilter().filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        //Spinner
        MenuItem item = menu.findItem(R.id.spinner);
        spinnerSortBy = (Spinner) item.getActionView();

        List<String> items = new ArrayList<>();
        items.add("Cases");
        items.add("Deaths");
        items.add("Recovered");
        items.add("Active");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.spinner_layout, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSortBy.setAdapter(adapter);
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countriesTotalList.clear();
                displayCountries();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
