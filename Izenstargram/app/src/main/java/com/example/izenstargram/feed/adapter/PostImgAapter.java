package com.example.izenstargram.feed.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.model.PostImage;

import java.util.List;

public class PostImgAapter extends RecyclerView.Adapter<PostImgAapter.ViewHolder> {
    private Context context;
    private List<PostImage> postImageList;

    public PostImgAapter(Context context, List<PostImage> postImageList) {
        this.context = context;
        this.postImageList = postImageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View imgView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_image_item, viewGroup, false); //itemGroup 의 xml inflate
        ViewHolder viewHolder = new ViewHolder(imgView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
//        Glide.with(viewHolder.itemView.getContext())
//                .load(postImageList.get(position).getImage_url())
//                .into(viewHolder.coreImg);
        Glide.with(viewHolder.itemView.getContext())
                .load(R.drawable.like)
                .into(viewHolder.coreImg);
    }

    @Override
    public int getItemCount() {
        return postImageList != null ? postImageList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView coreImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coreImg = itemView.findViewById(R.id.coreImg);
        }

    }
}
