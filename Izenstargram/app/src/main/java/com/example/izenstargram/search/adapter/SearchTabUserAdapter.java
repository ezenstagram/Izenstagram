package com.example.izenstargram.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.izenstargram.R;
import com.example.izenstargram.profile.UserDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchTabUserAdapter extends ArrayAdapter<UserDTO> {

    Activity activity;
    int resource;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public SearchTabUserAdapter(@NonNull Context context, int resource, @NonNull List<UserDTO> objects) {//////
        //public SearchTabUserAdapter(@NonNull Context context, int resource, @NonNull List objects) {//////
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
        imageLoaderInit();
    }
    private void imageLoaderInit() {
        // 이미지로더 초기화
        imageLoader = ImageLoader.getInstance();
        if(!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
            imageLoader.init(configuration);
        }
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
        UserDTO item = getItem(position);
        if (item != null) {
            TextView textViewUserId = convertView.findViewById(R.id.textViewUserId);
            TextView textViewUserName = convertView.findViewById(R.id.textViewUserName);
            CircleImageView circleImageView = convertView.findViewById(R.id.imageViewSearchUserCircle);
            imageLoader.displayImage(item.getProfile_photo(), circleImageView, options);
            textViewUserId.setText("@" + item.getLogin_id());
            textViewUserName.setText(item.getName());

        }
        return convertView;
    }






}
