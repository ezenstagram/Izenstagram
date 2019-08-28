package com.example.izenstargram.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.example.izenstargram.login.LoginActivity;
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
import static android.view.View.GONE;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    Animation translateLeftAnim;
    Animation translateRightAnim;
    Animation translateLeftmainAnim;
    Animation translateRightmainAnim;
    LinearLayout slidingPanel;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    Button button, buttonModi;
    TextView textViewLogin_id1, textViewLogin_id2, textViewPostCount, textViewFollower, textViewFollowing, textViewProfile, textViewLogout;
    //ImageView imageView;
    LinearLayout linearLayout, linearLayouttouch, linearLayoutReal;
    boolean isPageOpen=false;
    CircleImageView imageView;

    AsyncHttpClient client;
    ProfileInfoResponse profileInfoResponse;
    String profileInfoURL = "http://192.168.0.32:8080/project/profileInfo.do";

    UserInfoResponse userInfoResponse;
    String userInfoURL = "http://192.168.0.32:8080/project/user_profileInfo.do";

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
        linearLayoutReal = (LinearLayout) view.findViewById(R.id.linearLayoutReal);
        slidingPanel = (LinearLayout) view.findViewById(R.id.slidingPanel);
        button = (Button)view.findViewById(R.id.button);
        textViewLogout = view.findViewById(R.id.textViewLogout);
        textViewPostCount = view.findViewById(R.id.textViewPostCount);
        textViewFollower = view.findViewById(R.id.textViewFollower);
        textViewFollowing = view.findViewById(R.id.textViewFollowing);
        buttonModi = (Button)view.findViewById(R.id.buttonModi);
        // imageView = view.findViewById(R.id.imageView);
        imageView = view.findViewById(R.id.CircleImageView);
        linearLayouttouch.setVisibility(GONE);
        linearLayoutReal.setVisibility(GONE);
        slidingPanel.setVisibility(GONE);

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
        textViewLogout.setOnClickListener(this);
        buttonModi.setOnClickListener(this);
        button.setOnClickListener(this);
        linearLayouttouch.setOnClickListener(this);
        final  TabPagerAdapter pagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // viewPager.onSaveInstanceState();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                pagerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        translateLeftAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(isPageOpen){
                    slidingPanel.setVisibility(View.INVISIBLE);
                    isPageOpen=false;
                }else{
                    isPageOpen=true;
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        translateRightAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(isPageOpen){
                    slidingPanel.setVisibility(View.INVISIBLE);

                    isPageOpen=false;
                }else{
                    isPageOpen=true;
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        imageLoaderInit();
        return view;
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
                    moveToRight();
                }else{
                    moveToLeft();
                }
                break;
            case R.id.buttonModi:
                Intent intent = new Intent(getActivity(), ChangeProfileActivity.class);
                intent.putExtra("userDTO", userDTO);

                startActivityForResult(intent, 100);
                break;
            case R.id.linearLayouttouch:
                if(isPageOpen) {
                    moveToRight();
                }
                break;
            case R.id.textViewLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("정말 로그아웃 하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences pref = getActivity().getSharedPreferences("CONFIG", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.clear();
                        editor.commit();

                        Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setTitle("안내");

                Dialog dialog = builder.create();
                dialog.show();
                break;
        }
    }
    public void moveToLeft() {
        slidingPanel.startAnimation(translateLeftAnim);
        linearLayout.startAnimation(translateLeftmainAnim);
        slidingPanel.setVisibility(View.VISIBLE);
        button.setClickable(false);
        linearLayout.setEnabled(false);
        disableEnableControls(false, linearLayout);
        linearLayouttouch.setVisibility(View.VISIBLE);
        linearLayoutReal.setVisibility(View.VISIBLE);
    }
    public void moveToRight() {
        slidingPanel.startAnimation(translateRightAnim);
        linearLayout.startAnimation(translateRightmainAnim);
        slidingPanel.setVisibility(GONE);
        button.setClickable(true);
        disableEnableControls(true, linearLayout);
        linearLayouttouch.setVisibility(GONE);
        linearLayoutReal.setVisibility(View.GONE);
    }
    // linearLayout 기능 죽이기
    private void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }
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
            Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
    // 프로필 정보 가져오기
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
                if(!profile_photo.equals("null")) {
                    String photo = "http://192.168.0.13:8080/image/storage/" +profile_photo;
                    imageLoader.displayImage(photo, imageView, options);
                } else {
                    imageView.setImageResource(R.drawable.ic_empty);
                }

                userDTO = new UserDTO(user_id, login_id, name, password, profile_photo, website, introduction, email, tel, gender);
                textViewProfile.setText(str);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
    // 프로필 수정이 성공하면 프로필 새로고침
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