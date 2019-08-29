package com.example.izenstargram.search;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.feed.model.PostImage;
import com.example.izenstargram.search.adapter.SearchTabRandomAdapter;
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

public class SearchTabRandomFragment extends Fragment {

    // 서버
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://192.168.0.55:8080/project/selectPostImageRandom.do";
    List<PostImageDTO> list;
    SearchTabRandomAdapter adapter;
    GridView gridView;
    Activity activity = getActivity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("[INFO]", "TabRandomFragment : onCreateView() 실행");
        View view = inflater.inflate(R.layout.search_tab_random_layout, container, false);
        list = new ArrayList<>();
        adapter = new SearchTabRandomAdapter(getActivity(), R.layout.search_gridview_item, list);
        gridView = view.findViewById(R.id.gridViewForSearchRandom);
        gridView.setAdapter(adapter);
        // 서버
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);
        return view;
    }

    @Override
    public void onResume() {
        Log.d("[INFO]", "TabRandomFragment : onResume() 시작");
        super.onResume();
        adapter.clear();    // List의 데이터 삭제
        RequestParams params = new RequestParams();
        client.post(url, response);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("[INFO]", "TabRandomFragment : onCreate() 실행");
        super.onCreate(savedInstanceState);
    }


    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity = getActivity();

        public HttpResponse(Activity activity) {
            Log.d("[INFO]", "TabRandomFragment : HttpResponse() ");
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.d("[INFO]", "TabRandomFragment : onSuccess() 실행");
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject temp = data.getJSONObject(i);
                    PostImageDTO postImageDTO = new PostImageDTO();
                    postImageDTO.setImage_url(temp.getString("image_url"));
                    Log.d("[INFO]", "TabRandomFragment : image_url = " + postImageDTO.getImage_url());
                    adapter.add(postImageDTO);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.d("[INFO]", "TabRandomFragment : onFailure() 진입" + statusCode);
            //Toast.makeText(getContext(), "Tab user 연결실패", Toast.LENGTH_SHORT).show();
        }
    }

}

