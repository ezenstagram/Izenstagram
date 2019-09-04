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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.izenstargram.R;
import com.example.izenstargram.upload.CameraFragment;
import com.example.izenstargram.upload.UploadActivity;
import com.example.izenstargram.upload.WriteActivity;

public class ProfileCameraImageActivity extends AppCompatActivity implements View.OnClickListener {
    Bitmap adjustedBitmap;
    String photoPath;
    int sepa;
    TextView textViewOK, textViewCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_camera_image);

        Intent intent = getIntent();
        photoPath = intent.getStringExtra("strParamName");
        sepa = intent.getIntExtra("sepa", 0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        textViewOK = findViewById(R.id.textViewOK);
        textViewCancel = findViewById(R.id.textViewCancel);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        if(sepa == 0) {
            matrix.preRotate(90);
       }
        adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        ImageView img = (ImageView) findViewById(R.id.img);
        img.setImageBitmap(adjustedBitmap);

        textViewOK.setOnClickListener(this);
        textViewCancel.setOnClickListener(this);
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

            case R.id.home:
               finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewOK:
                Intent intent = new Intent();
                intent.putExtra("photoPath",photoPath);

                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.textViewCancel:
                finish();
            default:
                break;
        }
    }
}
