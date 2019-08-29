package com.example.izenstargram.follow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FollowTabAPagerdapter extends FragmentStatePagerAdapter {

    private int count;
    FollowTabFragment1 followFragment1 = new FollowTabFragment1();
    FollowTabFragment2 followFragment2 = new FollowTabFragment2();

    int user_id;
    public FollowTabAPagerdapter(FragmentManager fm, int tabCount, int user_id) {
        super(fm);
        this.count = tabCount;
        this.user_id = user_id;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle1 = new Bundle(4);
        bundle1.putInt("user_id", user_id);
        switch (i) {
            case 0:
                followFragment1.setArguments(bundle1);
                return followFragment1;
            case 1:
                followFragment2.setArguments(bundle1);
                return followFragment2;
            default:
                followFragment1.setArguments(bundle1);
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
