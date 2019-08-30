package com.example.izenstargram.follow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Picture;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class FollowAdapter1 extends ArrayAdapter<FollowDTO> {

    Activity activity;
    int resource;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    HttpResponse response;
    AsyncHttpClient client;
    String URL = "http://192.168.0.32:8080/project/follow.do";
    int user_id;
    int sign = 0;
    List<FollowDTO> objects;
    boolean check = false;

    public FollowAdapter1(Context context, int resource, List<FollowDTO> objects, int user_id) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
        this.objects = objects;

        this.user_id = user_id;

        client = new AsyncHttpClient();
        response = new HttpResponse();

        imageLoaderInit();
    }
    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if(!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration =
                    ImageLoaderConfiguration.createDefault(activity);
            imageLoader.init(configuration);
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);
        options = builder.build();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }
        final FollowDTO item = getItem(position);
        if(item != null) {
            CircleImageView imageView = convertView.findViewById(R.id.CircleImageView);
            TextView textViewLogin  = convertView.findViewById(R.id.textViewLogin);
            TextView textViewName  = convertView.findViewById(R.id.textViewName);
            final Button button = convertView.findViewById(R.id.button);
            button.setFocusable(false);
            textViewLogin.setText(item.getLogin_id());
            textViewName.setText(item.getName());

            if(!item.getProfile_photo().equals("null")) {
                String photo = "http://192.168.0.13:8080/image/storage/" +item.getProfile_photo();
                imageLoader.displayImage(photo, imageView, options);
            } else {
                imageView.setImageResource(R.drawable.ic_stub);
            }

            if(item.isFollowStatus()) {         // 트루이면 내가 팔로우 하고 있는 상태
                button.setBackgroundResource(R.drawable.for_button_modi);
                button.setText("팔로잉");
                button.setTextColor(Color.BLACK);
                //button.setBackgroundColor(Color.WHITE);
            } else {
                button.setBackgroundResource(R.drawable.unfoll_button);
                button.setText("팔로우");
                button.setTextColor(Color.WHITE);
                //button.setBackgroundColor(Color.rgb(0, 153, 204));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestParams params = new RequestParams();
                    params.put("user_id_owner", user_id);
                    params.put("user_id", item.getUser_id());
                    if(button.getText().toString().equals("팔로우")) {
                        params.put("sign", 0);
                    } else {
                        params.put("sign", 1);
                    }
                    client.post(URL, params, response);
                   // if(check) {
                        if(button.getText().toString().equals("팔로우")) {
                            button.setText("팔로잉");
                            button.setTextColor(Color.BLACK);
                            button.setBackgroundColor(Color.WHITE);
                            Toast.makeText(activity, "팔로우를 했습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            button.setText("팔로우");
                            button.setTextColor(Color.WHITE);
                            button.setBackgroundColor(Color.rgb(0, 153, 204));
                            Toast.makeText(activity, "팔로우를 취소 했습니다.", Toast.LENGTH_SHORT).show();
                     //   }
                    }

                }
            });
        }
        return convertView;
    }
    public class HttpResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");
                sign = json.getInt("sign");
                if (result > 0) {
                    check = true;
                } else {
                    check = false;
                    Toast.makeText(activity, "실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신 실패1111", Toast.LENGTH_SHORT).show();
        }
    }
}