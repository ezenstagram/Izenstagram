package com.example.izenstargram.feed.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.Interface.IItemClickListener;
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
        Glide.with(viewHolder.itemView.getContext())
                .load(postImageList.get(position).getImage_url())
                .into(viewHolder.coreImg);
           viewHolder.setItemClickListener(new IItemClickListener(){
               @Override
               public void onItemClickListener(View view, int position) {
                   Toast.makeText(context, ""+ postImageList.get(position).getImage_url(), Toast.LENGTH_SHORT).show();
               }
           });
    }

    @Override
    public int getItemCount() {
        return postImageList != null ? postImageList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       ImageView coreImg;
       IItemClickListener itemClickListener;

        public void  setItemClickListener(IItemClickListener itemClickListener){
           this.itemClickListener = itemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coreImg = itemView.findViewById(R.id.coreImg);
            itemView.setOnClickListener(this);
        }

        @Override  //make ViewHolder implement android.view.View.OnClickListener
        public void onClick(View view) {
              itemClickListener.onItemClickListener(view, getAdapterPosition());
        }
    }
}
