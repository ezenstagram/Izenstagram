package com.example.izenstargram.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;
import com.example.izenstargram.helper.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class JoinSecondActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    EditText editText1, editText2, editText3;
    TextView textView;

    String login_id;
    String name;
    String password;
    String tel;
    String email;

    AsyncHttpClient client;
    HttpResponseIdCheck response_idCheck;
    String URL_idCheck = "http://192.168.0.32:8080/project/InfoPresence.do";

    HttpResponseJoin response_join;
    String URL_join = "http://192.168.0.32:8080/project/user_join.do";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_second);

        button = findViewById(R.id.button);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        textView = findViewById(R.id.textView);

        String str = "이미 계정이 있으신가요? 로그인";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#0099cc")), 14, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ssb);

        client = new AsyncHttpClient();
        response_idCheck = new HttpResponseIdCheck(this);
        response_join = new HttpResponseJoin(this);

        int sepa = getIntent().getIntExtra("sepa", 2);

        if(sepa == 0) { tel = getIntent().getStringExtra("tel"); }               // 전화번호
        else if(sepa == 1){ email = getIntent().getStringExtra("email"); }       // 이메일
        else {                // 에러
            Toast.makeText(this, "가입하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        button.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView:
                Intent intent = new Intent(this, GoLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.button:
                login_id = editText1.getText().toString();   // 영문자, 숫자, 언더바, 20글자 미만
                name = editText2.getText().toString();       // 영문자, 숫자, 언더바, 한글, 20미만
                password = editText3.getText().toString();   // 8~15 사이

                RegexHelper regexHelper = RegexHelper.getInstance();
                String msg = null;

                if(msg == null && !regexHelper.isValue(login_id)) {
                    msg = "아이디를 입력하세요";
                } else if(msg == null && !regexHelper.loginIdCheck(login_id)) {
                    msg = "아이디는 영문자, 숫자, _ 로만 입력할 수 있습니다.";
                } else if(msg == null && login_id.length()>19) {
                    msg = "아이디는 20자 미만으로 입력해주세요.";
                } else if(msg == null && !regexHelper.isValue(name)) {
                    msg = "이름을 입력하세요";
                } else if(msg == null && !regexHelper.nameCheck(name)) {
                    msg = "이름은 영문자, 숫자, 한글, _ 로만 입력할 수 있습니다.";
                } else if(msg == null && name.length()>40) {
                    msg = "이름의 길이가 깁니다.";
                } else if(msg == null && !regexHelper.isValue(password)) {
                    msg = "비밀번호를 입력해주세요.";
                } else if(msg == null && !regexHelper.passwordCheck(password)) {
                    msg = "비밀번호가 유효하지 않습니다.";
                } else if(msg == null && !regexHelper.passwordLengthCheck(password)) {
                    msg = "비밀번호는 8~15자 사이로 입력해주세요.";
                }

                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("login_id", login_id);
                client.post(URL_idCheck, params, response_idCheck);
                break;
        }
    }
    public class HttpResponseIdCheck extends AsyncHttpResponseHandler {
        Activity activity;

        HttpResponseIdCheck(Activity activity) {this.activity = activity;}
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);

            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");

                if(result > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("이미 존재하는 아이디입니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editText1.requestFocus();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                } else {
                    RequestParams params = new RequestParams();
                    params.put("login_id", login_id);
                    params.put("name", name);
                    params.put("password", password);
                    params.put("email", email);
                    params.put("tel", tel);
                    client.post(URL_join, params, response_join);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(JoinSecondActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }



    public class HttpResponseJoin extends AsyncHttpResponseHandler {
        Activity activity;

        HttpResponseJoin(Activity activity) {this.activity = activity;}
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            
            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");
                if(result > 0) {
                   Intent intent = new Intent(activity, JoinSuccessActivity.class);
                   intent.putExtra("login_id", login_id);
                   startActivity(intent);
                } else {
                    Toast.makeText(activity, "가입 실패", Toast.LENGTH_SHORT).show();
                }
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }
}
