package com.example.ecovid19_update.TabLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecovid19_update.R;

public class FragmentCities extends Fragment {
    TextView textCountry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities, container, false);

        textCountry = view.findViewById(R.id.textCountry);
        Intent intent = getActivity().getIntent();
        if(intent != null){
            textCountry.setText(intent.getStringExtra("country"));
        }
        return view;
    }
}
