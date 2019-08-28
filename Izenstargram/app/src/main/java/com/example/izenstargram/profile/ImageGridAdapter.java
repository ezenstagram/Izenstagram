package com.example.izenstargram.profile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.izenstargram.R;
import com.example.izenstargram.helper.PhotoHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ImageGridAdapter extends BaseAdapter {

    LayoutInflater inf;
    Context context;
    int layout;
    List<String> list;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    // 이미지
    Bitmap bmp = null;
    String fileName;
    String img_URL;


    public ImageGridAdapter(Context context, int layout, List<String> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        //notifyDataSetChanged();
        imageLoaderInit();
    }

    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if(!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration =
                    ImageLoaderConfiguration.createDefault(context);
            imageLoader.init(configuration);
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.considerExifParams(true);
        options = builder.build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private  class ViewHolder {
        ImageView img;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //ViewHolder holder;
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
//            holder = new ViewHolder();
//            holder.img = (ImageView) convertView.findViewById(R.id.imageView1);
            ImageView img = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setLayoutParams( new GridView.LayoutParams( 360, 360 ));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //img.setPadding(5,5,5,5);

            String photo = "http://192.168.0.13:8080/image/storage/" + list.get(position);
        imageLoader.displayImage( photo, img, options);
        }
        return convertView;
    }


}
