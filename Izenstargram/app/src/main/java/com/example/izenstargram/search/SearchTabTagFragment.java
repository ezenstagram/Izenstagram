package com.example.izenstargram.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.izenstargram.R;

public class SearchTabTagFragment extends Fragment {

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab_tag_layout, container, false);

//        textView = view.findViewById(R.id.textViewTabTag);
//        String letter_to_search = getArguments().getString("letter_to_search");
//        Log.d("[INFO]", "TabUserFragment : 받아온 letter_to_search =" + letter_to_search);
//        textView.setText(letter_to_search);

        return view;
    }
}
