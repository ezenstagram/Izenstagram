package com.example.izenstargram.login;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.helper.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class FindLoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    TabHost tabHost;
    Button button1, button2, buttonAuth1, buttonAuth2;

    TextView textView1;
    EditText editText1, editText2, editTextAuth_email, editTextAuth_tel;
 //   TabWidget tab;
    LinearLayout linearLayout1, linearLayout2;
    RegexHelper regexHelper;
    SmsManager sms;

    String msg = null;
    String auth = "";
    String email;
    String tel;

    boolean hideTel = false;
    boolean hideEmail = false;
    String send_loginId;

    String[] login_ids;
    boolean emailAuthConfirm = false;
    boolean telAuthConfirm = false;
    boolean pressCheck = false;
    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.13:8080/project/User_id_emailandtel.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_login);

        client = new AsyncHttpClient();
        response = new HttpResponse(this);
        sms = SmsManager.getDefault();

        tabHost = findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("A").setContent(R.id.tab1).setIndicator("이메일");
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("B").setContent(R.id.tab2).setIndicator("전화번호");
        tabHost.addTab(tab2);
       // tab = findViewById(R.id.tab);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        buttonAuth1 = findViewById(R.id.buttonAuth1);
        buttonAuth2 = findViewById(R.id.buttonAuth2);
        textView1 = findViewById(R.id.textView1);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editTextAuth_email = findViewById(R.id.editTextAuth_email);
        editTextAuth_tel = findViewById(R.id.editTextAuth_tel);
        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);

        String str = "이미 계정이 있으신가요? 로그인";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#0099cc")), 14, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView1.setText(ssb);

        textView1.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        buttonAuth1.setOnClickListener(this);
        buttonAuth2.setOnClickListener(this);
      //  tab.setOnClickListener(this);

        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);

        permissionCheck();
    }
    @Override
    protected void onResume() {
        super.onResume();
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        buttonAuth1.setText("확인");
        buttonAuth2.setText("확인");
        emailAuthConfirm = false;
        telAuthConfirm = false;
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
        regexHelper = RegexHelper.getInstance();
        msg = null;
        RequestParams params = new RequestParams();
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button1:

                if(emailAuthConfirm) {
                    intent = new Intent(this, ChangePasswordActivity.class);
                    intent.putExtra("login_id", send_loginId);
                    emailAuthConfirm = false;
                } else {

                    if(hideEmail) {
                        Toast.makeText(this, "인증번호 확인을 받으세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    intent = null;
                    email = editText1.getText().toString().trim();

                    if(msg == null && !regexHelper.isValue(email)) {
                        msg = "이메일을 입력해주세요";
                    } else if(msg == null && !regexHelper.isEmail(email)) {
                        msg = "이메일이 형식에 맞지 않습니다.";
                    }
                    if(msg != null) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    params.put("email", email);
                    client.post(URL, params, response);
                }
                break;
            case R.id.button2:
                if(telAuthConfirm) {        // true 상태이면, 인증을 받은 상태이면
                    intent = new Intent(this, ChangePasswordActivity.class);
                    intent.putExtra("login_id", send_loginId);
                    telAuthConfirm = false;
                } else {
                    if(hideTel) {
                        Toast.makeText(this, "인증번호 확인을 받으세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    intent = null;
                    tel = editText2.getText().toString().trim();

                    if(msg == null && !regexHelper.isValue(tel)) {
                        msg = "전화번호를 입력해주세요";
                    } else if(msg == null && !regexHelper.isCellPhone(tel)) {
                        msg = "전화번호가 형식에 맞지 않습니다.";
                    }
                    if(msg != null) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    params.put("tel", tel);
                    client.post(URL, params, response);
                }
                break;
            case R.id.textView1:
                intent = new Intent(this, GoLoginActivity.class);

                break;

            case R.id.buttonAuth1:
                String emailAuth = editTextAuth_email.getText().toString();

                if(emailAuth.equals(auth)) {
                    emailAuthConfirm = true;
                    buttonAuth1.setText("V");
                    auth = "";
                } else {
                    Toast.makeText(this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonAuth2:
                String telAuth = editTextAuth_tel.getText().toString();

                if(telAuth.equals(auth)) {
                    telAuthConfirm = true;
                    buttonAuth2.setText("V");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    if(login_ids.length ==1) {
                        send_loginId = login_ids[0];
                    } else {
                        builder.setTitle("비밀번호를 찾을 계정을 선택해주세요.");
                        builder.setItems(login_ids, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                      send_loginId = login_ids[which];
                                Toast.makeText(FindLoginActivity.this, send_loginId, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    public void sendEmail() {
        Log.d("[INFO", "sendEmail() 호출");

        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.GONE);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        makeRandom();

        GMailSender sender1 = new GMailSender("hongji0516e@gmail.com","cfecrtapsxersptz");
        try {
            sender1.sendMail(
                    "Ezenstargram 인증번호",
                    "인증번호\n"+auth,
                    "Ezenstargram",
                    email
            );
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideEmail = true;
    }
    public void sendPhone() {

        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.VISIBLE);
        makeRandom();
        Toast.makeText(this, auth, Toast.LENGTH_SHORT).show();
        if (pressCheck == false) {
            // 문자 발송하기
            sms.sendTextMessage(tel, null, "인증번호\n"+auth,  null, null);
            // 딱 한번만 문자발송을 하기 위해서, 눌림상태를 저장
            pressCheck = true;
        }
        hideTel = true;
    }

    private String makeRandom() {
        auth = "";
        Random random = new Random();
        for(int i=0; i<6; i++) {
            auth += random.nextInt(10);
        }
        return auth;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        hideEmail = false;
        hideTel = false;
    }
    @Override
    public void afterTextChanged(Editable s) {}

    public class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;
        HttpResponse(Activity activity) {this.activity = activity;}

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세요.");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            AlertDialog.Builder builder =  null;
            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");
                int div = json.getInt("div");
                Log.d("[INFO", "result = " +result);
                Log.d("[INFO", "result = " +result);

                if(div == 0) {      // 이메일
                    if(result > 0){
                        send_loginId = json.getString("login_id");
                        sendEmail();
                    } else {
                        builder = new AlertDialog.Builder(activity);
                        builder.setMessage("일치하는 이메일이 없습니다.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                linearLayout1.setVisibility(View.GONE);
                                editText1.setText("");
                                editTextAuth_email.setText("");
                            }
                        });
                    }
                } else {            // div가 1(tel)인 경우
                    if(result > 0) {

                        JSONArray list = json.getJSONArray("list");
                        int list_size = list.length();
                        builder = new AlertDialog.Builder(activity);

                        if(list_size==1) {
                            sendPhone();
                        } else {
                            sendPhone();
                            login_ids = new String[list_size];
                            for(int i=0; i< list_size; i++) {
                                login_ids[i] = list.getString(i);
                            }
                        }
                    } else {
                        builder = new AlertDialog.Builder(activity);
                        builder.setMessage("일치하는 전화번호가 없습니다.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                linearLayout2.setVisibility(View.GONE);
                                editText2.setText("");
                                editTextAuth_tel.setText("");
                            }
                        });
                    }
                }
                if(builder != null) {
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