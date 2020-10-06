package com.example.ecovid19_update.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovid19_update.Data.CountriesTotal;
import com.example.ecovid19_update.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CountriesTotalAdapter extends RecyclerView.Adapter<CountriesTotalAdapter.ViewHolder> implements Filterable {
    private List<CountriesTotal>countriesTotalList;
    private List<CountriesTotal>countriesSearch;
    private Context context;
    static TapListener tapListener;

    public CountriesTotalAdapter(List<CountriesTotal> countriesTotalList, Context context) {
        this.countriesTotalList = countriesTotalList;
        this.context = context;
        countriesSearch = new ArrayList<>(countriesTotalList);
    }

    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    public Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<CountriesTotal> filterList = new ArrayList<>();
            if (charSequence == null || charSequence.length() ==0){
                filterList.addAll(countriesSearch);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (CountriesTotal item : countriesSearch){
                    if (item.getCountry().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            countriesTotalList.clear();
            countriesTotalList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cardview_countries, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CountriesTotal currentData = countriesTotalList.get(position);
        holder.country.setText(currentData.getCountry());
        holder.cases.setText(decimalFormatter(currentData.getCases()));
        holder.deaths.setText(decimalFormatter(currentData.getDeaths()));
        holder.recovered.setText(decimalFormatter(currentData.getRecovered()));
        holder.active.setText(decimalFormatter(currentData.getActive()));
        if (currentData.getTodayCases() == 0){
            holder.todayCases.setVisibility(View.GONE);
        }
        else {
            holder.todayCases.setText(decimalFormatter(currentData.getTodayCases())+"+ New Cases");
        }
        Picasso.with(context).load(currentData.getFlag())
                .fit().centerInside()
                .into(holder.imgFlag);
    }

    @Override
    public int getItemCount() {
        return countriesTotalList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView country, cases, deaths, recovered, active, todayCases, details;
        ImageView imgFlag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.textCountry);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            cases = itemView.findViewById(R.id.textCases);
            deaths = itemView.findViewById(R.id.textDeaths);
            recovered = itemView.findViewById(R.id.textRecovered);
            active = itemView.findViewById(R.id.textActive);
            todayCases = itemView.findViewById(R.id.textTodayCases);
            details = itemView.findViewById(R.id.textMoreDetails);

            details.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            tapListener.itemTap(view, getAdapterPosition(),
                    country.getText().toString());
        }
    }
    private String decimalFormatter(int decimal){
        String val = String.format("%,d", decimal);
        return val;
    }

    public interface TapListener {
        void itemTap(View view, int position, String country);
    }

    public void setTapListener(TapListener tapListener) {
        CountriesTotalAdapter.tapListener = tapListener;
    }
}
