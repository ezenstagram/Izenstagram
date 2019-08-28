package com.example.izenstargram.profile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.upload.ImageViewActivity;
import com.example.izenstargram.upload.OnPictureItemClickListener;
import com.example.izenstargram.upload.PictureAdapter;
import com.example.izenstargram.upload.PictureInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileGalleryActivity extends AppCompatActivity {
    TextView textView;
    RecyclerView recyclerView;
    ProfileGelleryAdapter adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    int pictureCount = 0;

    ArrayList<PictureInfo> pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gallery);


        recyclerView =findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,3);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProfileGelleryAdapter();
        recyclerView.setAdapter(adapter);
        textView = findViewById(R.id.textView);


        adapter.setOnItemClickListener(new OnProfileGalleryClickListener() {

            @Override
            public void onItemClick(ProfileGelleryAdapter.ViewHolder holder, View view, int position) {
                PictureInfo item = adapter.getItem(position);
                String str=item.getPath();

                Intent intent = new Intent(getApplicationContext(), ProfileCameraImageActivity.class);
                intent.putExtra("strParamName",str);
                startActivityForResult(intent, 100);
               // Toast.makeText(getApplicationContext(), "아이템 선택됨 : " + str, Toast.LENGTH_LONG).show();//getContext().를 붙임
            }
        });

        ArrayList < PictureInfo > result = queryAllPictures();
        adapter.setItems(result);
        adapter.notifyDataSetChanged();
    }
    private ArrayList<PictureInfo> queryAllPictures() {
        ArrayList<PictureInfo> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnDataIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnNameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        int columnDateIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);

        pictureCount = 0;
        while (cursor.moveToNext()) {
            String path = cursor.getString(columnDataIndex);
            String displayName = cursor.getString(columnNameIndex);
            String outDate = cursor.getString(columnDateIndex);
            String addedDate = dateFormat.format(new Date(new Long(outDate).longValue() * 1000L));

            if (!TextUtils.isEmpty(path)) {
                PictureInfo info = new PictureInfo(path, displayName, addedDate);
                result.add(info);
            }

            pictureCount++;
        }

        textView.setText(pictureCount + " 개");
        Log.d("MainActivity", "Picture count : " + pictureCount);

        for (PictureInfo info : result) {
            Log.d("MainActivity", info.toString());
        }

        return result;
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