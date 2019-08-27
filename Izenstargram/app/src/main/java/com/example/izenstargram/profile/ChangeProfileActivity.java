package com.example.izenstargram.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.helper.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    TextView textViewCancel, textViewOK, textViewChangePhoto;
    CircleImageView imageView;
    EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;

    AsyncHttpClient client;
    ProfileModiResponse response;
    String URL = "http://192.168.0.32:8080/project/changeProfile.do";
    int i=1;
    String photoPath = "";
    int user_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        SharedPreferences  preferences = getSharedPreferences("CONFIG", MODE_PRIVATE);
        user_id = preferences.getInt("user_id", 0);
        textViewCancel = findViewById(R.id.textViewCancel);
        textViewOK = findViewById(R.id.textViewOK);
        imageView = findViewById(R.id.CircleImageView);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        editText7 = findViewById(R.id.editText7);
        textViewChangePhoto = findViewById(R.id.textViewChangePhoto);

        client = new AsyncHttpClient();
        response = new ProfileModiResponse(this);
        Intent fromIntent = getIntent();
        UserDTO userDTO = (UserDTO) fromIntent.getSerializableExtra("userDTO");

        editText2.setText(userDTO.getLogin_id());

        if(userDTO.getName().equals("null")) {
            editText1.setText("");
        } else {
            editText1.setText(userDTO.getName());
        }
        if(userDTO.getWebsite().equals("null")) {
            editText3.setText("");
        } else {
            editText3.setText(userDTO.getWebsite());
        }
        if(userDTO.getIntroduction().equals("null")) {
            editText4.setText("");
        } else {
            editText4.setText(userDTO.getIntroduction());
        }
        if(userDTO.getEmail().equals("null")) {
            editText5.setText("");
        } else {
            editText5.setText(userDTO.getEmail());
        }
        if(userDTO.getTel().equals("null")) {
            editText6.setText("");
        } else {
            editText6.setText(userDTO.getTel());
        }
        if(userDTO.getGender().equals("null")) {
            editText7.setText("");
        } else {
            editText7.setText(userDTO.getGender());
        }
//        editText1.setText(userDTO.getName());
//        editText2.setText(userDTO.getLogin_id());
//        editText3.setText(userDTO.getWebsite());
//        editText4.setText(userDTO.getIntroduction());
//        editText5.setText(userDTO.getEmail());
//        editText6.setText(userDTO.getTel());
//        editText7.setText(userDTO.getGender());

        textViewCancel.setOnClickListener(this);
        textViewOK.setOnClickListener(this);
        textViewChangePhoto.setOnClickListener(this);
        editText2.setOnTouchListener(this);
        editText4.setOnTouchListener(this);
        editText5.setOnTouchListener(this);
        editText6.setOnTouchListener(this);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textViewCancel:
                finish();
                break;
            case R.id.textViewOK:
                confirmInput();
                break;
            case R.id.textViewChangePhoto:
                // 코드 추가하기
                break;
        }

    }

    private void confirmInput() {
        String name = editText1.getText().toString();
        String login_id = editText2.getText().toString();
        String website = editText3.getText().toString();
        String introduction = editText4.getText().toString();
        String email = editText5.getText().toString();
        String tel = editText6.getText().toString();
        String gender = editText7.getText().toString();

        RegexHelper regexHelper = RegexHelper.getInstance();
        String msg = null;

       if(msg == null && !regexHelper.nameCheck(name)) {
            msg = "이름은 영문자, 숫자, 한글, _ 로만 입력할 수 있습니다.";
        } else if(msg == null && name.length()>40) {
            msg = "이름의 길이가 깁니다.";
        } else if(msg==null && !regexHelper.isValue(email) && !regexHelper.isValue(tel)){
            msg= "이메일 또는 확인된 전화번호가 필요합니다.";
        }

        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("name",name);
        params.put("login_id",login_id);
        params.put("website",website);
        params.put("introduction",introduction);
        params.put("email",email);
        params.put("tel",tel);
        params.put("gender",gender);
        params.put("user_id",user_id);
        params.put("profile_photo",photoPath);
        client.post(URL, params, response);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        i++;
        Intent intent = new Intent(this, ProfileAuthActivity.class);
         if(event.getAction() == MotionEvent.ACTION_DOWN){
            switch (v.getId()) {
                case R.id.editText2:
                    intent.putExtra("sepa", 2);
                    intent.putExtra("login_id", editText2.getText().toString());
                    if(i>=2) {
                        Toast.makeText(this, "진입", Toast.LENGTH_SHORT).show();
                        startActivityForResult(intent, 102);
                        i=0;
                    }
                    break;
                case R.id.editText4:
                    intent.putExtra("sepa", 4);
                    intent.putExtra("introduction", editText4.getText().toString());
                    if(i>=2) {
                        startActivityForResult(intent, 104);
                        i=0;
                    }
                    break;
                case R.id.editText5:
                    i++;
                    intent.putExtra("sepa", 5);
                    intent.putExtra("email", editText5.getText().toString());
                    if(i>=2) {
                        startActivityForResult(intent, 105);
                        i=0;
                    }
                    break;
                case R.id.editText6:
                    i++;
                    intent.putExtra("sepa", 6);
                    intent.putExtra("tel", editText6.getText().toString());
                    if(i>=2) {
                        startActivityForResult(intent, 106);
                        i=0;
                    };
                    break;
            }
        }
       return false;

    }

    public class ProfileModiResponse extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public ProfileModiResponse(Activity activity) {
            this.activity = activity;
        }
        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세요..");
            dialog.setCancelable(false);
            dialog.show();
        }
        // 통신 종료료시, 자동 호출
        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog=null;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content = new String(responseBody);
            try {
                JSONObject json = new JSONObject(content);
                int result = json.getInt("result");
                if(result>0) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(activity, "변경 실패", Toast.LENGTH_SHORT).show();
                    finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case 102:
                    String login_id = data.getStringExtra("login_id");
                    editText2.setText(login_id);
                    break;
                case 104:
                    String intro = data.getStringExtra("introduction");
                    editText4.setText(intro);
                    break;
                case 105:
                    String email = data.getStringExtra("email");
                    editText5.setText(email);
                    break;
                case 106:
                    String tel = data.getStringExtra("tel");
                    editText6.setText(tel);
                    break;
            }
        }
    }
}
