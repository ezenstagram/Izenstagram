package com.example.izenstargram.like.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.like.adapter.FInfoRecyclerAdapter;
import com.example.izenstargram.like.bean.NotifiInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowingFragment extends Fragment {

    // server
    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.13:8080/project/followerNotifiInfo.do";

    RecyclerView recyclerView;
    ArrayList<NotifiInfo> list;
    FInfoRecyclerAdapter adapter;
    int user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.like_following_layout, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        user_id = pref.getInt("user_id", 0);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new FInfoRecyclerAdapter(list, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        client = new AsyncHttpClient();
        response = new HttpResponse();
        RequestParams params = new RequestParams();
        params.put("act_user_id", user_id);
        client.get(URL, params, response);
    }

    public class HttpResponse extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int result = jsonObject.getInt("result");
                if (result > 0) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = (JSONObject) array.get(i);
                        NotifiInfo notifiInfo = new NotifiInfo();
                        notifiInfo.setNotifi_id(object.getInt("notifi_id"));
                        notifiInfo.setMode_id(object.getInt("mode_id"));
                        notifiInfo.setPost_image(object.getString("post_image"));
                        notifiInfo.setComment_txt(object.getString("comment_txt"));
                        notifiInfo.setFollow_flg(object.getBoolean("follow_flg"));
                        notifiInfo.setTarget_id(object.getInt("target_id"));
                        notifiInfo.setTarget_sub_id(object.getInt("target_sub_id"));
                        notifiInfo.setAct_user_id(object.getInt("act_user_id"));
                        notifiInfo.setAct_login_id(object.getString("act_login_id"));
                        notifiInfo.setProfile_photo(object.getString("profile_photo"));
                        notifiInfo.setTarget_user_id(object.getInt("target_user_id"));
                        notifiInfo.setTarget_login_id(object.getString("target_login_id"));
                        notifiInfo.setDelete_flg(object.getBoolean("delete_flg"));
                        notifiInfo.setReg_date(object.getString("reg_date"));
                        list.add(notifiInfo); // RecyclerView의 마지막 줄에 삽입
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), "접속실패", Toast.LENGTH_SHORT).show();
        }
    }
}
