package com.example.orderandinventorysystem.ui.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.orderandinventorysystem.Adapter.SectionsPagerAdapter;
import com.example.orderandinventorysystem.R;
import com.google.android.material.tabs.TabLayout;

public class SalesFragment extends Fragment  {

    View root;
    ViewPager viewPager;
    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_packages, container, false);
        viewPager = root.findViewById(R.id.viewpager_all);
        tabLayout = root.findViewById(R.id.tabLayout);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new sales_avail(), "AVAILABLE");
        adapter.addFragment(new sales_remove(), "REMOVED");
        viewPager.setAdapter(adapter);
    }



}