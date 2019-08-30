package com.example.izenstargram.upload;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.helper.FileUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment  {
    TextView textView;
    RecyclerView recyclerView;
    PictureAdapter adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    int pictureCount = 0;

    ArrayList<PictureInfo> pictures;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false); // attachToRoot는 일단 false로..
        textView = view.findViewById(R.id.textView);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(),3);//this 대신 바꿈
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnPictureItemClickListener() {
            @Override
            public void onItemClick(PictureAdapter.ViewHolder holder, View view, int position) {
                PictureInfo item = adapter.getItem(position);
                String str=item.getPath();
                Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                intent.putExtra("strParamName_gallery",str);
                startActivity(intent);
                Toast.makeText(getContext().getApplicationContext(), "아이템 선택됨 : " + str, Toast.LENGTH_LONG).show();//getContext().를 붙임
            }
        });


//        AutoPermissions.Companion.loadAllPermissions(this, 101);

        ArrayList<PictureInfo> result = queryAllPictures();
        adapter.setItems(result);
        adapter.notifyDataSetChanged();
        return view;
    }
    private ArrayList<PictureInfo> queryAllPictures() {
        ArrayList<PictureInfo> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED };

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
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
}
