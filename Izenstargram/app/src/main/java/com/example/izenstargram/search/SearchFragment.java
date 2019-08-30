package com.example.izenstargram.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.izenstargram.R;
import com.example.izenstargram.search.adapter.SearchPagerAdapter;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    SearchPagerAdapter searchPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    static String letter_to_search;

    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        Log.d("[INFO]", "SEARCH : onCreateView() ");
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        // searchPagerAdapter = new SearchPagerAdapter(
        //          getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        searchPagerAdapter = new SearchPagerAdapter(
                this.getChildFragmentManager(), 3); ///?
        viewPager.setAdapter(searchPagerAdapter);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.onSaveInstanceState();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("[INFO]", "SEARCH : tab.getPosition() : " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
                searchPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                searchPagerAdapter.notifyDataSetChanged();
            }
        });
        TabLayout.Tab tab = tabLayout.getTabAt(0);  // 0: 검색아이콘눌렀을 때 첫 탭
        tab.select();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("[INFO]", "SearchFragment : onQueryTextSubmit() 실행됨");
        letter_to_search = query;
        searchPagerAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("[INFO]", "SearchFragment : onQueryTextSubmit() 실행됨");
        letter_to_search = newText;
        searchPagerAdapter.notifyDataSetChanged();
        return false;
    }



}