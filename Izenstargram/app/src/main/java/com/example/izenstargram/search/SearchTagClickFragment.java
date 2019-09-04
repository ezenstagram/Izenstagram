package com.example.izenstargram.search;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.feed.model.PostImage;
import com.example.izenstargram.search.adapter.SearchTagClickAdapter;
import com.example.izenstargram.upload.model.PostImageDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class SearchTagClickFragment extends Fragment implements AdapterView.OnItemClickListener {

    // 서버
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://192.168.0.62:8080/project/selectPostImageByTagId.do";
    List<PostImageDTO> list;
    SearchTagClickAdapter adapter;
    GridView gridView;
    Activity activity;

    int user_id;
    int tag_id;
    String tag_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tag_click_layout, container, false);
        activity = getActivity();
        SharedPreferences pref = getActivity().getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        user_id = pref.getInt("user_id", 0);
        if(getArguments() != null) {
            tag_id = getArguments().getInt("tag_id", 0);
            tag_name = getArguments().getString("tag_name");
        }
        //Log.d("[INFO]", "SearchTagClickFrag: user_id = " + user_id);
        list = new ArrayList<>();
        adapter = new SearchTagClickAdapter(getActivity(), R.layout.search_gridview_item_2, list);
        gridView = view.findViewById(R.id.gridViewForSearchTagClick);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        // 서버
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();    // List의 데이터 삭제
        RequestParams params = new RequestParams();
        //Log.d("[INFO]", "새로 만든 SearchTagClickAdapter : tag_id = " + tag_id);
        params.put("tag_id", tag_id);
        client.post(url, params, response);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        PostImageDTO item = adapter.getItem(position);
        int post_id = item.getPost_id();
        //Log.d("[INFO]", "SearchTagClickFragment : post_id = " + post_id);
        Fragment fragment = new SearchTagClickClickFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("post_id", post_id);
        fragment.setArguments(bundle);
        ((MainActivity) activity).replaceFragment(R.id.frame_layout, fragment, "search");

    }

    class HttpResponse extends AsyncHttpResponseHandler{
        Activity activity = getActivity();

        public HttpResponse(Activity activity)
        {this.activity = activity;}

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject temp = data.getJSONObject(i);
                    PostImageDTO postImageDTO = new PostImageDTO();
                    postImageDTO.setPost_id(temp.getInt("post_id"));
                    postImageDTO.setImage_url(temp.getString("image_url"));
                    //Log.d("[INFO]", "TabTagClickFragment : image_url = " + temp.getString("image_url"));
                    adapter.add(postImageDTO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            //Log.d("[INFO]", "TabRandomFragment : onFailure() 진입" + statusCode);
            //Toast.makeText(getContext(), "Tab user 연결실패", Toast.LENGTH_SHORT).show();
        }
    }
}
