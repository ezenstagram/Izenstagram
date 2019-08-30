package com.example.izenstargram.profile.adapter;

import android.os.Bundle;
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
    int user_id;
    public TabPagerAdapter(FragmentManager fm, int tabCount, int user_id) {
        super(fm);
        this.tabCount = tabCount;
        this.user_id = user_id;
    }
    @Override
    public Fragment getItem(int position) {
        Bundle bundle1 = new Bundle(4);
        bundle1.putInt("user_id", user_id);
        switch (position) {
            case 0:
                tabFragment1.setArguments(bundle1);
                Log.d("[IN", "user = "+user_id);
                return tabFragment1;
            case 1:
                tabFragment2.setArguments(bundle1);
                return tabFragment2;
            default:
                tabFragment1.setArguments(bundle1);
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
