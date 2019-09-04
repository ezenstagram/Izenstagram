package com.example.izenstargram.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.MainActivity;
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

public class SearchTabTagFragment extends Fragment implements AdapterView.OnItemClickListener {

    // 서버
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://192.168.0.32:8080/project/selectTagNameByLetter.do";
    String letter_to_search;
    List<AllTagDTO> list;
    SearchTabTagAdapter adapter;
    ListView listView;
    Activity activity;
    ArrayList<AllTagDTO> tagNameList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab_tag_layout, container, false);
        //View viewSearchFrag = inflater.inflate(R.layout.search_layout, container, false); (작동됨)
        //View viewSearchFrag = getLayoutInflater().inflate(R.layout.search_layout, null); //(작동됨)
        activity = getActivity();
        list = new ArrayList<>();
        //adapter = new SearchTabTagAdapter(getActivity().getApplicationContext(), R.layout.search_list_item, list);
        adapter = new SearchTabTagAdapter(getActivity(), R.layout.search_list_item_tag, list);
        listView = view.findViewById(R.id.listViewTag);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        // 서버
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);

        return view;
    }

    @Override
    public void onResume() {
        //Log.d("[INFO]", "TabTagFragment : onResume() 시작");
        super.onResume();
        adapter.clear();    // List의 데이터 삭제
        letter_to_search = SearchFragment.letter_to_search;
        RequestParams params = new RequestParams();
        //Log.d("[INFO]", "onResume : letter_to_search= " + letter_to_search);
        params.put("letter_to_search", letter_to_search);
        client.post(url, params, response);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Log.d("[INFO]", "TabTagFragment : onCreate() 실행");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        AllTagDTO item = adapter.getItem(position);
        //Log.d("[INFO]", "TabTagFragment : onItemClick() : position=" + position);
        int tag_id = item.getTag_id();
        String tag_name = item.getTag_name();
        //Log.d("[INFO]", "TabTagFragment : onItemClick() : tag_id=" + tag_id);
        //Log.d("[INFO]", "TabTagFragment : onItemClick() : tag_name=" + tag_name);
        Fragment fragment = new SearchTagClickFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tag_id", tag_id);
        bundle.putString("tag_name", tag_name);
        fragment.setArguments(bundle);
        ((MainActivity) activity).replaceFragment(R.id.frame_layout, fragment, "search");

    }

    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity = getActivity();

        public HttpResponse(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject temp = data.getJSONObject(i);
                    AllTagDTO allTagDTO = new AllTagDTO();
                    allTagDTO.setTag_id(temp.getInt("tag_id"));
                    allTagDTO.setTag_name(temp.getString("tag_name"));
                    adapter.add(allTagDTO);

                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            // Log.d("[INFO]", "TabTagFragment : onFailure() 진입" + statusCode);
        }
    }
}