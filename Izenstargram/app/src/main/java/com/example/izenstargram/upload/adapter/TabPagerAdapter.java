package com.example.izenstargram.upload.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.izenstargram.upload.CameraFragment;
import com.example.izenstargram.upload.GalleryFragment;
import com.example.izenstargram.upload.VideoFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GalleryFragment galleryFragment = new GalleryFragment();
                return galleryFragment;
            case 1:
                CameraFragment cameraFragment = new CameraFragment();
                return cameraFragment;

            case 2:
                VideoFragment videoFragment = new VideoFragment();
                return videoFragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
