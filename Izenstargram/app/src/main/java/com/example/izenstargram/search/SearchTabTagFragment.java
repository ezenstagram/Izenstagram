package com.example.izenstargram.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.search.adapter.SearchTabTagAdapter;
import com.example.izenstargram.search.model.AllTagDTO;
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

public class SearchTabTagFragment extends Fragment {

    // 서버
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://192.168.0.62:8080/project/selectTagNameByLetter.do";
    String letter_to_search;
    List<AllTagDTO> list;
    SearchTabTagAdapter adapter;
    ListView listView;
    Activity activity = getActivity();
    ArrayList<AllTagDTO> tagNameList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab_tag_layout, container, false);
//View viewSearchFrag = inflater.inflate(R.layout.search_layout, container, false); (작동됨)
        //View viewSearchFrag = getLayoutInflater().inflate(R.layout.search_layout, null); //(작동됨)
        list = new ArrayList<>();
        //adapter = new SearchTabTagAdapter(getActivity().getApplicationContext(), R.layout.search_list_item, list);
        adapter = new SearchTabTagAdapter(getActivity(), R.layout.search_list_item_tag, list);
        listView = view.findViewById(R.id.listViewTag);
        listView.setAdapter(adapter);
        // 서버
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);
        return view;
    }

    @Override
    public void onResume() {
        Log.d("[INFO]", "TabTagFragment : onResume() 시작");
        super.onResume();
        adapter.clear();    // List의 데이터 삭제
        letter_to_search = SearchFragment.letter_to_search;
        RequestParams params = new RequestParams();
        Log.d("[INFO]", "onResume : letter_to_search= " + letter_to_search);
        params.put("letter_to_search", letter_to_search);
        client.post(url, params, response);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("[INFO]", "TabTagFragment : onCreate() 실행");
        super.onCreate(savedInstanceState);
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity = getActivity();

        public HttpResponse(Activity activity) {
            Log.d("[INFO]", "TabTagFragment : HttpResponse() ");
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.d("[INFO]", "TabTagFragment : onSuccess() 실행");
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject temp = data.getJSONObject(i);
                    AllTagDTO userDTO = new AllTagDTO();
                    userDTO.setTag_name(temp.getString("tag_name"));
                    adapter.add(userDTO);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            // Log.d("[INFO]", "TabTagFragment : onFailure() 진입" + statusCode);
            //Toast.makeText(getContext(), "Tab Tag 연결실패", Toast.LENGTH_SHORT).show();
        }
    }


}
