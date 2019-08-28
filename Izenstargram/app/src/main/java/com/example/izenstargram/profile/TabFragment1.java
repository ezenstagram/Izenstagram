package com.example.izenstargram.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TabFragment1 extends Fragment{
    ImageGridAdapter adapter;

    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.32:8080/project/userProfileRefPost.do";
    List<String> list;
    int user_id;
    GridView gv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        Log.d("[INFO", "frag1");
        client = new AsyncHttpClient();
        response = new HttpResponse();

        SharedPreferences preferences = getActivity().getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        user_id = preferences.getInt("user_id", 0);

        RequestParams params = new RequestParams();
        params.put("user_id", user_id);

        client.post(URL, params, response);
        gv = (GridView) view.findViewById(R.id.gridView);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "position", Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            list = new ArrayList<>();
            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");
                if(result > 0) {
                    JSONArray array = json.getJSONArray("list");
                    int size = array.length();

                    for(int i=0; i<size; i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        list.add(jsonObject.getString("image_url"));
                    }
                    adapter = new ImageGridAdapter(getActivity().getApplicationContext(), R.layout.row, list);
                    gv.invalidateViews();
                    gv.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        }
    }

}