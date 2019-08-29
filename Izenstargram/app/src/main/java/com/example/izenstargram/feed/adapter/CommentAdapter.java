package com.example.izenstargram.feed.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.example.izenstargram.feed.Interface.OnItemClick;
import com.example.izenstargram.feed.model.Comments;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements OnItemClick {

    List<Comments> commentsList;
    Context context;
    private  OnItemClick mCallback;

    public CommentAdapter(List<Comments> commentsList, Context context, OnItemClick mCallback) {
        this.commentsList = commentsList;
        this.context = context;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(String value) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
