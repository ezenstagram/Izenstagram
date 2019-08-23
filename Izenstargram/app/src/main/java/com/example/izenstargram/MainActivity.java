package com.example.izenstargram;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.izenstargram.like.LikeFragment;
import com.example.izenstargram.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 메뉴에 들어갈 Fragment들
    private LikeFragment likeFragment = new LikeFragment();
    private ListFragment listFragment = new ListFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private UploadFragment uploadFragment = new UploadFragment();
    // menu
    BottomNavigationView bottomNavigationView;
    // user id
    int user_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_menu1: // 민경
                        replaceFragment(R.id.frame_layout, listFragment);
                        break;
                    case R.id.navigation_menu2: // 은경
                        replaceFragment(R.id.frame_layout, searchFragment);
                        break;
                    case R.id.navigation_menu3: // 지현
                        replaceFragment(R.id.frame_layout, uploadFragment);
                        break;
                    case R.id.navigation_menu4: // 수정
                        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
                        bundle.putString("data", "hong"); // key , value
                        likeFragment.setArguments(bundle);
                        replaceFragment(R.id.frame_layout, likeFragment);
                        break;
                    case R.id.navigation_menu5: // 지윤
                        replaceFragment(R.id.frame_layout, profileFragment);
                        break;
                }
                return true;
            }
        });
        SharedPreferences pref = getSharedPreferences("CONFIG", MODE_PRIVATE);
        user_id = pref.getInt("user_id", 0);
        if (user_id == 0) {
            // 에러 화면....
        }

        permissionCheck();
    }

    // 퍼미션 체크
    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        } else {
        }
    }

    // 화면 전환
    private void replaceFragment(int layoutId, Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutId, fragment).commitAllowingStateLoss();
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
