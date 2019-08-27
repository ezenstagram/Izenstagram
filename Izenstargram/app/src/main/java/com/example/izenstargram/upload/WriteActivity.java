package com.example.izenstargram.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.upload.model.PostDTO;
import com.example.izenstargram.upload.model.PostImageDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
    String photoPath=null;
    Intent intent = getIntent();

    PostDTO postDTO = new PostDTO();
    PostImageDTO postImageDTO = new PostImageDTO();
    Bitmap adjustedBitmap ;
    String fileName;
    //객체 선언
    ImageView img_view;
    String img_URL;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        edit_content = findViewById(R.id.edit_content);
        edit_location = findViewById(R.id.edit_location);
        img_view= findViewById(R.id.img);

        //통신용 객체 초기화
        client = new AsyncHttpClient();
        response = new HttpResponse(this);
        //이미지뷰에 사진넣기
        Intent intent = getIntent();
        photoPath = intent.getStringExtra("photoPath");
        String fileName = photoPath.substring( photoPath.lastIndexOf('/')+1, photoPath.length() );
        Toast.makeText(this, "fileName : "+fileName, Toast.LENGTH_LONG).show();
        File file = new File(photoPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        matrix.preRotate(90);
        adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);


        ImageView img = (ImageView) findViewById(R.id.img);
        img.setImageBitmap(adjustedBitmap);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_btn1:
                //테스트용 user_id 임의로 설정
                int user_id=130;
                //입력값 얻어오기
                String content = edit_content.getText().toString().trim();
                String location = edit_location.getText().toString().trim();
                //입력값, 위치 모두 업로드 안해도 되므로 입력값 검사를 따로할필요 없다.
                //서버로 데이터 전송 및 요청
                RequestParams params = new RequestParams();
                params.put("user_id",user_id);
                params.put("content",content);
                params.put("location",location);
                params.setForceMultipartEntityContentType(true);

                try {
                    params.put("img",new File(photoPath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                client.post(URL, params,response);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    class HttpResponse extends AsyncHttpResponseHandler {

        //기본 추가 시키기
        Activity activity;


        public HttpResponse(Activity activity) {
            this.activity = activity;
        }

        // onStart() : 통신 시작시
        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세요");
            dialog.setCancelable(false);
            dialog.show();
        }

        // onFinish() : 통신 종료시
        @Override
        public void onFinish() {
           dialog.dismiss();
            dialog = null;
        }



        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Toast.makeText(activity,"통신성공 진입확인용",Toast.LENGTH_SHORT).show();
            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("result");
                if(rt.equals("1")){
                    Toast.makeText(activity,"저장성공",Toast.LENGTH_SHORT).show();
                    finish(); //앱을 종료시키는 것
                }else{
                    Toast.makeText(activity,"저장실패"+statusCode,Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패"+statusCode, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();
    }
}
