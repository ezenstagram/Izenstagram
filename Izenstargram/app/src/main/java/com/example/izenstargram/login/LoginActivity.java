package com.example.izenstargram.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences pref = getSharedPreferences("CONFIG", MODE_PRIVATE);
        int id = pref.getInt("user_id", 0);


        if(id != 0){
            // 로그인이 되어 있으면 main 화면으로
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button1:      // 회원가입 화면으로 이동
                intent = new Intent(this, JoinFirstActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:      // 로그인 화면으로 이동
                intent = new Intent(this, GoLoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
