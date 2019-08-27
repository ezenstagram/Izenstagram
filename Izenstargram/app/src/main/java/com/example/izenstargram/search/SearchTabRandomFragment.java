package com.example.izenstargram.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.search.adapter.SearchTabRandomAdapter;

public class SearchTabRandomFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab_random_layout, container, false);
        int[] img = {
                R.drawable.jobo_01, R.drawable.jobo_02, R.drawable.jobo_03, R.drawable.jobo_04, R.drawable.jobo_05, R.drawable.jobo_06,
                R.drawable.jobo_07, R.drawable.jobo_08, R.drawable.jobo_09, R.drawable.jobo_10, R.drawable.jobo_11, R.drawable.jobo_12,
                R.drawable.jobo_13, R.drawable.jobo_14
        };
        SearchTabRandomAdapter adapter = new SearchTabRandomAdapter(getActivity().getApplicationContext(), R.layout.search_gridview_item, img);

        GridView gv = (GridView) view.findViewById(R.id.gridView);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "position", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
