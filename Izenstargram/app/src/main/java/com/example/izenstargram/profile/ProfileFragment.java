package com.example.izenstargram.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    Animation translateLeftAnim;
    Animation translateRightAnim;
    Animation translateLeftmainAnim;
    Animation translateRightmainAnim;
    LinearLayout slidingPanel;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    Button button, buttonModi;
    TextView textViewLogin_id1, textViewLogin_id2, textViewPostCount, textViewFollower, textViewFollowing, textViewProfile;
    //ImageView imageView;
    LinearLayout linearLayout, linearLayouttouch;
    boolean isPageOpen=false;
    CircleImageView imageView;

    AsyncHttpClient client;
    ProfileInfoResponse profileInfoResponse;
    String profileInfoURL = "http://192.168.0.5:8080/project/profileInfo.do";

    UserInfoResponse userInfoResponse;
    String userInfoURL = "http://192.168.0.5:8080/project/user_profileInfo.do";

    ImageLoader imageLoader;
    DisplayImageOptions options;

    UserDTO userDTO;
    int user_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);// attachToRoot는 일단 false로..

        SharedPreferences pref = getActivity().getSharedPreferences("CONFIG", MODE_PRIVATE);
        String login_id = pref.getString("login_id", null);
        user_id = pref.getInt("user_id", 0);

        client = new AsyncHttpClient();
        profileInfoResponse = new ProfileInfoResponse();
        userInfoResponse = new UserInfoResponse();
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        client.post(profileInfoURL, params, profileInfoResponse);
        client.post(userInfoURL, params, userInfoResponse);

        // 메뉴
        translateLeftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translate_left);
        translateRightAnim =AnimationUtils.loadAnimation(getContext(),R.anim.translate_right);
        translateLeftmainAnim =AnimationUtils.loadAnimation(getContext(), R.anim.translatemain_left);
        translateRightmainAnim =AnimationUtils.loadAnimation(getContext(),R.anim.translatemain_right);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        linearLayouttouch = (LinearLayout) view.findViewById(R.id.linearLayouttouch);
        slidingPanel = (LinearLayout) view.findViewById(R.id.slidingPanel);
        button = (Button)view.findViewById(R.id.button);
        textViewPostCount = view.findViewById(R.id.textViewPostCount);
        textViewFollower = view.findViewById(R.id.textViewFollower);
        textViewFollowing = view.findViewById(R.id.textViewFollowing);
        buttonModi = (Button)view.findViewById(R.id.buttonModi);
       // imageView = view.findViewById(R.id.imageView);
        imageView = view.findViewById(R.id.CircleImageView);
        linearLayouttouch.setVisibility(View.GONE);

        textViewProfile = view.findViewById(R.id.textViewProfile);
        textViewLogin_id1 = view.findViewById(R.id.TextViewLogin_id1);
        textViewLogin_id2 = view.findViewById(R.id.TextViewLogin_id2);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        textViewLogin_id1.setText(login_id);
        textViewLogin_id2.setText(login_id);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
         //   imageView.setBackground(new ShapeDrawable(new OvalShape()));
        }
        if(Build.VERSION.SDK_INT >= 21) {
           // imageView.setClipToOutline(true);
        }

        buttonModi.setOnClickListener(this);
        button.setOnClickListener(this);

        final  TabPagerAdapter pagerAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
              //  pagerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

//        translateLeftAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if(isPageOpen){
//                    // slidingPanel.setVisibility(View.INVISIBLE);
//                    isPageOpen=false;
//                }else{
//                    isPageOpen=true;
//                }
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//        translateRightAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if(isPageOpen){
//                    //slidingPanel.setVisibility(View.INVISIBLE);
//                    isPageOpen=false;
//                }else{
//                    isPageOpen=true;
//                }
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });

        imageLoaderInit();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if(!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration =
                    ImageLoaderConfiguration.createDefault(getActivity());
            imageLoader.init(configuration);
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        options = builder.build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if(isPageOpen){
                  //  moveToRight();
                }else{
                //    moveToLeft();
                }
                break;
            case R.id.buttonModi:
                Intent intent = new Intent(getActivity(), ChangeProfileActivity.class);
                intent.putExtra("userDTO", userDTO);

                startActivityForResult(intent, 100);
                break;
            case R.id.linearLayouttouch:
                if(isPageOpen) {
               //     moveToRight();
                }
                break;
        }

    }
    //    public void moveToLeft() {
//        slidingPanel.startAnimation(translateLeftAnim);
//        linearLayout.startAnimation(translateLeftmainAnim);
//        slidingPanel.setVisibility(View.VISIBLE);
//        button.setClickable(false);
//        linearLayout.setEnabled(false);
//        disableEnableControls(false, linearLayout);
//        linearLayouttouch.setVisibility(View.VISIBLE);
//    }
//    public void moveToRight() {
//        slidingPanel.startAnimation(translateRightAnim);
//        linearLayout.startAnimation(translateRightmainAnim);
//        slidingPanel.setVisibility(View.GONE);
//        button.setClickable(true);
//        disableEnableControls(true, linearLayout);
//        linearLayouttouch.setVisibility(View.GONE);
//    }
//    // linearLayout 기능 죽이기
//    private void disableEnableControls(boolean enable, ViewGroup vg){
//        for (int i = 0; i < vg.getChildCount(); i++){
//            View child = vg.getChildAt(i);
//            child.setEnabled(enable);
//            if (child instanceof ViewGroup){
//                disableEnableControls(enable, (ViewGroup)child);
//            }
//        }
//    }

    // 게시물 수, 팔로우 수 가져오기
    public class ProfileInfoResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);

            try {
                JSONObject json = new JSONObject(content);
                int postCount = json.getInt("postCount");
                int follower = json.getInt("follower");
                int following = json.getInt("following");

                textViewPostCount.setText(postCount+"");
                textViewFollower.setText(follower+"");
                textViewFollowing.setText(following+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }
    public class UserInfoResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);

            try {
                JSONObject json = new JSONObject(content);
                JSONObject jsonObject = json.getJSONObject("userDTO");
                int user_id = jsonObject.getInt("user_id");
                String login_id = jsonObject.getString("login_id");
                String name = jsonObject.getString("name");
                String password = jsonObject.getString("password");
                String profile_photo = jsonObject.getString("profile_photo");
                String website = jsonObject.getString("website");
                String introduction = jsonObject.getString("introduction");
                String email = jsonObject.getString("email");
                String tel = jsonObject.getString("tel");
                String gender = jsonObject.getString("gender");

                String str = "";
                if(!name.equals("null")) {
                    str += name;
                }
                if(!introduction.equals("null")) {
                    str += "\n"+introduction;
                }
                if(!website.equals("null")) {
                    str += "\n"+website;
                }
//                if(profile_photo.equals("null"))  profile_photo = "0";
//                if(email.equals("null"))  email = "0";
//                if(tel.equals("null"))  tel = "0";
//                if(gender.equals("null"))  gender = "0";


                userDTO = new UserDTO(user_id, login_id, name, password, profile_photo, website, introduction, email, tel, gender);
                textViewProfile.setText(str);

                if(profile_photo != null) {
                   imageLoader.displayImage(profile_photo, imageView, options);
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 나에게 온 정보인지 확인
        switch (requestCode) {
            case 100:
                // 결과값이 "성공"일 경우만 처리
                if(resultCode == RESULT_OK) {
                    RequestParams params = new RequestParams();
                    params.put("user_id", user_id);
                    client.post(userInfoURL, params, userInfoResponse);

                }
                break;
        }
    }
}

