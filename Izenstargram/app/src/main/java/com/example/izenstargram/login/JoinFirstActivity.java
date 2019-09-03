package com.example.izenstargram.login;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class JoinFirstActivity extends AppCompatActivity implements View.OnClickListener {
    // 서버
    AsyncHttpClient client;
    HttpResponse response;
    String URL = "http://192.168.0.32:8080/project/InfoPresence.do";

    String email;

    // linearLayout1
    LinearLayout linearLayout1;
    TextView textView;
    EditText editText1, editText2;
    Button button1, button2;
    TabHost tabHost;

    // linearLayout2
    LinearLayout linearLayout2;
    TextView textViewTel, textViewTelChange, textViewSMS;
    EditText editTextAuth;
    Button button3;

    SmsManager sms;             // 문자
    boolean pressCheck = false; // 필요한건지 안필요한건지 모르게따
    String tel;
    boolean goIntent;           // 인텐트로 이동하는 것을 방지하기 위해
    String auth;                // 인증 번호
    int i = 0;      // 인증번호 입력 횟수 초과하는 경우
    int j=1;        // 인증번호 3번 넘게 받을 경우
    boolean timer = true;       // true면 인증번호 받을 수 있는 상태, false면 3분이 아직 지나지 않은 상태

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_first);
        client = new AsyncHttpClient();
        response = new HttpResponse(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tabHost = findViewById(R.id.taphost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("A").setContent(R.id.tab1).setIndicator("전화번호");
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("B").setContent(R.id.tab2).setIndicator("이메일");
        tabHost.addTab(tab2);

        linearLayout1 = findViewById(R.id.linearLayout1);
        textView = findViewById(R.id.textView);     // 이미 계정이 있으신가요? 로그인
        editText1 = findViewById(R.id.editText1);   // 전화번호 입력
        editText2 = findViewById(R.id.editText2);   // 이메일 입력
        button1 = findViewById(R.id.button1);       // 전화번호 다음버튼
        button2 = findViewById(R.id.button2);       // 이메일 다음버튼

        linearLayout2 = findViewById(R.id.linearLayout2);
        textViewTel = findViewById(R.id.textViewTel);               // 번호 텍스트
        textViewTelChange = findViewById(R.id.textViewTelChange);   // 전화번호 변경
        textViewSMS = findViewById(R.id.textViewSMS);       // SMS 재전송
        button3 = findViewById(R.id.button3);               // 인증번호 확인

        sms = SmsManager.getDefault();

        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.GONE);

        editTextAuth = findViewById(R.id.editTextAuth);


        String str = "이미 계정이 있으신가요? 로그인";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#0099cc")), 14, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ssb);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        textViewTelChange.setOnClickListener(this);
        textViewSMS.setOnClickListener(this);

        textView.setOnClickListener(this);
        permissionCheck();

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
        Intent intent = null;
        RegexHelper regexHelper = RegexHelper.getInstance();

        String msg = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (v.getId()) {
            case R.id.textView:
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                goIntent = false;
                break;

            case R.id.button1:
                tel = editText1.getText().toString().trim();
                if(msg == null && !regexHelper.isValue(tel)) {
                    msg = "전화번호를 입력하세요";
                } else if(msg == null && !regexHelper.isCellPhone(tel)) {
                    msg = "전화번호를 올바르게 입력하세요";
                } else {
                    auth = makeRandom();
                    smsSend(tel, auth);
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.VISIBLE);

                    String str = textViewTel.getText().toString().trim();
                    str = String.format(str, tel);
                    textViewTel.setText(str);               // textView에 전화번호 가져오기

                    goIntent = true;

                    Toast.makeText(this, auth, Toast.LENGTH_SHORT).show();      // 임시로 적어둔 코드
                    pressCheck = false;
                }
                break;
            case R.id.button2:
                email = editText2.getText().toString().trim();

                if(msg == null && !regexHelper.isValue(email)) {
                    msg = "이메일을 입력하세요";
                } else if(msg == null && !regexHelper.isEmail(email)) {
                    msg = "이메일을 올바르게 입력하세요";
                } else {
                    RequestParams params = new RequestParams();
                    params.put("email", email);              // 이메일로는 계정 한 개만 만들 수 있다.
                    client.post(URL, params, response);      //   -> 서버로 이메일을 보내 가입된 이메일이 있는지 확인

                    goIntent = true;                         // true이면 startActivity에 가지 않음
                }
                break;
            case R.id.button3:              // 인증번호가 일치하는 경우
                if(editTextAuth.getText().toString().trim().equals(auth)) {
                    intent = new Intent(this, JoinSecondActivity.class);
                    intent.putExtra("sepa", 0);         // 가입 시 이메일로 가입하는지 전화번호로 가입하는지 구분하기 위해.
                    intent.putExtra("tel",tel);
                    goIntent = false;                                   // false면 startActivity로 이동
                } else {                    // 인증번호 일치하지 않는 경우
                    i++;
                    if(i>3) {               // 인증번호 입력 3번 초과 시
                        builder.setMessage("전화번호를 다시 입력해주세요.");
                        editText1.setText("");
                        i=0;
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                linearLayout1.setVisibility(View.VISIBLE);
                                linearLayout2.setVisibility(View.GONE);
                                editTextAuth.setText("");
                            } });
                    } else {                // 인증번호 입력을 3번 초과하지 않았을 경우 보여줄 다이얼로그
                        builder.setMessage("인증번호를 확인해주세요.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {} });
                    }
                    Dialog dialog = builder.create();
                    dialog.show();
                    goIntent = true;
                }
                break;
            case R.id.textViewTelChange:            // 전화번호 변경 textView 클릭 시
                editTextAuth.setText("");
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                break;
            case R.id.textViewSMS:                  // 인증번호 재발급 textView 클릭 시
                if(j>3) {       // 인증번호 세 번 이상 발송 시 전화번호 다시 입력
                    builder.setMessage("전화번호를 다시 입력해주세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            linearLayout1.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.GONE);
                            editText1.setText("");
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                    j=0;
                } else {
                    if(timer) {         // timer가 true이면 문자를 전송할 수 있다.
                        CountDownTimer countDownTimer = new CountDownTimer(18000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                timer = false;
                            }
                            @Override
                            public void onFinish() {
                                timer = true;
                            }
                        }.start();

                        auth = makeRandom();
                        smsSend(tel, auth);                  // 문자 재전송
                       Toast.makeText(this, "문자를 전송했습니다.\n"+auth, Toast.LENGTH_SHORT).show();

                        j++;
                    } else {                                // 문자를 재발급한지 3분이 지나지 않았을 경우
                        builder.setMessage("잠시 후에 다시 시도해주세요.");
                        Dialog dialog = builder.create();
                        dialog.show();
                    }
                }
                break;
        }
        if (msg != null) {
            builder.setMessage(msg);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {} });
            Dialog dialog = builder.create();
            dialog.show();
        } else if(goIntent) {
            // intent 이동 방지
        } else {
            startActivity(intent);
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

    public class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;

        HttpResponse(Activity activity) {this.activity = activity;}
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);

            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");

                if(result>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("이미 존재하는 이메일입니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editText2.requestFocus();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                } else {
                    Intent intent = new Intent(activity, JoinSecondActivity.class);
                    intent.putExtra("sepa",1);  // 0이면 tel로 1이면 이메일로 회원가입
                    intent.putExtra("email", email);
                    startActivity(intent);
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
