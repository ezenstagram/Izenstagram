package com.example.izenstargram.like.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.like.bean.NotifiInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserRecyclerHolder> {

    private ArrayList<NotifiInfo> list;
    Activity activity;

    public class UserRecyclerHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView1;
        protected TextView textView;
        protected ImageView imageView2;
        protected Button button;

        public UserRecyclerHolder(View view) {
            super(view);
            this.imageView1 = (ImageView) view.findViewById(R.id.imageView1);
            this.textView = (TextView) view.findViewById(R.id.textView);
            this.imageView2 = (ImageView) view.findViewById(R.id.imageView2);
            this.button = (Button) view.findViewById(R.id.button);
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
            NotifiInfo notifiInfo = list.get(i);
            userRecyclerHolder.button.setVisibility(View.GONE);
            userRecyclerHolder.imageView2.setVisibility(View.VISIBLE);
            String str = "";
            if (notifiInfo.getMode_id() == 1) {
                str = "<a href='#'>" + notifiInfo.getAct_login_id()+"</a>";
                str += "님이 회원님의 게시물을 좋아합니다.";
//                userRecyclerHolder.textView.setText(make(notifiInfo.getAct_login_id()));
//                userRecyclerHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
            } else if (notifiInfo.getMode_id() == 2) {
                str = notifiInfo.getAct_login_id() + "님이 댓글에서 회원님을 태그했습니다. : " + notifiInfo.getComment_txt();
            } else if(notifiInfo.getMode_id() == 3){
                userRecyclerHolder.button.setVisibility(View.VISIBLE);
                userRecyclerHolder.imageView2.setVisibility(View.GONE);
                str = notifiInfo.getAct_login_id() + "님이 회원님을 팔로우하기 시작했습니다.";
            }
            userRecyclerHolder.textView.setText(Html.fromHtml(str));
            Glide.with(userRecyclerHolder.itemView.getContext())
                    .load(notifiInfo.getProfile_photo())
                    .into(userRecyclerHolder.imageView1);
            Glide.with(userRecyclerHolder.itemView.getContext())
                    .load(notifiInfo.getPost_image())
                    .into(userRecyclerHolder.imageView2);
        }
    }



    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }
}
