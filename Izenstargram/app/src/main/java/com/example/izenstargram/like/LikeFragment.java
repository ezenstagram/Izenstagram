package com.example.izenstargram.like;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.like.adapter.LikePagerAdapter;
import com.example.izenstargram.profile.ProfileFragment;

public class LikeFragment extends Fragment {

    LikePagerAdapter likePagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.like_layout, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        likePagerAdapter = new LikePagerAdapter(
                this.getChildFragmentManager(), 2);

        viewPager.setAdapter(likePagerAdapter);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        viewPager.onSaveInstanceState();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                likePagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                likePagerAdapter.notifyDataSetChanged();
            }
        });

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        return view;
    }

}