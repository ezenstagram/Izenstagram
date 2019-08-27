package com.example.izenstargram.upload.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.izenstargram.R;
import com.example.izenstargram.upload.helper.FileUtils;
import com.example.izenstargram.upload.helper.PhotoHelper;

public class Gallery extends AppCompatActivity implements View.OnClickListener {
    //객체 선언
    ImageView imageView;
    Button button;
    Bitmap bmp; //메모리로 로드한 이미지가 저장될 객체
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //객체 초기화
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        //이벤트 설정
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                // 갤러리를 호출하기 위한 암묵적 인텐트
                Intent intent = null;
                if(Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){ //킷캣 ==> 19
                    intent = new Intent(intent.ACTION_GET_CONTENT);
                }else{
                    intent = new Intent(intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                //이미지 파일만 필터링 ==> MiME 형태 (KITKAT 이상 버전용) "*/*" 이면 모든 종류의 파일을 불러온다.
                intent.setType("image/*");
                //구글 클라우드에 싱크된 파일 제외
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                }
                //갤러리 실행 요청
                startActivityForResult(intent, 100);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==100){
            if(resultCode == RESULT_OK){
                // 사용자가 선택한 결과값 얻기
                Uri photoUri = data.getData();
                //com.example.a4_galleryexam D/[INFO]: photoUri=content://com.android.providers.media.documents/document/image%3A32648
                Log.d("[INFO]","photoUri=" + photoUri);
                //imageView.setImageURI(photoUri); 직접써도 상관없다.
                //파일의 실제경로(절대경로) 얻어오기
                String filePath = FileUtils.getPath(this, photoUri);
                //filePath=/storage/emulated/0/DCIM/p2019-07-16 07-19-07.jpg
                Log.d("[INFO]","filePath="+filePath);

                imageView.setImageBitmap(null);
                if(bmp!=null){
                    bmp.recycle();
                    bmp=null;
                }
                bmp = PhotoHelper.getInstance().getThumb(this, filePath);
                imageView.setImageBitmap(bmp);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bmp!=null){
            bmp.recycle();
            bmp=null;
        }
    }

}
