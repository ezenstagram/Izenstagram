package com.example.izenstargram.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.profile.ProfileFragment;
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
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://192.168.0.32:8080/project/selectUserBySearch.do";
    String letter_to_search;
    List<UserDTO> list;
    SearchTabUserAdapter adapter;
    ListView listView;
    Activity activity;
    ArrayList<UserDTO> userNameList = new ArrayList<>();

   int user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Log.d("[INFO]", "TabUserFragment : onCreateView() 실행");
        View view = inflater.inflate(R.layout.search_tab_user_layout, container, false);
        activity = getActivity();
       // SharedPreferences pref = getActivity().getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        //user_id = pref.getInt("user_id", 0);
        //View viewSearchFrag = inflater.inflate(R.layout.search_layout, container, false); (작동됨)
        //View viewSearchFrag = getLayoutInflater().inflate(R.layout.search_layout, null); //(작동됨)
        list = new ArrayList<>();
        //adapter = new SearchTabUserAdapter(getActivity().getApplicationContext(), R.layout.search_list_item, list);
        adapter = new SearchTabUserAdapter(getActivity(), R.layout.search_list_item_user, list);
        listView = view.findViewById(R.id.listViewUser);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);
        letter_to_search = SearchFragment.letter_to_search;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
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
        //Log.d("[INFO]", "TabUserFragment : onItemClick() : login_id=" + login_id);

        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle(1);
        user_id = list.get(position).getUser_id();
        bundle.putInt("user_id", user_id);
        //Log.d("[INFO]", "TabUserFragment : onItemClick() : user_id=" + user_id);
       // bundle.putInt("user_id",list.get(position).getUser_id());
        fragment.setArguments(bundle);
        ((MainActivity)getActivity()).replaceFragment(R.id.frame_layout, fragment, "profile");

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
                    UserDTO userDTO = new UserDTO();
                    userDTO.setProfile_photo(temp.getString("profile_photo"));
                    userDTO.setLogin_id(temp.getString("login_id"));
                    userDTO.setName(temp.getString("name"));
                    userDTO.setUser_id(temp.getInt("user_id"));
                    user_id = temp.getInt("user_id");
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




