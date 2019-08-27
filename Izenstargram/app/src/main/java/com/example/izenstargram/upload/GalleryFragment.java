package com.example.izenstargram.upload;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.izenstargram.R;


public class GalleryFragment extends Fragment implements View.OnClickListener {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false); // attachToRoot는 일단 false로..

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
