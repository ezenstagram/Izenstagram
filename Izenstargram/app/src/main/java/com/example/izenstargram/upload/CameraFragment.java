package com.example.izenstargram.upload;


import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.helper.PhotoHelper;
import com.example.izenstargram.upload.adapter.TabPagerAdapter;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener {
    static Camera camera;
    ImageButton button; //촬영용 버튼
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    TabPagerAdapter tabPagerAdapter;
    String str;
    int cameraId;
    boolean capture;

    @SuppressWarnings("deprecation")
    Camera.PictureCallback jpegCallback;


    @Nullable
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_camera, container, false); // attachToRoot는 일단 false로..
        View view = inflater.inflate(R.layout.fragment_camera, null); // attachToRoot는 일단 false로..
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
        camera = Camera.open(cameraId);
        camera.setDisplayOrientation(90);
        surfaceView = view.findViewById(R.id.surfaceView);
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

                Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                intent.putExtra("strParamName_camera", str);
                Log.d("[INFO]","photoPath의 역할을 하는 str:"+str);
                startActivity(intent);
            }
        };

        return view;
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
        }
    }
}





