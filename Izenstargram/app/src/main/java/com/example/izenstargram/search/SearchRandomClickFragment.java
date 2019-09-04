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
import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.model.PostAll;
import com.example.izenstargram.feed.model.PostImage;
import com.example.izenstargram.helper.SpannableStringMaker;
import com.example.izenstargram.profile.ProfileFragment;
import com.example.izenstargram.profile.UserDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRandomClickFragment extends Fragment {

    String url = "http://192.168.0.32:8080/project/feedPostOne.do";
    String urlForFindingId = "http://192.168.0.32:8080/project/findUserIdByLoginId.do";
    AsyncHttpClient client;
    SelectOneHttpResponse selectOneHttpResponse;
    HttpResponse response;
    CircleImageView feed_profile_Img;
    TextView feed_login_id;
    TextView feed_cnt_likes;
    TextView feed_txt_content;
    TextView feed_txt_comment;
    ImageView select_one_image;
    private PostAll postOne = new PostAll();
    String imgurl = null;

    int post_id;
    int user_id;
    int writer_id;
    String fullContent;
    Activity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listone_item, container, false); // attachToRoot는 일단 false로..
        activity = getActivity();

        if (getArguments() != null) {
            post_id = getArguments().getInt("post_id", 0);
        }
        user_id = getArguments().getInt("user_id", 0);
        feed_profile_Img = view.findViewById(R.id.feed_profile_Img);
        feed_login_id = view.findViewById(R.id.feed_login_id);
        feed_cnt_likes = view.findViewById(R.id.feed_cnt_likes);
        feed_txt_content = view.findViewById(R.id.feed_txt_content);
        feed_txt_comment = view.findViewById(R.id.feed_txt_comment);
        select_one_image = view.findViewById(R.id.select_one_image);
        client = new AsyncHttpClient();
        selectOneHttpResponse = new SelectOneHttpResponse();
        return view;
    }

    @Override
    public void onResume() {
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
                for (int i = 0; i < tempImgList.length(); i++) {
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
                writer_id = tempUserDTO.getInt("user_id");
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
                fullContent = postOne.getContent();


                feed_login_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ProfileFragment fragment = new ProfileFragment();
                        Bundle bundle = new Bundle(1);

                        bundle.putInt("user_id", writer_id);
                        fragment.setArguments(bundle);

                        ((MainActivity)activity).replaceFragment(R.id.frame_layout, fragment, "search");
                    }
                });

//                Map<String, Fragment> clickStrMap = new HashMap<>();
//                List<String> strList = testForUser(fullContent);
//                Log.d("[INFO]", "SearchRandomClickFrag : testForUser(fullContent).toString : " + strList.toString());
//                for (String userTagList : strList) {
//                    ProfileFragment fragment = new ProfileFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("user_id", user_id);
//                    fragment.setArguments(bundle);
//                    clickStrMap.put(userTagList, fragment);
//                }
//                feed_txt_content.setText(SpannableStringMaker.getInstance(activity).makeSpannableString(fullContent, clickStrMap, "search"));
//                feed_txt_content.setMovementMethod(LinkMovementMethod.getInstance());

                feed_txt_content.setText(postOne.getContent());
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
            //Toast.makeText(getContext(), "연결실패", Toast.LENGTH_SHORT).show();
        }
    }

//    public void letsFindUserId(String fullContent){
//
//
//        Map<String, Fragment> clickStrMap = new HashMap<>();
//        List<String> strList = testForUser(fullContent);
//        Log.d("[INFO]", " testForUser(fullContent).toString : " + strList.toString());
//
//        for (String userTagList : strList) {
//
//            String tagged_login_id = userTagList;
//            RequestParams params = new RequestParams();
//            params.put("tagged_login_id", tagged_login_id);
//            client.post(urlForFindingId, params, response);
//            Log.d("[INFO]", "SearchRandomClickFrag : 두번째 client로 보내는 params : " + tagged_login_id);
//
//
//           // ProfileFragment fragment = new ProfileFragment();
////                    Bundle bundle = new Bundle();
////                    bundle.putInt("user_id");
////                    fragment.setArguments(bundle);
//
////
////
////            clickStrMap.put(userTagList, fragment);
//        }
//    }



//    class HttpResponse extends AsyncHttpResponseHandler {
//
////        public HttpResponse() { this.activity = activity;
////        }
//
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//            Log.d("[INFO]", "두번째 response 연결 onSuccess 진입" + statusCode);
//            String document = new String(responseBody);
//            try {
//                JSONObject json = new JSONObject(document);
//               int tagged_user_id = json.getInt("result");
//                Log.d("[INFO]", "SearchRandomClickFrag : tagged_user_id = " + tagged_user_id);
//
//
//                Map<String, Fragment> clickStrMap = new HashMap<>();
//                List<String> strList = testForUser(fullContent);
//                if(tagged_user_id !=0){
//
//                    for (String userTagList : strList) {
//
//                        ProfileFragment fragment = new ProfileFragment();
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("user_id", tagged_user_id);
//                        fragment.setArguments(bundle);
//
//
//                        clickStrMap.put(userTagList, fragment);
//                    }
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//            Log.d("[INFO]", "두번째 response 연결 실패" + statusCode);
//        }
//    }
//

//    public List<String> testForUser(String fullContent) {
//        String USER_TAG_DELIMITER = "@";
//        String BASIC_TAG_DELIMITER = "#";
//        String BLANK_TAG_DELIMITER = " ";
//        String WITH_DELIMITER = "((?=%1$s)|(?=%2$s)|(?=%3$s))";
//        String[] results = fullContent.split(String.format(WITH_DELIMITER, USER_TAG_DELIMITER, BASIC_TAG_DELIMITER, BLANK_TAG_DELIMITER));
//        List<String> users = new ArrayList<>();
//        List<String> basics = new ArrayList<>();
//        for (String result : results) {
//            System.out.println("parse Result : " + result);
//            if (result.startsWith(USER_TAG_DELIMITER)) {
//                users.add(result.substring(1));
//            } else if (result.startsWith(BASIC_TAG_DELIMITER)) {
//                basics.add(result);
//            }
//        }
//        return users;
//    }
//
//    public List<String> testForBasic(String fullContent) {
//        String USER_TAG_DELIMITER = "@";
//        String BASIC_TAG_DELIMITER = "#";
//        String BLANK_TAG_DELIMITER = " ";
//        String WITH_DELIMITER = "((?=%1$s)|(?=%2$s)|(?=%3$s))";
//        String[] results = fullContent.split(String.format(WITH_DELIMITER, USER_TAG_DELIMITER, BASIC_TAG_DELIMITER, BLANK_TAG_DELIMITER));
//        List<String> users = new ArrayList<>();
//        List<String> basics = new ArrayList<>();
//        for (String result : results) {
//            System.out.println("parse Result : " + result);
//            if (result.startsWith(USER_TAG_DELIMITER)) {
//                users.add(result);
//            } else if (result.startsWith(BASIC_TAG_DELIMITER)) {
//                basics.add(result);
//            }
//        }
//        return basics;
//    }

}
