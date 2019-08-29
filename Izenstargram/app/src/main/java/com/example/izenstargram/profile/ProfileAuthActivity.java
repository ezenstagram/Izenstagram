package com.example.izenstargram.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.helper.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class ProfileAuthActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayoutAuth, linearLayoutNext;
    EditText editText1, editText2, editText3, editText4, editTextAuth;
    TextView textViewCancel, textViewOK,textViewCount;
    Button buttonAuth, buttonOK;

    int sepa;
    int length;
    String auth = "";
    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.32:8080/project/emailAndLogin_id.do";
    SmsManager sms;             // 문자
    boolean pressCheck = false;

    boolean telCheck = false;
    boolean telAuth = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_auth);

        client = new AsyncHttpClient();
        response = new HttpResponse();

        sms = SmsManager.getDefault();
        permissionCheck();

        textViewCancel = findViewById(R.id.textViewCancel);
        textViewOK = findViewById(R.id.textViewOK);
        textViewCount = findViewById(R.id.textViewCount);
        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);
        linearLayout3 = findViewById(R.id.linearLayout3);
        linearLayout4 = findViewById(R.id.linearLayout4);
        linearLayoutAuth = findViewById(R.id.linearLayoutAuth);
        linearLayoutNext = findViewById(R.id.linearLayoutNext);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editTextAuth = findViewById(R.id.editTextAuth);
        buttonAuth = findViewById(R.id.buttonAuth);
        buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(this);
        buttonAuth.setOnClickListener(this);

        sepa = getIntent().getIntExtra("sepa", 0);
        textViewCount.setText(150-editText2.getText().toString().length()+"");
        linearLayoutNext.setVisibility(View.VISIBLE);
        linearLayoutAuth.setVisibility(View.GONE);
        if(sepa == 2) {
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.GONE);
            String login_id = getIntent().getStringExtra("login_id");
            editText1.setText(login_id);
        } else if(sepa == 4) {
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.GONE);
            String intro = getIntent().getStringExtra("introduction");
            editText2.setText(intro);
        } else if(sepa == 5){
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.VISIBLE);
            linearLayout4.setVisibility(View.GONE);
            String email = getIntent().getStringExtra("email");
            editText3.setText(email);

        } else if(sepa == 6){
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.VISIBLE);
            String tel = getIntent().getStringExtra("tel");
            editText4.setText(tel);
        }
        textViewCancel.setOnClickListener(this);
        textViewOK.setOnClickListener(this);
        editText2.addTextChangedListener(this);
    }
    private void permissionCheck() {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.SEND_SMS}, 100);
            }
        }
    }
    @Override
    public void onClick(View v) {
        RegexHelper regexHelper = RegexHelper.getInstance();
        String msg = null;
        RequestParams params;
        switch (v.getId()) {
            case R.id.textViewCancel:
                finish();
                break;
            case R.id.textViewOK:
                if(sepa == 2) {
                    String login_id = editText1.getText().toString();
                    if(login_id.equals(getIntent().getStringExtra("login_id"))) {

                        finish();
                    } else {
                        if(msg == null && !regexHelper.isValue(login_id)) {
                            msg = "아이디를 입력하세요";
                        } else if(msg == null && !regexHelper.loginIdCheck(login_id)) {
                            msg = "아이디는 영문자, 숫자, _ 로만 입력할 수 있습니다.";
                        } else if(msg == null && login_id.length()>19) {
                            msg = "아이디는 20자 미만으로 입력해주세요.";
                        }
                        if(msg!= null) {
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        params = new RequestParams();
                        params.put("login_id", login_id);
                        client.post(URL, params, response);
                    }
                } else if(sepa == 4) {
                    String introduction = editText2.getText().toString();
                    if(msg == null && introduction.length()>149) {
                        msg = "소개글은 150자 미만으로 입력해주세요.";
                    }
                    if(msg!= null) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent();
                    intent.putExtra("introduction", introduction);
                    setResult(RESULT_OK, intent);
                    finish();

                } else if(sepa == 5){
                    String email = editText3.getText().toString();
                    if(msg == null && !regexHelper.isValue(email)) {
                        msg = "이메일을 입력하세요";
                    } else if(msg==null && !regexHelper.isEmail(email)) {
                        msg ="올바른 이메일을 입력하세요.";
                    }
                    if(msg!= null) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    params = new RequestParams();
                    params.put("email", email);
                    client.post(URL, params, response);
                } else if(sepa == 6){
                    String tel = editText4.getText().toString();
                    if(telCheck) {      // telCheck : 인증 번호를 받았으면 true, 그렇지 않으면 false
                        Intent intent = new Intent();
                        intent.putExtra("tel", tel);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(this, "먼저 인증 확인을 받아주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.buttonAuth:
                if(auth.equals(editTextAuth.getText().toString())) {
                   telCheck = true;
                   buttonAuth.setText("V");
                } else {
                    Toast.makeText(this, "인증번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonOK:
                String tel = editText4.getText().toString();
                if(msg == null && !regexHelper.isValue(tel)) {
                    msg = "전화번호를 입력하세요";
                } else if(msg==null && !regexHelper.isCellPhone(tel)) {
                    msg="전화번호를 올바르게 입력하세요.";
                }
                if(msg!= null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                linearLayoutAuth.setVisibility(View.VISIBLE);
                linearLayoutNext.setVisibility(View.GONE);
                auth = makeRandom();
                Toast.makeText(this, auth, Toast.LENGTH_SHORT).show();
                smsSend(tel, auth);
                break;
        }
    }
    private String makeRandom() {
        String auth = "";
        Random random = new Random();

        for(int i=0; i<6; i++) {
            auth += random.nextInt(10);

        }
        return auth;
    }
    private void smsSend(String tel, String auth) {
        if (pressCheck == false) {
            sms.sendTextMessage(tel, null, auth, null, null);
            pressCheck = true;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        length = editText2.getText().toString().length();
        textViewCount.setText(150-length+"");
    }

    @Override
    public void afterTextChanged(Editable s) {}

    public class HttpResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);

            try {
                JSONObject json = new JSONObject(content);
                int div = json.getInt("div");
                int result = json.getInt("result");

                String msg = "";

                if(div == 0) {      // 이메일
                    if(result>0) {
                        Toast.makeText(ProfileAuthActivity.this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("email", json.getString("email"));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else if(div == 1) {       // 로그인 아이디
                    if(result>0) {
                        Toast.makeText(ProfileAuthActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("login_id", json.getString("login_id"));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
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
