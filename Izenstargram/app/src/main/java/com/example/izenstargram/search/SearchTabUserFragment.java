package com.example.izenstargram.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.profile.UserDTO;
import com.example.izenstargram.search.adapter.SearchTabUserAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class SearchTabUserFragment extends Fragment implements AdapterView.OnItemClickListener {
    // 서버
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://192.168.0.55:8080/project/selectUserBySearch.do";
    //selectTagNameByLetter.do
    String letter_to_search;
    List<UserDTO> list;
    SearchTabUserAdapter adapter;
    ListView listView;
    Activity activity = getActivity();
    ArrayList<UserDTO> userNameList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Log.d("[INFO]", "TabUserFragment : onCreateView() 실행");
        View view = inflater.inflate(R.layout.search_tab_user_layout, container, false);
        //View viewSearchFrag = inflater.inflate(R.layout.search_layout, container, false); (작동됨)
        //View viewSearchFrag = getLayoutInflater().inflate(R.layout.search_layout, null); //(작동됨)
        list = new ArrayList<>();
        //adapter = new SearchTabUserAdapter(getActivity().getApplicationContext(), R.layout.search_list_item, list);
        adapter = new SearchTabUserAdapter(getActivity(), R.layout.search_list_item_user, list);
        listView = view.findViewById(R.id.listViewUser);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        // 서버
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);
        letter_to_search = SearchFragment.letter_to_search;
        return view;
    }

    @Override
    public void onResume() {
        //Log.d("[INFO]", "TabUserFragment : onResume() 시작");
        super.onResume();
        adapter.clear();    // List의 데이터 삭제
        letter_to_search = SearchFragment.letter_to_search;
        RequestParams params = new RequestParams();
        // Log.d("[INFO]", "onResume : letter_to_search= " + letter_to_search);
        params.put("letter_to_search", letter_to_search);
        client.post(url, params, response);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Log.d("[INFO]", "TabUserFragment : onCreate() 실행");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        UserDTO item = adapter.getItem(position);
        //Log.d("[INFO]", "TabUserFragment : onItemClick() : position=" + position);
        String login_id = item.getLogin_id();
//        Log.d("[INFO]", "TabUserFragment : onItemClick() : login_id=" +
////        Intent intent = new Intent(getActivity(), SearchUserClickActivity.class);
////        intent.putExtra("login_id", login_id);
////        startActivity(intent);login_id);
        // startActivityForResult(intent, 100);///////
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 100:
//                // 결과값이 "성공"일 경우만 처리
//                if(resultCode == RESULT_OK) {
//                    RequestParams params = new RequestParams();
//                    params.put("login_id", login_id);
//                    client.post(userInfoURL, params, userInfoResponse);
//                }
//                break;
//        }
//    }


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
                    UserDTO userDTO = new UserDTO();
                    userDTO.setProfile_photo(temp.getString("profile_photo"));
                    userDTO.setLogin_id(temp.getString("login_id"));
                    adapter.add(userDTO);
                }
            } catch (JSONException e) {
                //Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            //Log.d("[INFO]", "TabUserFragment : onFailure() 진입" + statusCode);
            //Toast.makeText(getContext(), "Tab user 연결실패", Toast.LENGTH_SHORT).show();
        }
    }
}
