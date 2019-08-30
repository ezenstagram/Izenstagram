package com.example.izenstargram.feed.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.bumptech.glide.Glide;
import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.model.PostAll;
import com.example.izenstargram.feed.model.PostImage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>  {
    MainActivity mainActivity = new MainActivity();
    int user_id;
    AsyncHttpClient client;
    HttpResponse response; //좋아요 유무 확인용, response 는 0 혹은 1
    FeedLikeResponse feedLikeResponse; //좋아요 데이터 저장 혹은 삭제용 response
    ArrayList<PostAll> feedPostList; //itemGroup
    Context context;
    String url = "http://192.168.0.62:8080/project/chkLikes.do";
    String url_like_save = "http://192.168.0.62:8080/project/saveLikes.do";
    String url_like_delete = "http://192.168.0.62:8080/project/delLikes.do";

    int mode = 0;
    public void setItems(List<PostAll> feedPostList) {
        this.feedPostList = (ArrayList<PostAll>) feedPostList;
    }

    public FeedAdapter(ArrayList<PostAll> feedPostList, Context context, int user_id) {
        this.feedPostList = feedPostList;
        this.context = context;
        this.user_id = user_id;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //parent, i : viewType
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false); //itemGroup 의 xml inflate
        ViewHolder viewHolder = new ViewHolder(view);
        client = new AsyncHttpClient();
        feedLikeResponse = new FeedLikeResponse(viewHolder, mode);

        return viewHolder;
    }

    @Override //다른 뷰에 보여질 때마다 데이터를 viewHolder 에 bind 시킴
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if(feedPostList.get(position).isLike()){
            viewHolder.heart_btn.setChecked(true);
        }else {
            viewHolder.heart_btn.setChecked(false);
        }
        response = new HttpResponse(viewHolder.heart_btn, position);
        viewHolder.feed_cnt_likes.setText("좋아요 " + feedPostList.get(position).getLikes_cnt() + "개");
        viewHolder.feed_txt_comment.setText("댓글 " + feedPostList.get(position).getComment_cnt() + "개 모두 보기"); //댓글총갯수
        viewHolder.feed_txt_content.setText(feedPostList.get(position).getContent()); //댓글내용
        viewHolder.feed_login_id.setText(feedPostList.get(position).getUserDTO().getLogin_id()); //글쓴사람
        Glide.with(viewHolder.itemView.getContext())
                .load(feedPostList.get(position).getUserDTO().getProfile_photo())
                .into(viewHolder.feed_profile_Img);



        List<PostImage> postImageList = feedPostList.get(position).getPostImageList();   // 게시글은 현재 1개
        final PostImgAapter imgItemAdapter = new PostImgAapter(context, postImageList);
        viewHolder.recyclerView_img_item.setHasFixedSize(true);
        viewHolder.recyclerView_img_item.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.recyclerView_img_item.setAdapter(imgItemAdapter);
        viewHolder.recyclerView_img_item.setNestedScrollingEnabled(false);


        /* 피드 좋아요 버튼 꾸욱 */
        viewHolder.heart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //좋아요 토글 체크유무저장
                RequestParams params = new RequestParams();
                params.put("post_id", feedPostList.get(position).getPost_id());
                params.put("user_id", user_id);
                response = new HttpResponse(viewHolder.heart_btn, position); //*IMPORTANT//
                client.post(url, params, response);
            }
        });

        /* 피드 댓글 기능 */
        viewHolder.cmt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("댓글기능 구현 시작");

            }
        });


    }

    @Override
    public int getItemCount() {

        return (feedPostList != null ? feedPostList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView feed_profile_Img; //프로필이미지
        TextView feed_login_id; //유저아이디
        RecyclerView recyclerView_img_item;  //피드 사진을 위한 뷰
        TextView feed_cnt_likes; //좋아요 갯수
        TextView feed_txt_content; //피드 포스트 글내용
        TextView feed_txt_comment; //댓글내용
        ToggleButton heart_btn;
        ImageView cmt_btn;

        @SuppressLint("ResourceType")
        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("[INFO]", "ViewHolder(View itemView) 진입");
            feed_profile_Img = itemView.findViewById(R.id.feed_profile_Img);
            feed_login_id = itemView.findViewById(R.id.feed_login_id);
            recyclerView_img_item = itemView.findViewById(R.id.recyclerView_img_item);
            heart_btn = itemView.findViewById(R.id.heart_btn);

            cmt_btn =itemView.findViewById(R.id.cmt_btn);
            feed_cnt_likes = itemView.findViewById(R.id.feed_cnt_likes);
            feed_txt_content = itemView.findViewById(R.id.feed_txt_content);
            feed_txt_comment = itemView.findViewById(R.id.feed_txt_comment);

        }
    }


    class HttpResponse extends AsyncHttpResponseHandler{
        ToggleButton heart_btn;
        int position;

        public HttpResponse(ToggleButton heart_btn, int position) {
            this.heart_btn = heart_btn;
            this.position = position;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String document = new String(responseBody);
            RequestParams params =  new RequestParams();
            params.put("post_id", feedPostList.get(position).getPost_id());
            params.put("user_id", user_id);

            try {
                JSONObject json = new JSONObject(document);
                int data = json.getInt("data");

                if(data>0){  //-->좋아요 데이터가 존재할 때, 없애주는 처리
                    mode = 1;
                    client.post(url_like_delete, params, feedLikeResponse); //서로 다른 response 이기 때문에

                }else{
                    mode = 2; //-->좋아요 데이터가 없을때, 저장해주는 처리
                    client.post(url_like_save, params, feedLikeResponse);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(context, "좋아요 데이터 검사 실패", Toast.LENGTH_SHORT).show();
        }
    }
}