package com.example.izenstargram.upload;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.upload.adapter.TabPagerAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ImageViewActivity extends AppCompatActivity implements View.OnClickListener {
    CameraFragment cameraFragment; //액티비티에서 프래그먼트를 호출하기위해
    GalleryFragment galleryFragment;
    Bitmap adjustedBitmap,adjustedBitmap1;
    String photoPath_gallery, photoPath_camera;
    TabPagerAdapter adapter;


    Button button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        //뒤로가기 버튼 액션바에 추가하기
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        photoPath_gallery = intent.getStringExtra("strParamName_gallery");
        photoPath_camera = intent.getStringExtra("strParamName_camera");
        cameraFragment = new CameraFragment();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        if (photoPath_camera!=null){ //카메라에서 찍어서 넘어온 사진은 90도 회전이 되므로 roatate 설정 해주어야함
            Bitmap bmp = BitmapFactory.decodeFile(photoPath_camera, options);
            Matrix matrix = new Matrix();
            matrix.preRotate(90);
            adjustedBitmap = Bitmap.createBitmap(bmp,0,0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            adjustedBitmap1 = Bitmap.createScaledBitmap(adjustedBitmap, 160, 160, true);

        }
        if(photoPath_gallery!=null){ //갤러리에서 넘어온 사진은 90도 회전할 필요가 없음
            Bitmap bmp = BitmapFactory.decodeFile(photoPath_gallery, options);
            Matrix matrix = new Matrix();
            adjustedBitmap = Bitmap.createBitmap(bmp,0,0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            adjustedBitmap1 = Bitmap.createScaledBitmap(adjustedBitmap, 160, 160, true);

        }
        ImageView img = (ImageView) findViewById(R.id.img);
        img.setImageBitmap(adjustedBitmap1);

        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void replaceFragment1(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.cameraFragment, fragment);
        fragmentTransaction.commit();
    }
    public void replaceFragment2(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.galleryFragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_btn1:
                Intent intent = new Intent(this, WriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                if (photoPath_camera!=null){
                    intent.putExtra("strParamName_camera",photoPath_camera);
                }
                if(photoPath_gallery!=null){
                    intent.putExtra("strParamName_gallery",photoPath_gallery);
                }
                startActivity(intent);
                finish();
                return true;
            case R.id.home:
                if(adapter.getItem(adapter.getItemPosition(1)).equals(1)){
                    replaceFragment1(cameraFragment);
                }
                if(adapter.getItem(adapter.getItemPosition(0)).equals(0)){
                   replaceFragment2(galleryFragment);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                if(adapter.getItem(1).equals(1)){
                    replaceFragment1(cameraFragment);
                }
                if(adapter.getItem(adapter.getItemPosition(0)).equals(0)){
                    replaceFragment2(galleryFragment);
                }

                break;
        }
    }
}
