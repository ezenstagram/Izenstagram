package com.example.izenstargram.search.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.izenstargram.search.SearchTabRandomFragment;
import com.example.izenstargram.search.SearchTabTagFragment;
import com.example.izenstargram.search.SearchTabUserFragment;

public class SearchPagerAdapter extends FragmentStatePagerAdapter {

    private int pageCount;
    SearchTabRandomFragment searchTabRandomFragment = new SearchTabRandomFragment();
    SearchTabUserFragment searchTabUserFragment = new SearchTabUserFragment();
    SearchTabTagFragment searchTabTagFragment = new SearchTabTagFragment();


    public SearchPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.pageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return searchTabRandomFragment;
            case 1:
                return searchTabUserFragment;
            case 2:
                return searchTabTagFragment;
            default:
                return searchTabTagFragment;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
