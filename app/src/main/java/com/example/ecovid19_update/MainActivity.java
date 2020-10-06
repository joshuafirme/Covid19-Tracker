package com.example.ecovid19_update;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import android.view.MenuItem;

import com.example.ecovid19_update.Fragments.FragmentAbout;
import com.example.ecovid19_update.Fragments.FragmentCountries;
import com.example.ecovid19_update.Fragments.FragmentWorldWide;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentWorldWide()).commit();

    }
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedItem = null;
                    switch (item.getItemId()){
                        case R.id.world:
                            selectedItem = new FragmentWorldWide();
                            break;
                        case R.id.countries:
                            selectedItem = new FragmentCountries();
                            break;
                        case R.id.about:
                            selectedItem = new FragmentAbout();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedItem).commit();
                    return true;
                }
            };
}

