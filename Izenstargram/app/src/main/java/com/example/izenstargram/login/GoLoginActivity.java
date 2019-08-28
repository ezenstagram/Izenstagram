package com.example.izenstargram.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class GoLoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    EditText editText1, editText2;
    Button button;
    HttpResponse response;
    AsyncHttpClient client;
    TextView textView, textViewJoin;

    String URL = "http://192.168.0.5:8080/project/user_login.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_login);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        textViewJoin = findViewById(R.id.textViewJoin);

        response = new HttpResponse(this);
        client = new AsyncHttpClient();

        button.setOnClickListener(this);
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);

        textView.setOnClickListener(this);
        textViewJoin.setOnClickListener(this);

        check();
    }

    private void check() {
        String id = editText1.getText().toString();
        String pw = editText2.getText().toString();

        if(id.equals("") || pw.equals("")) {
            button.setBackgroundColor(Color.rgb(217, 229, 250));
            button.setEnabled(false);
        } else {
            button.setBackgroundColor(Color.rgb(177, 191, 250));
            button.setEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {check();}      // text에 변화가 있을 때마다 호출
    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button:
                String login_id = editText1.getText().toString();
                String password = editText2.getText().toString();
                RequestParams params = new RequestParams();
                params.put("login_id", login_id);
                params.put("password", password);

                client.post(URL, params, response);     // 서버로 id와 password 보내기
                break;
            case R.id.textView:
              intent = new Intent(this, FindLoginActivity.class);
                break;
            case R.id.textViewJoin:
                intent = new Intent(this, JoinFirstActivity.class);
                break;
        }
        if(intent != null) {
            startActivity(intent);
        }
    }
    public class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        HttpResponse(Activity activity) {this.activity = activity;}

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);

            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");
                if(result >0) {         // 로그인 성공 시

                    Toast.makeText(activity, "로그인 성공", Toast.LENGTH_SHORT).show();
                    int user_id = json.getInt("user_id");
                    String login_id = json.getString("login_id");
                    SharedPreferences pref = getSharedPreferences("CONFIG", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putInt("user_id", user_id);      // user_id 공유
                    editor.putString("login_id", login_id);
                    editor.commit();

                    Intent intent = new Intent(activity, MainActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {        // 로그인 실패 시
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("아이디 또는 비밀번호를 확인해주세요");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editText1.requestFocus();
                        }});
                    Dialog dialog = builder.create();
                    dialog.show();
                }
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
