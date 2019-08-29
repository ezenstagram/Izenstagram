package com.example.izenstargram.like.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.helper.SpannableStringMaker;
import com.example.izenstargram.like.bean.NotifiInfo;
import com.example.izenstargram.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FInfoRecyclerAdapter extends RecyclerView.Adapter<FInfoRecyclerAdapter.FInfoRecyclerHolder> {

    private ArrayList<NotifiInfo> list;
    Activity activity;

    public class FInfoRecyclerHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView1;
        protected TextView textView;
        protected ImageView imageView2;
        protected ToggleButton button; // 표시되지 않음

        public FInfoRecyclerHolder(View view) {
            super(view);
            this.imageView1 = (ImageView) view.findViewById(R.id.imageView1);
            this.textView = (TextView) view.findViewById(R.id.textView);
            this.imageView2 = (ImageView) view.findViewById(R.id.imageView2);
            this.button = (ToggleButton) view.findViewById(R.id.toggleButton); // 표시되지 않게 설정
            this.button.setVisibility(View.GONE);
        }
    }

    public FInfoRecyclerAdapter(ArrayList<NotifiInfo> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FInfoRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.like_users_list_item, viewGroup, false);
        FInfoRecyclerHolder viewHolder = new FInfoRecyclerHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FInfoRecyclerHolder fInfoRecyclerHolder, int i) {
        if (list.size() > 0) {
            final NotifiInfo notifiInfo = list.get(i);

            // 팔로우 버튼 초기화
            fInfoRecyclerHolder.button.setVisibility(View.GONE);
            fInfoRecyclerHolder.imageView2.setVisibility(View.VISIBLE);

            String str = "";
            Map<String, Fragment> clickStrMap = new HashMap<>();
            switch (notifiInfo.getMode_id()) {
                case 1: // 좋아요 알림
                    clickStrMap.put(notifiInfo.getAct_login_id(), new ProfileFragment());
                    clickStrMap.put(notifiInfo.getTarget_login_id(), new ProfileFragment());
                    str = notifiInfo.getAct_login_id() + "님이 " + notifiInfo.getTarget_login_id() + "님의 게시물을 좋아합니다.";
                    break;
                case 3: // 팔로우 알림
                    clickStrMap.put(notifiInfo.getAct_login_id(), new ProfileFragment());
                    clickStrMap.put(notifiInfo.getTarget_login_id(), new ProfileFragment());
                    str = notifiInfo.getAct_login_id() + "님이 " + notifiInfo.getTarget_login_id() + "님을 팔로우하기 시작했습니다.";
                    break;
            }
            // 유저 태그가 있는지 없는지 검사
            List<String> strList = SpannableStringMaker.getInstance(activity).getUserTagList(str);
            for (String uerTagList : strList) {
                clickStrMap.put(uerTagList, new ProfileFragment());
            }
            // 링크걸어진 text 작성
            fInfoRecyclerHolder.textView.setText(SpannableStringMaker.getInstance(activity).makeSpannableString(str, clickStrMap, "like"));
            fInfoRecyclerHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());

            Glide.with(fInfoRecyclerHolder.itemView.getContext())
                    .load(notifiInfo.getProfile_photo())
                    .into(fInfoRecyclerHolder.imageView1);
            fInfoRecyclerHolder.imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) activity).replaceFragment(R.id.frame_layout, new ProfileFragment(), "like");
                }
            });
            if (notifiInfo.getMode_id() == 1) {
                Glide.with(fInfoRecyclerHolder.itemView.getContext())
                        .load(notifiInfo.getPost_image())
                        .into(fInfoRecyclerHolder.imageView2);
            } else {
                fInfoRecyclerHolder.imageView2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

}
