package com.example.izenstargram.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.izenstargram.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class ImageGridAdapter extends BaseAdapter {

    LayoutInflater inf;
    Context context;
    int layout;
    List<String> list;
    ImageLoader imageLoader;
    DisplayImageOptions options;

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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setLayoutParams( new GridView.LayoutParams( 330, 330 ));

            imageLoader.displayImage(list.get(position), holder.img, options);
            convertView.setTag(holder);

            //iv.setImageResource(img[position]);
        }
        return convertView;
    }


}
