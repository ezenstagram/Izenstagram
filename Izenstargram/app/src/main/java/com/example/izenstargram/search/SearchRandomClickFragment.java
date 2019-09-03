package com.example.izenstargram.search;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.model.PostAll;
import com.example.izenstargram.feed.model.PostImage;
import com.example.izenstargram.helper.SpannableStringMaker;
import com.example.izenstargram.profile.UserDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRandomClickFragment extends Fragment {

    String url = "http://192.168.0.62:8080/project/feedPostOne.do";
    AsyncHttpClient client;
    SelectOneHttpResponse selectOneHttpResponse;
    CircleImageView feed_profile_Img;
    TextView feed_login_id;
    TextView feed_cnt_likes;
    TextView feed_txt_content;
    TextView feed_txt_comment;
    ImageView select_one_image;  //이미지용
    private PostAll postOne = new PostAll();
    String imgurl = null;

    int post_id;
    int user_id;

    Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("[INFO]", "SearchRandomClickFrag : onCreateView()");
        View view = inflater.inflate(R.layout. listone_item, container, false); // attachToRoot는 일단 false로..
        activity = getActivity();


        if(getArguments() != null) {
            post_id = getArguments().getInt("post_id", 0);
        }
        user_id = getArguments().getInt("user_id", 0);
        feed_profile_Img = view.findViewById(R.id.feed_profile_Img);
        feed_login_id = view.findViewById(R.id.feed_login_id);
        feed_cnt_likes =view.findViewById(R.id.feed_cnt_likes);
        feed_txt_content =view.findViewById(R.id.feed_txt_content);
        feed_txt_comment = view.findViewById(R.id.feed_txt_comment);
        select_one_image = view.findViewById(R.id.select_one_image);
        client = new AsyncHttpClient();
        selectOneHttpResponse = new SelectOneHttpResponse();

        return view;
    }

    @Override
    public void onResume() {
        Log.d("[INFO]", "SearchRandomClickFrag : onResume()");
        super.onResume();
        RequestParams params = new RequestParams();
        params.put("post_id", post_id);
        params.put("user_id", user_id);
        //params.put("user_id", getArguments().getInt("list_user_id", 0)); //이쪽에 보드아이디 저장하나 ?
        client.get(url, params, selectOneHttpResponse);
    }

    class SelectOneHttpResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String document = new String(responseBody);
            try {

                JSONObject json = new JSONObject(document);
                JSONObject data = json.getJSONObject("data");

                postOne.setPost_id(data.getInt("post_id"));
                postOne.setUser_id(data.getInt("user_id"));
                postOne.setContent(data.getString("content"));
                postOne.setLocation(data.getString("location"));
                postOne.setReg_date(data.getString("reg_date"));
                JSONArray tempImgList = data.getJSONArray("postImageList");
                for(int i=0; i<tempImgList.length();i++){
                    JSONObject imageList = tempImgList.getJSONObject(i);  //한 게시글 당 여러개 이미지
                    PostImage postImage = new PostImage();
                    postImage.setPost_id(imageList.getInt("post_id"));
                    postImage.setImage_id(imageList.getInt("image_id"));
                    postImage.setImage_url(imageList.getString("image_url"));
                    imgurl = postImage.getImage_url();
                }

                postOne.setComment_cnt(data.getInt("comment_cnt"));
                JSONObject tempUserDTO = data.getJSONObject("userDTO");
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
                postOne.setLikes_cnt(data.getInt("likes_cnt"));
                postOne.setLike(data.getBoolean("like"));
                postOne.setUserDTO(userDTO);
                feed_login_id.setText(postOne.getUserDTO().getLogin_id());
                feed_cnt_likes.setText("좋아요 " + postOne.getLikes_cnt() + "개");





                Map<String, Fragment> clickStrMap = new HashMap<>();
                String str = postOne.getContent();
                Log.d("[INFO]", "SearchRandomClickFrag : postOne_getContent() = " + postOne.getContent());
//                // 유저 태그가 있는지 없는지 검사
//                List<String> strListForUser = SpannableStringMaker.getInstance(activity).getUserTagList(str);
//                for (String userTagList : strListForUser) {
//                    SearchUserClickFragment fragment = new SearchUserClickFragment();
//                    clickStrMap.put(userTagList, fragment);
//                }
//                // 링크걸어진 text 작성
//                feed_txt_content.setText(SpannableStringMaker.getInstance(activity).makeSpannableString(str, clickStrMap, "search"));
//                feed_txt_content.setMovementMethod(LinkMovementMethod.getInstance());

                // 해시 태그가 있는지 없는지 검사
                List<String> strListForTag = SpannableStringMaker.getInstance(activity).getHashTagList(str);
                for (String hashTagList : strListForTag) {  // 링크달기
                    //SearchUserClickFragment fragment = new SearchUserClickFragment();
                    //clickStrMap.put(hashTagList, fragment);
                    Fragment newNew = clickStrMap.get(hashTagList);
                }




                출처: https://mainia.tistory.com/2237 [녹두장군 - 상상을 현실로]
                feed_txt_content.setText(SpannableStringMaker.getInstance(activity).makeSpannableString(str, clickStrMap, "search"));
                feed_txt_content.setMovementMethod(LinkMovementMethod.getInstance());











                //feed_txt_content.setText(postOne.getContent());
                feed_txt_comment.setText("댓글" + postOne.getComment_cnt() + "개 모두 보기");
                Glide.with(getActivity())
                        .load(postOne.getUserDTO().getProfile_photo())
                        .into(feed_profile_Img);
                ImageLoader.getInstance().displayImage(imgurl, select_one_image);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getContext(), "연결실패", Toast.LENGTH_SHORT).show();
        }
    }
}
