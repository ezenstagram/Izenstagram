package com.example.izenstargram.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.profile.UserDTO;
import com.example.izenstargram.upload.model.PostDTO;
import com.example.izenstargram.upload.model.PostImageDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class WriteActivity extends AppCompatActivity {
    //객체 선언
    EditText edit_content, edit_location;
    //통신용 객체 선언
    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.62:8080/project/post.do";

    Profilephoto profilephoto;
    String profile_URL = "http://192.168.0.62:8080/project/user_profileInfo.do";


    String photoPath = null;

    Bitmap adjustedBitmap;
    String photoPath_gallery, photoPath_camera;
    //객체 선언
    ImageView img_view, profile_picture;
    //ImageLoader imageLoader;
    DisplayImageOptions options_profile;
    Bitmap adjustedBitmap1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFF8F8FF));
        edit_content = findViewById(R.id.edit_content);
        edit_location = findViewById(R.id.edit_location);
        img_view = findViewById(R.id.img);
        profile_picture = findViewById(R.id.profile_picture);
        //통신용 객체 초기화
        client = new AsyncHttpClient();
        response = new HttpResponse(this);
        profilephoto = new Profilephoto(this);
        Intent intent = getIntent();
        //photoPath = intent.getStringExtra("photoPath");
        photoPath_gallery = intent.getStringExtra("strParamName_gallery");
        photoPath_camera = intent.getStringExtra("strParamName_camera");
        //이미지뷰에 사진넣기(내가 업로드할 사진)
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (photoPath_camera != null) { //카메라에서 찍어서 넘어온 사진은 90도 회전이 되므로 roatate 설정 해주어야함
            String fileName = photoPath_camera.substring(photoPath_camera.lastIndexOf('/') + 1, photoPath_camera.length());
            Toast.makeText(this, "photoPath : " + photoPath_camera, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "fileName : " + fileName, Toast.LENGTH_LONG).show();
            Bitmap bmp = BitmapFactory.decodeFile(photoPath_camera, options);
            Matrix matrix = new Matrix();
            matrix.preRotate(90);
            adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            adjustedBitmap1 = Bitmap.createScaledBitmap(adjustedBitmap, 40, 40, true);

        }
        if (photoPath_gallery != null) { //갤러리에서 넘어온 사진은 90도 회전할 필요가 없음
            String fileName = photoPath_gallery.substring(photoPath_gallery.lastIndexOf('/') + 1, photoPath_gallery.length());
            Toast.makeText(this, "photoPath : " + photoPath_gallery, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "fileName : " + fileName, Toast.LENGTH_LONG).show();
            Bitmap bmp = BitmapFactory.decodeFile(photoPath_gallery, options);
            Matrix matrix = new Matrix();
            adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            adjustedBitmap1 = Bitmap.createScaledBitmap(adjustedBitmap, 40, 40, true);

        }
        ImageView img = (ImageView) findViewById(R.id.img);
        img.setImageBitmap(adjustedBitmap1);

        //프로필 사진 화면에 user_id별 프로필 사진 넣기
        RequestParams params = new RequestParams();
        SharedPreferences pref = getSharedPreferences("CONFIG", MODE_PRIVATE);
        int user_id = pref.getInt("user_id", 0);
        params.put("user_id", user_id);
        client.post(profile_URL, params, profilephoto);

        imageLoaderInit();

    }

    private void imageLoaderInit() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        options_profile = builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_btn1:
                //테스트용 user_id 임의로 설정
                SharedPreferences pref = getSharedPreferences("CONFIG", MODE_PRIVATE);
                int user_id = pref.getInt("user_id", 0);
                //입력값 얻어오기
                String content = edit_content.getText().toString().trim();
                String location = edit_location.getText().toString().trim();
                //입력값, 위치 모두 업로드 안해도 되므로 입력값 검사를 따로할필요 없다.
                //서버로 데이터 전송 및 요청
                RequestParams params = new RequestParams();
                params.put("user_id", user_id);
                params.put("content", content);
                params.put("location", location);
                params.setForceMultipartEntityContentType(true);
                try {
                    if (photoPath_camera != null) {
                        params.put("img", new File(photoPath_camera));
                    } else if (photoPath_gallery != null) {
                        params.put("img", new File(photoPath_gallery));
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                client.post(URL, params, response);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*프로필 사진 받아오기*/
    class Profilephoto extends AsyncHttpResponseHandler {
        Activity activity;

        public Profilephoto(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Toast.makeText(activity, "프로필 사진 통신성공 진입확인용", Toast.LENGTH_SHORT).show();
            String content = new String(responseBody);
            try {
                JSONObject json = new JSONObject(content);
                String profile_photo = json.getString("profile_photo");

                if (!profile_photo.equals("null")) {
                    String photoPath_profile = "http://192.168.0.13:8080/image/storage/" + profile_photo;
                    ImageLoader.getInstance().displayImage(photoPath_profile, profile_picture, options_profile);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "프로필사진이름 못받아온것" + statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    class HttpResponse extends AsyncHttpResponseHandler {

        //기본 추가 시키기
        Activity activity;

        public HttpResponse(Activity activity) {
            this.activity = activity;
        }

        // onStart() : 통신 시작시
//        @Override
//        public void onStart() {
//            dialog = new ProgressDialog(activity);
//            dialog.setMessage("잠시만 기다려주세요");
//            dialog.setCancelable(false);
//            dialog.show();
//        }

        // onFinish() : 통신 종료시
//        @Override
//        public void onFinish() {
//           dialog.dismiss();
//            dialog = null;
//        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Toast.makeText(activity, "통신성공 진입확인용", Toast.LENGTH_SHORT).show();
            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("result");
                /** 태그관련 새로추가  **/
                String post_id = json.getString("post_id");
                String content = json.getString("content");
                if (rt.equals("1")) {
                    Toast.makeText(activity, "저장성공", Toast.LENGTH_SHORT).show();
                    registerTags(post_id, content);
                    /** 태그관련 일단 요기까지 **/
                    finish(); //앱을 종료시키는 것
                } else {
                    Toast.makeText(activity, "저장실패" + statusCode, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패" + statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 태그관련 메소드
     */
    private void registerTags(String post_id, String content) {
        String TAG_URL = "http://192.168.0.62:8080/project/putTagIntoPost.do";
        String USER_TAG_URL = "http://192.168.0.62:8080/project/putUserTagIntoPost.do";

        RequestParams params = new RequestParams();
        params.put("post_id", post_id);
        params.put("fullContent", content);

        client.post(TAG_URL, params, response);
        client.post(USER_TAG_URL, params, response);
    }

//    @Override
//    protected void onDestroy() {
//        dialog.dismiss();
//        super.onDestroy();
//    }
}