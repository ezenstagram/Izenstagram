package com.example.izenstargram.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.izenstargram.R;
import com.example.izenstargram.upload.CameraFragment;
import com.example.izenstargram.upload.UploadActivity;
import com.example.izenstargram.upload.WriteActivity;

public class ProfileCameraImageActivity extends AppCompatActivity {
    Bitmap adjustedBitmap;
    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_camera_image);

        //뒤로가기 버튼 액션바에 추가하기
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        photoPath = intent.getStringExtra("strParamName");

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
                Intent intent = new Intent();
                intent.putExtra("photoPath",photoPath);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case R.id.home:
               finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
