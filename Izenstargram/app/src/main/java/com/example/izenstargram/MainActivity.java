package com.example.izenstargram;

import android.Manifest;
import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.izenstargram.like.LikeFragment;
import com.example.izenstargram.profile.ProfileFragment;
import com.example.izenstargram.upload.UploadActivity;

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

    String[] navNames = {"list","search","like","profile"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

//        button = findViewById(R.id.button);
//        button.setOnClickListener(this);

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
                        replaceFragment(R.id.frame_layout, likeFragment, navNames[2]);
                        break;
                    case R.id.navigation_menu5: // 지윤
                        replaceFragment(R.id.frame_layout, profileFragment, navNames[3]);
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
    private void replaceFragment(int layoutId, Fragment fragment, String fra_name) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutId, fragment,fra_name);
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.addToBackStack(null);
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
