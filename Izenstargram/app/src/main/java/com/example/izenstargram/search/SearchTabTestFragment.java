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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchTabTestFragment extends Fragment implements View.OnClickListener {

    Button button;
    EditText editText, editTextTabTest1, editTextTabTest2;
    int post_id = 3;
    String fullContent = "";
    Activity activity = getActivity();
    String url = "http://192.168.0.5:8080/project/putTagIntoPost.do";
    AsyncHttpClient client;
    HttpResponse response;
    String letter_to_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("[INFO]", "TabTest : onCreateView() 시작");
        View view = inflater.inflate(R.layout.search_tab_test_layout, container, false);
        View viewSearchFrag = getLayoutInflater().inflate(R.layout.search_layout, null); //(작동됨)
        editText = viewSearchFrag.findViewById(R.id.editText);
        ///////////////////////letter_to_search = ///////////////여기를해조야합니다...
        editText.setText(letter_to_search);
        editTextTabTest1 = view.findViewById(R.id.editTextTabTest1);

        button = view.findViewById(R.id.button);
        editTextTabTest2 = view.findViewById(R.id.editTextTabTest2);

//        letter_to_search = getArguments().getString("letter_to_search");
//        Log.d("[INFO]", "SearchFrag에서 준 letter_to_search (" + letter_to_search + ")를 전달받음");


        //서버
        client = new AsyncHttpClient();
        response = new HttpResponse(activity);
        return view;
    }

    @Override
    public void onResume() {
        Log.d("[INFO]", "TabTest : onResume()");
        super.onResume();
        RequestParams params = new RequestParams();
        params.put("post_id", post_id);
        // fullContent = "#새안드태그";
        Log.d("[INFO]", "fullContent = " + fullContent);
        params.put("fullContent", fullContent);
        client.post(url, params, response);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("[INFO]", "TabTest : onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {/////안먹힘
        Log.d("[INFO]", "TabTest : onClick() 실행");
        fullContent = editTextTabTest1.getText().toString().trim();
        onResume();

    }

    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity = getActivity();

        public HttpResponse(Activity activity) {
            Log.d("[INFO]", "TabTest : HttpResponse()");
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.d("[INFO]", "TabTest : onSuccess() 함수 진입");
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String result = json.getString("result");
                editTextTabTest2.setText(result);
            } catch (JSONException e) {
                Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.d("[INFO]", "TabTest : onFailure() 진입" + statusCode);
            Toast.makeText(getContext(), "연결실패", Toast.LENGTH_SHORT).show();
        }
    }
}