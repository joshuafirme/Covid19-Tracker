package com.example.ecovid19_update.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovid19_update.Data.TopCountriesData;
import com.example.ecovid19_update.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopCountriesAdapter extends RecyclerView.Adapter<TopCountriesAdapter.ViewHolder> {
    private List<TopCountriesData> topCountriesData;
    private Context context;

    public TopCountriesAdapter(List<TopCountriesData> topCountriesData, Context context) {
        this.topCountriesData = topCountriesData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_dashboard_countries, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopCountriesData currentData = topCountriesData.get(position);
        holder.country.setText(currentData.getCountry());
        holder.cases.setText(decimalFormatter(currentData.getCases()));
        holder.deaths.setText(decimalFormatter(currentData.getDeaths()));
        holder.recovered.setText(decimalFormatter(currentData.getRecovered()));
    //    holder.active.setText(decimalFormatter(currentData.getActive()));
        Picasso.with(context).load(currentData.getFlag())
                .fit().centerInside()
                .into(holder.imgFlag);
    }

    @Override
    public int getItemCount() {
        return topCountriesData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView country, cases, deaths, recovered;
        ImageView imgFlag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.textCountry);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            cases = itemView.findViewById(R.id.textCases);
            deaths = itemView.findViewById(R.id.textDeaths);
            recovered = itemView.findViewById(R.id.textRecovered);
          //  active = itemView.findViewById(R.id.textActive);
        }
    }

    private String decimalFormatter(int decimal) {
        String val = String.format("%,d", decimal);
        return val;
    }
}
