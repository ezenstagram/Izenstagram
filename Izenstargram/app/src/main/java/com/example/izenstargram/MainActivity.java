package com.example.izenstargram;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.izenstargram.feed.ListFragment;
import com.example.izenstargram.follow.FollowListFragment;
import com.example.izenstargram.like.LikeFragment;
import com.example.izenstargram.like.response.CheckResponse;
import com.example.izenstargram.profile.ProfileFragment;
import com.example.izenstargram.search.SearchFragment;
import com.example.izenstargram.upload.UploadActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //    Button button;
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 메뉴에 들어갈 Fragment들
    private LikeFragment likeFragment = new LikeFragment();
    private ListFragment listFragment = new ListFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private SearchFragment searchFragment = new SearchFragment();
    // menu
    BottomNavigationView bottomNavigationView;
    // user id
    int user_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageLoader imageLoader;
    private View notificationBadge;
    FrameLayout frameLayout;
    String CHECK_URL = "http://192.168.0.62:8080/project/selectNewCnt.do";

    String[] navNames = {"list","search","upload","like","profile"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        frameLayout = findViewById(R.id.frame_layout);
        //액션바 없애주는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //setContentView(R.layout.activity_upload);
        SharedPreferences pref = getSharedPreferences("CONFIG", MODE_PRIVATE);
        user_id = pref.getInt("user_id", 0);
        if (user_id == 0) {
            // 에러 화면....
        }
        addFragment();

        Bundle list_bundle = new Bundle(1);
        list_bundle.putInt("list_user_id", user_id);
        listFragment.setArguments(list_bundle);
        replaceFragment(R.id.frame_layout, listFragment, navNames[0]);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_menu1: // 민경
                       replaceFragment(R.id.frame_layout, listFragment, navNames[0]);
                        break;

                    case R.id.navigation_menu2: // 은경
                        replaceFragment(R.id.frame_layout, searchFragment, navNames[1]);
                        break;
                    case R.id.navigation_menu3: // 지현
                        Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,R.xml.slide_up);
                        break;
                    case R.id.navigation_menu4: // 수정
                        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
                        bundle.putInt("user_id", user_id); // key , value
                        likeFragment.setArguments(bundle);
                        replaceFragment(R.id.frame_layout, likeFragment, navNames[3]);

                        break;
                    case R.id.navigation_menu5: // 지윤
                        Bundle bundle5 = new Bundle(1);
                        bundle5.putInt("user_id", user_id);
                        profileFragment.setArguments(bundle5);
                        replaceFragment(R.id.frame_layout, profileFragment, navNames[4]);
                        break;
                }
                return true;
            }
        });

        ArrayList<String> permissionCheck = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionCheck.add(Manifest.permission.CAMERA);}
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionCheck.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);}
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionCheck.add(Manifest.permission.RECORD_AUDIO);}
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionCheck.add(Manifest.permission.READ_EXTERNAL_STORAGE);}

        if(permissionCheck.size() > 0) {
            String[] reqPermissionArray = new String[permissionCheck.size()];
            reqPermissionArray = permissionCheck.toArray(reqPermissionArray);
            ActivityCompat.requestPermissions(this, reqPermissionArray,100);
        }
        imageLoaderInit();
        // 좋아요 체크
        checkLike();
    }
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void imageLoaderInit() {
        // 이미지로더 초기화
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
            imageLoader.init(configuration);
        }
    }

    private void checkLike(){
        Runnable myRunnable = new Runnable() {
            AsyncHttpClient client = new AsyncHttpClient();
            CheckResponse response = new CheckResponse(MainActivity.this);
            RequestParams params = new RequestParams();

            @Override
            public void run() {
                while (true){
                    System.out.println("checkLike()");
                    params.put("target_user_id", user_id);
                    client.get(CHECK_URL, params, response);
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        HandlerThread thread = new HandlerThread("like_check");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(myRunnable);
    }

    public void addBadgeView(String text) {
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.like_notification, null , false);
        TextView textView = notificationBadge.findViewById(R.id.textView);
        textView.setText(text);
        notificationBadge.setVisibility(View.VISIBLE);
        frameLayout.addView(notificationBadge);
    }

    public void delBadgeView() {
        if(notificationBadge != null){
            ((ViewManager)notificationBadge.getParent()).removeView(notificationBadge);
        }
    }


    // 화면 전환
    public void replaceFragment(int layoutId, Fragment fragment, String fra_name) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutId, fragment,fra_name);
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_layout, listFragment, navNames[0]);
        transaction.add(R.id.frame_layout, searchFragment, navNames[1]);
        transaction.add(R.id.frame_layout, likeFragment, navNames[3]);
        Bundle bundle = new Bundle(1);
        bundle.putInt("user_id", user_id);
        profileFragment.setArguments(bundle);
        transaction.add(R.id.frame_layout, profileFragment, navNames[4]);
        Bundle bundle2 = new Bundle(1);
        bundle2.putInt("sepa", 0);
        FollowListFragment followListFragment = FollowListFragment.newInstance();

        followListFragment.setArguments(bundle2);
        transaction.add(R.id.frame_layout, followListFragment, navNames[4]);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            for(int i = 0; i <navNames.length; i++){
                Fragment currentFrag1 =  fragmentManager.findFragmentByTag(navNames[i]);
                if (currentFrag1 != null && currentFrag1.isVisible()) {
                    bottomNavigationView.getMenu().getItem(i).setChecked(true);
                    return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        pref = getSharedPreferences("CONFIG", MODE_PRIVATE);
        editor = pref.edit();
        //editor.remove("user_id");   // or
        editor.clear();
        editor.commit();
    }
}
