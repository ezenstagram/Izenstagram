package com.example.izenstargram.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class JoinSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView1;
    Button button;

    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.5:8080/project/find_userId.do";
    String login_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_success);

        textView1 = findViewById(R.id.textView1);
        button = findViewById(R.id.button);

        client = new AsyncHttpClient();
        response = new HttpResponse(this);

        login_id = getIntent().getStringExtra("login_id");

        String str = textView1.getText().toString().trim();
        str = String.format(str, login_id);
        textView1.setText(str);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                RequestParams params = new RequestParams();
                params.put("login_id", login_id);           // login_id를 이용해서 user_id를 얻어온다. (공유 하기 위해)
                client.post(URL, params, response);
                break;
        }
    }
    public class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;

        HttpResponse(Activity activity) {this.activity = activity;}
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            Intent intent = null;
            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");
                if(result > 0) {
                    int user_id = json.getInt("user_id");
                    SharedPreferences pref = getSharedPreferences("CONFIG", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("user_id", user_id);
                    editor.putString("login_id", login_id);
                    editor.commit();

                    intent = new Intent(activity, MainActivity.class);

                } else {
                    Toast.makeText(activity, "로그인 실패", Toast.LENGTH_SHORT).show();
                    intent = new Intent(activity, GoLoginActivity.class);

                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
