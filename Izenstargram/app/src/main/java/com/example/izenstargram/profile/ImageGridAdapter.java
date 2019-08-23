package com.example.izenstargram.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.izenstargram.R;

public class ImageGridAdapter extends BaseAdapter {

    Context context;
    int layout;
    int img[];
    LayoutInflater inf;

    public ImageGridAdapter(Context context, int layout, int[] img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return img[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);

            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setLayoutParams( new GridView.LayoutParams( 330, 330 ));
            iv.setImageResource(img[position]);
        }
        return convertView;
    }
}
