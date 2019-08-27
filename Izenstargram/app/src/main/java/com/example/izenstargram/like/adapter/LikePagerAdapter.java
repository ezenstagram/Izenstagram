package com.example.izenstargram.like.adapter;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.izenstargram.like.fragment.FollowingFragment;
import com.example.izenstargram.like.fragment.UsersFragment;

public class LikePagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;
    FollowingFragment followingFragment = new FollowingFragment();
    UsersFragment usersFragment = new UsersFragment();


    public LikePagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return followingFragment;
            case 1:
                return usersFragment;
            default:
                return usersFragment;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mPageCount;
    }



}