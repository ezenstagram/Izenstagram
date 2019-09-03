package com.example.izenstargram.feed;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.feed.adapter.CommentAdapter;
import com.example.izenstargram.feed.model.Comments;

import com.example.izenstargram.profile.UserDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "http://192.168.0.5:8080/project/cmtList.do"; //댓글 목록보기
    String url_insert_comment = "http://192.168.0.5:8080/project/saveCmts.do"; //댓글 저장하기
    AsyncHttpClient client;
    CommentHttpResponse response;
    CommentInsertHttpResponse commentInsertHttpResponse;
    private CommentAdapter commentAdapter;
    private ArrayList<Comments> commentList = new ArrayList<>();
    int post_position;
    int user_id;
    RecyclerView comment_list_view1;
    CircleImageView insert_cmt_profile_image;
    EditText insert_cmt_cmt;
    Button insert_cmt_ok_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        post_position= getIntent().getIntExtra("post_id", 0);
        user_id = getIntent().getIntExtra("user_id",0);
        comment_list_view1 = findViewById(R.id.comment_list_view1);
        insert_cmt_profile_image = findViewById(R.id.insert_cmt_profile_image);
        insert_cmt_cmt = findViewById(R.id.insert_cmt_cmt);
        insert_cmt_ok_button = findViewById(R.id.insert_cmt_ok_button);
        comment_list_view1.setHasFixedSize(true);
        commentAdapter = new CommentAdapter(this, commentList);
        comment_list_view1.setLayoutManager(new LinearLayoutManager(this));
        client = new AsyncHttpClient();
        response = new CommentHttpResponse(this);
        commentInsertHttpResponse = new CommentInsertHttpResponse();
        insert_cmt_ok_button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RequestParams params = new RequestParams();
        params.put("post_id",post_position); //게시글 받아옴
        clear();
        client.post(url, params, response);
        comment_list_view1.setAdapter(commentAdapter);
    }

    private void clear() {
        int size = commentList.size();
        commentList.clear();
        commentAdapter.notifyItemRangeRemoved(size, 0);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.insert_cmt_ok_button){
            String comment = String.valueOf(insert_cmt_cmt.getText());
            RequestParams params = new RequestParams();
            Comments comments = new Comments();
            comments.setPost_id(post_position);
            comments.setComment_cmt(comment);
            comments.setUser_id(user_id);
            params.put("post_id", comments.getPost_id());
            params.put("user_id", comments.getUser_id());
            params.put("comment_cmt", comments.getComment_cmt());
            client.post(url_insert_comment, params, commentInsertHttpResponse);

        }
    }

    //얘는 댓글출력 하는 애
    class CommentHttpResponse extends AsyncHttpResponseHandler {
        Activity activity;

        public CommentHttpResponse(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            System.out.println("onSuccess 에 진입을 하나");
            String document = new String(responseBody);
            try {
                System.out.println("onSuccess 에 진입을 하나?222");
                JSONObject json = new JSONObject(document);
                JSONArray data = json.getJSONArray("data");

                for(int i=0; i<data.length(); i++){
                    JSONObject tempCmtObject = data.getJSONObject(i);
                    Comments comments = new Comments();
                    comments.setPost_id(tempCmtObject.getInt("post_id"));
                    comments.setUser_id(tempCmtObject.getInt("user_id"));
                    comments.setComment_id(tempCmtObject.getInt("comment_id"));
                    comments.setComment_cmt(tempCmtObject.getString("comment_cmt"));
                    comments.setReg_date(tempCmtObject.getString("reg_date"));
                    JSONObject tempUserDTO = tempCmtObject.getJSONObject("userDTO");
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUser_id(tempUserDTO.getInt("user_id"));
                    userDTO.setName(tempUserDTO.getString("name"));
                    userDTO.setLogin_id(tempUserDTO.getString("login_id"));
                    userDTO.setPassword(tempUserDTO.getString("password"));
                    userDTO.setProfile_photo(tempUserDTO.getString("profile_photo"));
                    userDTO.setWebsite(tempUserDTO.getString("website"));
                    userDTO.setIntroduction(tempUserDTO.getString("introduction"));
                    userDTO.setEmail(tempUserDTO.getString("email"));
                    userDTO.setTel(tempUserDTO.getString("tel"));
                    userDTO.setGender(tempUserDTO.getString("gender"));
                    //데이터가 바뀔때마다 Adapter 가 알림
                    comments.setUserDTO(userDTO);
                    commentList.add(comments);
                    commentAdapter.setItems(commentList);
                    commentAdapter.notifyDataSetChanged();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }


    class CommentInsertHttpResponse  extends  AsyncHttpResponseHandler{

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String document = new String(responseBody);
            try {
                JSONObject json  = new JSONObject(document);
                int result = json.getInt("data");
                if(result>0){
                    Toast.makeText(getApplicationContext(), "댓글작성을 완료하였습니다. ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }
}
