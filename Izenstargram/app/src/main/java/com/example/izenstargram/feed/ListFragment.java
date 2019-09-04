package com.example.izenstargram.feed;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.adapter.FeedAdapter;
import com.example.izenstargram.feed.model.PostAll;
import com.example.izenstargram.feed.model.PostImage;
import com.example.izenstargram.profile.UserDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class ListFragment extends Fragment {
    Activity activity;
    String url = "http://192.168.0.62:8080/project/feedPostList.do";
    AsyncHttpClient client;
    HttpResponse response;
    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private ArrayList<PostAll> feedPostList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.list_layout, container, false); // attachToRoot는 일단 false로..
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new FeedAdapter(feedPostList, activity, getArguments().getInt("list_user_id", 0));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        RequestParams params = new RequestParams();
        params.put("user_id", getArguments().getInt("list_user_id", 0));
        clear();
        client.get(url, params, response);
        recyclerView.setAdapter(adapter);

    }

    public void clear() {
        int size = feedPostList.size();
        feedPostList.clear();
        adapter.notifyItemRangeRemoved(size, 0);
    }




    class HttpResponse extends AsyncHttpResponseHandler{
        Activity activity;

        public HttpResponse(Activity activity) {
            this.activity  = activity;
        }


        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String document = new String(responseBody);
            try {

                JSONObject json = new JSONObject(document);
                JSONArray data = json.getJSONArray("data");
                for(int i=0; i<data.length(); i++){

                    JSONObject feedItem = data.getJSONObject(i);
                    PostAll postAll = new PostAll();
                    postAll.setPost_id(feedItem.getInt("post_id"));
                    postAll.setUser_id(feedItem.getInt("user_id"));
                    postAll.setContent(feedItem.getString("content"));
                    postAll.setLocation(feedItem.getString("location"));
                    postAll.setReg_date(feedItem.getString("reg_date"));
                    postAll.setLike(feedItem.getBoolean("like"));
                    postAll.setLikes_cnt(feedItem.getInt("likes_cnt"));
                    postAll.setComment_cnt(feedItem.getInt("comment_cnt"));
                    JSONArray tempPostImageList = feedItem.getJSONArray("postImageList");
                    JSONObject tempUserDTO =  feedItem.getJSONObject("userDTO");

                    ArrayList<PostImage> imgList = new ArrayList<>();
                    for(int j=0; j<tempPostImageList.length(); j++){    //object 갯수가 3개니까 3번 돌음

                        JSONObject feedImageItem = tempPostImageList.getJSONObject(j); //imageList에 있는 하나의 object
                        PostImage postImage = new PostImage();
                        postImage.setPost_id(feedImageItem.getInt("post_id"));
                        postImage.setImage_id(feedImageItem.getInt("image_id"));
                        postImage.setImage_url(feedImageItem.getString("image_url"));
                        imgList.add(postImage);
                    }
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUser_id(tempUserDTO.getInt("user_id"));
                    userDTO.setLogin_id(tempUserDTO.getString("login_id"));
                    userDTO.setName(tempUserDTO.getString("name"));
                    userDTO.setPassword(tempUserDTO.getString("password"));
                    userDTO.setProfile_photo(tempUserDTO.getString("profile_photo"));
                    userDTO.setWebsite(tempUserDTO.getString("website"));
                    userDTO.setIntroduction(tempUserDTO.getString("introduction"));
                    userDTO.setEmail(tempUserDTO.getString("email"));
                    userDTO.setGender(tempUserDTO.getString("gender"));

                    postAll.setPostImageList(imgList);
                    postAll.setUserDTO(userDTO);
                    feedPostList.add(postAll);

                    //adapter의 데이터가 바뀔때마다
                    adapter.setItems(feedPostList);
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getContext(), "연결실패", Toast.LENGTH_SHORT).show();
        }
    }


}


