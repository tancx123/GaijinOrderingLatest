package com.example.orderandinventorysystem.ui.pack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.orderandinventorysystem.Adapter.SectionsPagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.orderandinventorysystem.R;
import com.google.android.material.tabs.TabLayout;

public class PackageFragment extends Fragment {

    private PackageViewModel packageViewModel;

    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_packages, container,false);
        viewPager = myFragment.findViewById(R.id.viewpager_all);
        tabLayout = myFragment.findViewById(R.id.tabLayout);
        viewPager.setOffscreenPageLimit(4);
        return myFragment;
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

        adapter.addFragment(new packages_all(), "ALL");
        adapter.addFragment(new packages_packed(), "PACKED");
        adapter.addFragment(new packages_shipped(), "SHIPPED");
        adapter.addFragment(new packages_delivered(), "DELIVERED");

        viewPager.setAdapter(adapter);
    }

}