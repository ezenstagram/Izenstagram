package com.example.izenstargram.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.helper.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    EditText editText1, editText2;

    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.32:8080/project/change_password.do";

    int user_id = 0;
    String login_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        button = findViewById(R.id.button);

        login_id = getIntent().getStringExtra("login_id");

        client = new AsyncHttpClient();
        response = new HttpResponse(this);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String password = editText1.getText().toString();
                String passwordConfirm = editText2.getText().toString();

                RegexHelper regexHelper = RegexHelper.getInstance();
                String msg = null;

                 if(msg == null && (!regexHelper.isValue(password) || !regexHelper.isValue(passwordConfirm))) {
                    msg = "비밀번호를 입력해주세요.";
                } else if(msg == null && !regexHelper.passwordCheck(password) && !regexHelper.passwordCheck(passwordConfirm)) {
                    msg = "비밀번호가 유효하지 않습니다.";
                } else if(msg == null && !regexHelper.passwordLengthCheck(password) && !regexHelper.passwordCheck(passwordConfirm)) {
                    msg = "비밀번호는 8~15자 사이로 입력해주세요.";
                } else if(!password.equals(passwordConfirm)) {
                     msg = "비밀번호가 일치하지 않습니다.";
                 }
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("password", password);

                params.put("login_id", login_id);
                Log.d("[INFO", login_id);

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
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");
                if(result > 0) {
                    builder.setMessage("비밀번호를 변경하였습니다.");
                } else {
                    builder.setMessage("비밀번호를 변경하지 못했습니다.");
                }
                builder.setCancelable(false);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activity, GoLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            } catch (JSONException e) {


            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }
}

