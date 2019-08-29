package com.example.izenstargram.profile.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.example.izenstargram.profile.TabFragment1;
import com.example.izenstargram.profile.TabFragment2;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private  int tabCount;
    TabFragment1 tabFragment1 = new TabFragment1();
    TabFragment2 tabFragment2 = new TabFragment2();
    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return tabFragment1;
            case 1:
                return tabFragment2;
            default:
                return tabFragment1;
        }
    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
