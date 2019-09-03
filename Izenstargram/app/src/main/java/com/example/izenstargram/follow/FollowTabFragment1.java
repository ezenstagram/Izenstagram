package com.example.izenstargram.follow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.profile.ProfileFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FollowTabFragment1 extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    List<FollowDTO> list;

    ListView listView;
    FollowAdapter1 adapter1;
    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.32:8080/project/followerList.do";
    PullRefreshLayout loading;

    Button buttonOrder;
    TextView textViewOrder;
    String[] items;
    String orderStatus = "desc";

    RequestParams params;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.follow_tab_fragment1, container, false);

        int user_id = getArguments().getInt("user_id", 0);

        list = new ArrayList<>();
        adapter1 = new FollowAdapter1(getActivity(), R.layout.follower_tab_row, list, user_id);
        listView = view.findViewById(R.id.listView);
        client = new AsyncHttpClient();
        response = new HttpResponse(getActivity(), adapter1);

        buttonOrder = view.findViewById(R.id.buttonOrder);
        textViewOrder = view.findViewById(R.id.textViewOrder);
        listView.setAdapter(adapter1);
        listView.setFocusable(false);
        listView.setOnItemClickListener(this);
        params = new RequestParams();
        params.put("user_id", user_id);
        params.put("sepa", 0);
        client.post(URL,params, response);

        loading= (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        //pullrefresh 스타일 지정
        loading.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        //pullrefresh가 시작됬을 시 호출
        loading.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Thread - 1초 후 로딩 종료
                list.clear();
               client.post(URL,params, response);

                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        buttonOrder.setOnClickListener(this);
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle(1);

        bundle.putInt("user_id",list.get(position).getUser_id());
        fragment.setArguments(bundle);
        ((MainActivity)getActivity()).replaceFragment(R.id.frame_layout, fragment, "profile");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOrder:
                items = new String[]{"기본", "팔로우한 날짜: 최신순", "팔로우한 날짜: 오래된순"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("정렬 기준");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                textViewOrder.setText(items[0]);
                                order(0);
                                break;
                            case 1:
                                textViewOrder.setText(items[1]);
                                order(0);
                                break;
                            case 2:
                                textViewOrder.setText(items[2]);
                                order(1);
                                break;
                        }
                    }
                });
                Dialog dialog = builder.create();
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                dialog.show();

                break;
        }
    }
    public void order(int sepa) {
        if(sepa==0) {
            orderStatus = "desc";
        } else {
            orderStatus="asc";
        }

        client.post(URL,params, response);
    }
    public class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;       // ProgressDialog에서 사용
        FollowAdapter1 adapter1;   // List에 데이터 저장할 때 사용
        ProgressDialog dialog;   // 잠시만기다려쥬

        public HttpResponse(Activity activity, FollowAdapter1 adapter1) {
            this.activity = activity;
            this.adapter1 = adapter1;
        }
        // 통신 시작 시, 자동 호출
        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세요..");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog=null;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            adapter1.clear();
            list.clear();
            String strJson = new String(responseBody);
            List<FollowDTO> arrayList = new ArrayList<>();
            try {
                JSONObject json = new JSONObject(strJson);
                int result = json.getInt("result");
                if(result > 0 ) {
                    JSONArray array = json.getJSONArray("list");
                    for(int i=0; i<array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Boolean followStatus = jsonObject.getBoolean("followStatus");
                        int user_id = jsonObject.getInt("user_id");
                        String name = jsonObject.getString("name");
                        String profile_photo = jsonObject.getString("profile_photo");
                        String login_id = jsonObject.getString("login_id");

                        FollowDTO followDTO = new FollowDTO(followStatus, user_id, name, profile_photo, login_id);
                        arrayList.add(followDTO);
                    }
                    if(orderStatus.equals("desc")) {
                        for(int i=0; i<arrayList.size(); i++) {
                            adapter1.add(arrayList.get(i));
                        }
                    } else {
                        for(int i=arrayList.size()-1; 0<=i; i--) {
                            adapter1.add(arrayList.get(i));
                        }
                    }
                } else {
                    adapter1.clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패", Toast.LENGTH_SHORT).show();
        }
    }
}
