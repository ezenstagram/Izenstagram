package com.example.izenstargram.feed.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
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

    AsyncHttpClient client;
    HttpResponse response; //좋아요 유무 확인용, response 는 0 혹은 1
    FeedLikeResponse feedLikeResponse; //좋아요 데이터 저장 혹은 삭제용 response
    ArrayList<PostAll> feedPostList; //itemGroup
    Context context;
    String url = "http://192.168.0.5:8080/project/chkLikes.do";
    String url_like_save = "http://192.168.0.5:8080/project/saveLikes.do";
    String url_like_delete = "http://192.168.0.5:8080/project/delLikes.do";
    static int mode = 0;
    public void setItems(List<PostAll> feedPostList) {
        this.feedPostList = (ArrayList<PostAll>) feedPostList;
    }

    public FeedAdapter(ArrayList<PostAll> feedPostList, Context context) {
        Log.d("[INFO]", "FeedAdapter(ArrayList<PostAll> feedPostList, Context context) 생성자 시작");
        this.feedPostList = feedPostList;
        this.context = context;
        Log.d("[INFO]", "FeedAdapter(ArrayList<PostAll> feedPostList, Context context) 생성자 끝");
    }


    @Override
    //this view will be called whenever our viewHolder is created(Rayout_xml(한줄화면)inflate 시킬때)
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //parent, i : viewType
        Log.d("[INFO]", "onCreateViewHolder() 시작");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false); //itemGroup 의 xml inflate
        ViewHolder viewHolder = new ViewHolder(view);
        client = new AsyncHttpClient();
        response = new HttpResponse(viewHolder.heart_btn);
        feedLikeResponse = new FeedLikeResponse(viewHolder, mode);
        Log.d("[INFO]", "onCreateViewHolder() 끝");
        return viewHolder;
    }

    @Override //다른 뷰에 보여질 때마다 데이터를 viewHolder 에 bind 시킴
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Log.d("[INFO]", "onBindViewHolder() 시작");

        viewHolder.feed_user_id.setText(String.valueOf(feedPostList.get(position).getUser_id()));


        List<PostImage> postImageList = feedPostList.get(position).getPostImageList();   // 게시글은 현재 1개
        final PostImgAapter imgItemAdapter = new PostImgAapter(context, postImageList);
        viewHolder.recyclerView_img_item.setHasFixedSize(true);
        viewHolder.recyclerView_img_item.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.recyclerView_img_item.setAdapter(imgItemAdapter);
        viewHolder.recyclerView_img_item.setNestedScrollingEnabled(false);
        if (feedPostList.get(position).getPostImageList() != null) { //이미지가 있을 때,
            Log.d("[COUNT]", "첫번째 게시글 갯수: (1개여야야함)" + postImageList.get(0).getImage_id());

        }

        viewHolder.feedTxtLikes.setText(String.valueOf(feedPostList.get(position).getComment_cnt())); //댓글총갯수
        viewHolder.feedTxtCmt.setText(feedPostList.get(position).getContent()); //댓글내용
        Log.d("[INFO]", "onBindViewHolder() 끝");

        /* 피드 좋아요 버튼 꾸욱 */
        viewHolder.heart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("#########################################################");
                //좋아요 토글 체크유무저장
            boolean chk_heart = viewHolder.heart_btn.isChecked();

            if(chk_heart==true){ //이때 데이터 저장
                RequestParams params = new RequestParams();
                params.put("post_id", 1);
                params.put("user_id", 3);
                client.post(url, params, response);
            }
            if(chk_heart==false){ //이때 데이터 삭제
                RequestParams params = new RequestParams();
                params.put("post_id", 1);
                params.put("user_id", 3);
                client.post(url, params, response);

            }


            }
        });

        /* 피드 댓글 기능 */
         viewHolder.cmt_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                Fragment CommentFragment = new Fragment();
               // replaceFragment(R.id.frame_layout, CommentFragment);
         }
         });


    }

    @Override
    public int getItemCount() {
        Log.d("[INFO]", "getItemCount() 시작");
        Log.d("[INFO]", "getItemCount() 갯수 : " + feedPostList.size());

        return (feedPostList != null ? feedPostList.size() : 0);

    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView feed_profile_Img; //프로필이미지
        TextView feed_user_id; //유저아이디
        RecyclerView recyclerView_img_item;  //list_layout 의 recyclerView
        TextView feedTxtLikes;
        TextView feedTxtCmt;
        ToggleButton heart_btn;
        ImageView cmt_btn;

        @SuppressLint("ResourceType")
        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("[INFO]", "ViewHolder(View itemView) 진입");
            feed_profile_Img = itemView.findViewById(R.id.feed_profile_Img);
            feed_user_id = itemView.findViewById(R.id.feed_user_id);
            recyclerView_img_item = itemView.findViewById(R.id.recyclerView_img_item);

            heart_btn = itemView.findViewById(R.id.heart_btn);
            heart_btn.setChecked(false);
            cmt_btn =itemView.findViewById(R.id.cmt_btn);

            feedTxtLikes = itemView.findViewById(R.id.feedTxtLikes);
            feedTxtCmt = itemView.findViewById(R.id.feedTxtCmt);


        }
    }


    class HttpResponse extends AsyncHttpResponseHandler{
       ToggleButton heart_btn;


        public HttpResponse(ToggleButton heart_btn) {
            this.heart_btn = heart_btn;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.d("[INFO]", "좋아요 유무 확인하는 처리에 들어가나?");
            String document = new String(responseBody);
            RequestParams params =  new RequestParams();
            params.put("post_id", 1);
            params.put("user_id", 3);
            try {
                JSONObject json = new JSONObject(document);
                int data = json.getInt("data");
                Log.d("[INFO] data = ",  String.valueOf(data));
                if(data>0){  //좋아요 데이터가 존재할 시, 하트체크해제 + 데이터 저장 취소
                   // heart_btn.setChecked(false);
                    mode = 1;
                    client.post(url_like_delete, params, feedLikeResponse); //서로 다른 response 이기 때문에
                    heart_btn.setChecked(false);
                    Log.d("[INFO]", "좋아요 데이터 저장성공");

                }else{
                    //heart_btn.setChecked(true); //좋아요 데이터가 존재하지 않을 시, 데이터 저장
                    mode = 2;
                    client.post(url_like_save, params, feedLikeResponse);
                    heart_btn.setChecked(true);
                    Log.d("[INFO]", "좋아요 데이터 삭제 완료");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(context, "좋아요 데이터 확인 유무 실패", Toast.LENGTH_SHORT).show();
        }
    }



    
}

