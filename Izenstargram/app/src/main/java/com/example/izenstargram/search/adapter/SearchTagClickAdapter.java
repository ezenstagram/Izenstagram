package com.example.izenstargram.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.izenstargram.R;
import com.example.izenstargram.upload.model.PostImageDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SearchTagClickAdapter extends ArrayAdapter<PostImageDTO> {

    Activity activity;
    int resource;
    DisplayImageOptions options;

    public SearchTagClickAdapter(@NonNull Context context, int resource, @NonNull List<PostImageDTO> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
        imageLoaderInit();
    }

    private void imageLoaderInit() {
        // 다운로드시 옵션 설정
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        //builder.showImageOnFail(R.drawable.ic_error);
        builder.showImageOnFail(R.drawable.icon_main);
        options = builder.build();
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);

        }
        PostImageDTO item = getItem(position);

        if (item != null) {
            //  for (int i = 0; i < 1; i++) {
            ImageView imageView = convertView.findViewById(R.id.imageViewSearchGrid2);
            convertView.setLayoutParams(new GridView.LayoutParams(360, 360));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.getPost_id();
            ImageLoader.getInstance().displayImage(item.getImage_url(), imageView, options);
            //  }
        }
        return convertView;
    }
}
