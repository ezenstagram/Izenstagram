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


public class SearchTabUserFragment extends Fragment implements View.OnClickListener {
    // 서버
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://192.168.0.55:8080/project/selectUserBySearch.do";


    Button button;
    EditText editText;
    MenuItem mSearch;

    List<UserDTO> list;
    SearchTabUserAdapter adapter;
    ListView listView;
    String letter_to_search;
    Activity activity = getActivity();

    ArrayList<UserDTO> userNameList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("[INFO]", "TabUserFragment : onCreateView() 실행");
        View view = inflater.inflate(R.layout.search_tab_user_layout, container, false);


        //View viewSearchFrag = inflater.inflate(R.layout.search_layout, container, false); (작동됨)
        View viewSearchFrag = getLayoutInflater().inflate(R.layout.search_layout, null); //(작동됨)
        button = viewSearchFrag.findViewById(R.id.button);
        editText = viewSearchFrag.findViewById(R.id.editText);
        button.setOnClickListener(this);
        list = new ArrayList<>();
        //adapter = new SearchTabUserAdapter(getActivity().getApplicationContext(), R.layout.search_list_item, list);
        adapter = new SearchTabUserAdapter(getActivity(), R.layout.search_list_item, list);
        listView = view.findViewById(R.id.listViewUser);
        listView.setAdapter(adapter);



        // 서버
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);
        return view;
    }

    @Override
    public void onResume() {
        Log.d("[INFO]", "TabUserFragment : onResume() 시작");
        super.onResume();
        adapter.clear();    // List의 데이터 삭제
        RequestParams params = new RequestParams();
        Log.d("[INFO]", "onResume : letter_to_search(1) = " + letter_to_search);
        String letter_to_search = "h";
        Log.d("[INFO]", "onResume : letter_to_search(2) = " + letter_to_search);
        params.put("letter_to_search", letter_to_search);
        client.post(url, params, response);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("[INFO]", "TabUserFragment : onCreate() 실행");
        super.onCreate(savedInstanceState);
    }


    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity = getActivity();

        public HttpResponse(Activity activity) {
            Log.d("[INFO]", "TabUserFragment : HttpResponse() ");
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.d("[INFO]", "TabUserFragment : onSuccess() 실행");
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject temp = data.getJSONObject(i);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setLogin_id(temp.getString("login_id"));
                    adapter.add(userDTO);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.d("[INFO]", "TabUserFragment : onFailure() 진입" + statusCode);
            Toast.makeText(getContext(), "연결실패", Toast.LENGTH_SHORT).show();
        }
    }






//    // Search 메뉴 구현 시작
//    // 메뉴 생성하는 onCreateOptionsMenu
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//
//        // search_menu.xml 등록
//        MenuInflater menuInflater = inflater;
//        menuInflater.inflate(R.menu.search_menu, menu);
//        mSearch = menu.findItem(R.id.search);
//
//        // 메뉴 아이콘 클릭했을 시 확장, 취소했을 시 축소
//        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {  //확장
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {    //축소
//                return false;
//            }
//        });
//
//        // menuItem 을 이용해서 SearchView 변수 생성
//        SearchView searchView = (SearchView) mSearch.getActionView();
//        // 확인버튼 활성화
//        searchView.setSubmitButtonEnabled(true);
//        // SearchView의 검색 이벤트
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            // 검색버튼 눌렀을 경우
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.d("[INFO]", "TabUserFragment : onQueryTextSubmit (검색버튼눌림)");
//                editText.setText(query);
//                return true;
//            }
//            // 텍스트가 바뀔 때마다 호출됨
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.d("[INFO]", "TabUserFragment : onQueryTextChange (텍스트바뀌고있음)");
//                editText.setText(newText);
//                return true;
//            }
//        });
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Log.d("[INFO]", "TabUserFragment : onClick() : 버튼 눌림");
                break;
        }


    }


}
