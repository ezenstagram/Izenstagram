package com.example.izenstargram.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.izenstargram.R;
import com.example.izenstargram.upload.model.PostImageDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.List;

public class SearchTabRandomAdapter extends ArrayAdapter<PostImageDTO> {


    Activity activity;
    int resource;
    ImageLoader imageLoader;
    DisplayImageOptions options;


    public SearchTabRandomAdapter(Context context, int resource, List<PostImageDTO> objects) {
//        this.activity = activity;
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
        imageLoaderInit();

    }

    private void imageLoaderInit() {
        // 이미지로더 초기화
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
            imageLoader.init(configuration);
        }
        // 다운로드시 옵션 설정
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);
        options = builder.build();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);

        }
        PostImageDTO item = getItem(position);

        if (item != null) {
            for (int i = 0; i < 1; i++) {
                ImageView imageView = convertView.findViewById(R.id.imageViewSearchGrid);
                convertView.setLayoutParams(new GridView.LayoutParams(360, 360));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Log.d("[INFO]", "새로 만든 SearchTabRandomAdapter : item.getImage_url() = " + item.getImage_url());
                imageLoader.displayImage(item.getImage_url(), imageView, options);

            }
        }
        return convertView;
    }
}