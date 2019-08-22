package com.example.izenstargram.like.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.example.izenstargram.R;
import com.example.izenstargram.like.fragment.FollowingFragment;
import com.example.izenstargram.like.fragment.UsersFragment;

public class LikePagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;

    public LikePagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FollowingFragment followingFragment = new FollowingFragment();
                return followingFragment;
            case 1:
                UsersFragment usersFragment = new UsersFragment();
                return usersFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}