package com.example.izenstargram.feed.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.model.Comments;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {
    Activity activity;
    List<Comments> commentsList;

    public CommentAdapter(Activity activity, ArrayList<Comments> commentList) {
        this.activity = activity;
        this.commentsList = commentList;
    }

    public void setItems(List<Comments> commentsList) {
        this.commentsList =  commentsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_comment_item, viewGroup, false); //itemGroup 의 xml inflate
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.cmt_profile_id.setText("  " + commentsList.get(position).getUserDTO().getLogin_id());
        viewHolder.cmt_comment.setText("  " + commentsList.get(position).getComment_cmt());
        viewHolder.cmt_reg_date.setText("   " + commentsList.get(position).getReg_date());
        Glide.with(viewHolder.itemView.getContext())
                .load(commentsList.get(position).getUserDTO().getProfile_photo())
                .asBitmap()
                .into(viewHolder.cmt_profile_img);

    }

    @Override
    public int getItemCount() {
        return (commentsList != null ? commentsList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView cmt_profile_img;
        TextView cmt_profile_id;
        TextView cmt_comment;
        TextView cmt_reg_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cmt_profile_img = itemView.findViewById(R.id.cmt_profile_img);
            cmt_profile_id = itemView.findViewById(R.id.cmt_profile_id);
            cmt_comment = itemView.findViewById(R.id.cmt_comment);
            cmt_reg_date =itemView.findViewById(R.id.cmt_reg_date);

        }
    }

}
