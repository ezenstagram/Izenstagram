package com.example.izenstargram.like.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.helper.SpannableStringMaker;
import com.example.izenstargram.like.bean.NotifiInfo;
import com.example.izenstargram.profile.ProfileFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserRecyclerHolder> {

    private ArrayList<NotifiInfo> list;
    Activity activity;
    // server
    AsyncHttpClient client;
    FollowResponse response;
    String URL = "http://192.168.0.13:8080/project/follow.do";


    public class UserRecyclerHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView1;
        protected TextView textView;
        protected ImageView imageView2;
        protected ToggleButton button;

        public UserRecyclerHolder(View view) {
            super(view);
            this.imageView1 = (ImageView) view.findViewById(R.id.imageView1);
            this.textView = (TextView) view.findViewById(R.id.textView);
            this.imageView2 = (ImageView) view.findViewById(R.id.imageView2);
            this.button = (ToggleButton) view.findViewById(R.id.toggleButton);
        }
    }

    public UserRecyclerAdapter(ArrayList<NotifiInfo> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.like_users_list_item, viewGroup, false);
        UserRecyclerHolder viewHolder = new UserRecyclerHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerHolder userRecyclerHolder, int i) {
        if (list.size() > 0) {
            final NotifiInfo notifiInfo = list.get(i);

            // 팔로우 버튼 초기화
            userRecyclerHolder.button.setVisibility(View.GONE);
            userRecyclerHolder.imageView2.setVisibility(View.VISIBLE);

            String str = "";
            Map<String, Fragment> clickStrMap = new HashMap<>();
            switch (notifiInfo.getMode_id()) {
                case 1: // 좋아요 알림
                    clickStrMap.put(notifiInfo.getAct_login_id(), new ProfileFragment());
                    str = notifiInfo.getAct_login_id() + "님이 회원님의 게시물을 좋아합니다.";
                    break;
                case 2: // 태그 알림
                    clickStrMap.put(notifiInfo.getAct_login_id(), new ProfileFragment());
                    str = notifiInfo.getAct_login_id() + "님이 댓글에서 회원님을 태그했습니다. : " + notifiInfo.getComment_txt();
                    break;
                case 3: // 팔로우 알림
                    userRecyclerHolder.button.setVisibility(View.VISIBLE);
                    userRecyclerHolder.imageView2.setVisibility(View.GONE);
                    if (notifiInfo.isFollow_flg()) {
                        userRecyclerHolder.button.setChecked(true);
                        userRecyclerHolder.button.setBackgroundColor(Color.WHITE);
                        userRecyclerHolder.button.setTextColor(Color.BLACK);
                    } else {
                        userRecyclerHolder.button.setChecked(false);
                        userRecyclerHolder.button.setBackgroundColor(Color.rgb(0, 153, 204));
                        userRecyclerHolder.button.setTextColor(Color.WHITE);
                    }
                    userRecyclerHolder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            client = new AsyncHttpClient();
                            response = new FollowResponse(activity);
                            RequestParams params = new RequestParams();
                            params.put("user_id", notifiInfo.getAct_user_id());
                            params.put("user_id_owner", notifiInfo.getTarget_id());
                            params.put("sign", (!isChecked) ? 1 : 0);
                            client.post(URL, params, response);
                            if (isChecked) {
                                buttonView.setChecked(true);
                                buttonView.setBackgroundColor(Color.WHITE);
                                buttonView.setTextColor(Color.BLACK);
                            } else {
                                buttonView.setChecked(false);
                                buttonView.setBackgroundColor(Color.rgb(0, 153, 204));
                                buttonView.setTextColor(Color.WHITE);
                            }
                        }
                    });
                    clickStrMap.put(notifiInfo.getAct_login_id(), new ProfileFragment());
                    str = notifiInfo.getAct_login_id() + "님이 회원님을 팔로우하기 시작했습니다.";
                    break;
            }
            // 유저 태그가 있는지 없는지 검사
            List<String> strList = SpannableStringMaker.getInstance(activity).getUserTagList(str);
            for (String uerTagList : strList) {
                clickStrMap.put(uerTagList, new ProfileFragment());
            }
            // 링크걸어진 text 작성
            userRecyclerHolder.textView.setText(SpannableStringMaker.getInstance(activity).makeSpannableString(str, clickStrMap, "like"));
            userRecyclerHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());

            Glide.with(userRecyclerHolder.itemView.getContext())
                    .load(notifiInfo.getProfile_photo())
                    .into(userRecyclerHolder.imageView1);
            userRecyclerHolder.imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) activity).replaceFragment(R.id.frame_layout, new ProfileFragment(), "like");
                }
            });
            if (notifiInfo.getMode_id() == 1 || notifiInfo.getMode_id() == 2) {
                Glide.with(userRecyclerHolder.itemView.getContext())
                        .load(notifiInfo.getPost_image())
                        .into(userRecyclerHolder.imageView2);
            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class FollowResponse extends AsyncHttpResponseHandler {

        Activity activity;

        public FollowResponse(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int result = jsonObject.getInt("result");
                if (result <= 0) {
                    Toast.makeText(activity, "실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "접속실패", Toast.LENGTH_SHORT).show();
        }
    }

}
