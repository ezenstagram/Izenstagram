package com.example.izenstargram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LikeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.like_layout, null);
        View view = inflater.inflate(R.layout.like_layout, container, false);
        String name = getArguments().getString("data");
        Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();

        return view;
    }
}