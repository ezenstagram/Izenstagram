package com.example.izenstargram.profile;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.helper.PhotoHelper;
import com.example.izenstargram.upload.ImageViewActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {
    static Camera camera;
    ImageButton button;
    TextView textViewCancel;

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    String str;
    int cameraId;
    boolean capture;

    @SuppressWarnings("deprecation")
    Camera.PictureCallback jpegCallback;

    @Nullable
    @SuppressWarnings("deprecation")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_camera);
        textViewCancel = findViewById(R.id.textViewCancel);
        button = findViewById(R.id.button);
        camera = Camera.open(cameraId);
        camera.setDisplayOrientation(90);
        button.setOnClickListener(this);
        textViewCancel.setOnClickListener(this);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    str = PhotoHelper.getInstance().getNewPhotoPath();
                    outStream = new FileOutputStream(str);

                    outStream.write(data);
                    outStream.close();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                }
                Intent intent = new Intent(getApplicationContext(), ProfileCameraImageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("strParamName", str);
                Log.d("[INFO]","photoPath의 역할을 하는 str:"+str);
                startActivityForResult(intent, 100);
            }
        };

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            if(camera==null){
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }
        } catch (IOException e) {

        }
    }
    private void releaseCameraAndPreview() {

        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera(camera);
    }

    private void refreshCamera(Camera camera) {
        if(surfaceHolder.getSurface()==null){
            return;
        }
        try{
            camera.stopPreview();
        }catch (Exception e){

        }
        setCamera(camera);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        camera = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean capture(Camera.PictureCallback handler){
        if(camera!=null){
            camera.takePicture(null,null,handler);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                camera.takePicture(null, null, jpegCallback);
                break;
            case R.id.textViewCancel:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100:
                // 결과값이 "성공"일 경우만 처리
                if(resultCode == RESULT_OK) {
                    // 되돌려받은 수정 내용 추출
                    String photoPath = data.getStringExtra("photoPath");

                    Intent intent = new Intent();
                    intent.putExtra("photoPath",photoPath);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }
}