package com.example.izenstargram.feed.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.model.Comments;
import com.example.izenstargram.helper.SpannableStringMaker;
import com.example.izenstargram.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {
    Activity activity;
    List<Comments> commentsList;
    int comment_position;

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
        Map<String, Fragment> clickStrMap = new HashMap<>(); //댓글이름눌렀을 때 프로필로 넘어가는 기능
        clickStrMap.put(commentsList.get(position).getUserDTO().getLogin_id(), new ProfileFragment());  //눌렀을 때 이동하는
        String str = commentsList.get(position).getUserDTO().getLogin_id() +  "  " + commentsList.get(position).getComment_cmt();
        viewHolder.cmt_profile_id.setText(SpannableStringMaker.getInstance(activity).makeSpannableString(str, clickStrMap, "list"));
        viewHolder.cmt_reg_date.setText("   " + commentsList.get(position).getReg_date());
        Glide.with(viewHolder.itemView.getContext())
                .load(commentsList.get(position).getUserDTO().getProfile_photo())
                .asBitmap()
                .into(viewHolder.cmt_profile_img);
       comment_position = viewHolder.getAdapterPosition();

    }

    @Override
    public int getItemCount() {
        return (commentsList != null ? commentsList.size() : 0);
    }

    public void notifyItemInserted(ArrayList<Comments> commentList) {
        this.commentsList = commentList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView cmt_profile_img;
        TextView cmt_profile_id;
       // TextView cmt_comment;
        TextView cmt_reg_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cmt_profile_img = itemView.findViewById(R.id.cmt_profile_img);
            cmt_profile_id = itemView.findViewById(R.id.cmt_profile_id);
          //  cmt_comment = itemView.findViewById(R.id.cmt_comment);
            cmt_reg_date =itemView.findViewById(R.id.cmt_reg_date);

        }
    }

}
