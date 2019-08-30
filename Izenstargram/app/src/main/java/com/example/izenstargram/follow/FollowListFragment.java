package com.example.izenstargram.follow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.izenstargram.R;

public class FollowListFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager pager;
    TabItem tabItem1, tabItem2;
    TextView textViewLogin_id;

//    String following = getArguments().getString("following", "0");
//      String follower = getArguments().getString("follower", "0");

    public static String following = "";
    public static String follower = "";

    int sepa;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.follow_fragment_layout, container, false);

        int user_id = getArguments().getInt("user_id", 0);
        String login_id = getArguments().getString("login_id", "0");
        sepa = getArguments().getInt("sepa", 0);

        tabLayout = view.findViewById(R.id.tabLayout);
        tabItem1 = view.findViewById(R.id.tabItem1);
        tabItem2 = view.findViewById(R.id.tabItem2);
        textViewLogin_id = view.findViewById(R.id.textViewLogin_id);
        pager = view.findViewById(R.id.viewPager);

        textViewLogin_id.setText(login_id);

        tabLayout.getTabAt(0).setText(follower+ " 팔로워");
        tabLayout.getTabAt(1).setText(following+ " 팔로잉");
        final FollowTabAPagerdapter adapter = new FollowTabAPagerdapter(getChildFragmentManager(), tabLayout.getTabCount(), user_id);
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        pager.onSaveInstanceState();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        TabLayout.Tab tab;
        if(sepa==0) {
            tab = tabLayout.getTabAt(0);
        } else {
            tab = tabLayout.getTabAt(1);
        }
        tab.select();
        return view;
    }
}
