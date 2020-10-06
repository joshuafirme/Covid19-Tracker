package com.example.ecovid19_update.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ecovid19_update.TabLayout.FragmentCities;
import com.example.ecovid19_update.TabLayout.FragmentDashboard;
import com.example.ecovid19_update.TabLayout.FragmentFacilities;
import com.example.ecovid19_update.TabLayout.FragmentTimeline;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public PageAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentDashboard();
            case 1:
                return new FragmentTimeline();
            case 2:
                return new FragmentCities();
            case 3:
                return new FragmentFacilities();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
