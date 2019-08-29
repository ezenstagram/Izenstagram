package com.example.izenstargram.follow;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FollowTabAPagerdapter extends FragmentStatePagerAdapter {

    private int count;
    FollowTabFragment1 followFragment1 = new FollowTabFragment1();
    FollowTabFragment2 followFragment2 = new FollowTabFragment2();

    public FollowTabAPagerdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.count = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return followFragment1;
            case 1:
                return followFragment2;
            default:
                return followFragment1;
        }
    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
    @Override
    public int getCount() {
        return count;
    }
}
