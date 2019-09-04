package com.example.izenstargram.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izenstargram.R;
import com.example.izenstargram.helper.RegexHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    TextView textViewCancel, textViewOK, textViewChangePhoto;
    CircleImageView imageView;
    EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;

    AsyncHttpClient client;
    ProfileModiResponse response;
    String URL = "http://192.168.0.62:8080/project/changeProfile.do";
    int i=1;
    String photoPath = "";
    int user_id = 0;
    UserDTO userDTO;

    ImageLoader imageLoader;
    DisplayImageOptions options;
    Bitmap adjustedBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
        imageView = findViewById(R.id.CircleImageView);
        imageLoaderInit();
        client = new AsyncHttpClient();
        response = new ProfileModiResponse(this);
        Intent fromIntent = getIntent();
        userDTO = (UserDTO) fromIntent.getSerializableExtra("userDTO");

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
        if(userDTO.getProfile_photo().equals("null")) {
            imageView.setImageResource(R.drawable.ic_stub);
        } else {
            String photo = "http://192.168.0.13:8080/image/storage/" + userDTO.getProfile_photo();
            imageLoader.displayImage(photo, imageView, options);
        }
        textViewCancel.setOnClickListener(this);
        textViewOK.setOnClickListener(this);
        textViewChangePhoto.setOnClickListener(this);
        editText2.setOnTouchListener(this);
        editText4.setOnTouchListener(this);
        editText5.setOnTouchListener(this);
        editText6.setOnTouchListener(this);
        imageView.setOnClickListener(this);
        textViewChangePhoto.setOnClickListener(this);

    }
    private void imageLoaderInit() {
        // ImageLoader 초기화
        imageLoader = ImageLoader.getInstance();
        if(!imageLoader.isInited()) {       // 초기화 되어있지 않으면
            ImageLoaderConfiguration configuration =
                    ImageLoaderConfiguration.createDefault(this);
            imageLoader.init(configuration);
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);
        options = builder.build();
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
            case R.id.CircleImageView:
                final String[] items;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if(userDTO.getProfile_photo().equals("null")) {
                    items = new String[]{"사진 찍기", "라이브러리에서 선택"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    showCamera();
                                    break;
                                case 1:
                                    showGallery();
                                    break;
                            }
                        }
                    });
                } else {
                    items = new String[]{"현재 사진 삭제", "사진 찍기", "라이브러리에서 선택"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    photoPath = "";
                                    imageView.setImageResource(R.drawable.ic_stub);
                                    break;
                                case 1:
                                    showCamera();
                                    break;
                                case 2:
                                    showGallery();
                                    break;
                            }
                        }
                    });
                }
                Dialog dialog = builder.create();
                dialog.show();
                break;
        }
    }
    public void showCamera() {
        Intent intent = new Intent(this, ProfileCameraActivity.class);
        startActivityForResult(intent, 100);
    }
    public void showGallery() {
        Intent intent = new Intent(this, ProfileGalleryActivity.class);
        startActivityForResult(intent, 100);
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


        String fileName = photoPath.substring( photoPath.lastIndexOf('/')+1, photoPath.length());
        params.put("profile_photo", fileName);
        try {
            params.put("photopath",new File(photoPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.setForceMultipartEntityContentType(true);
        client.post(URL, params, response);
    }
    // editText 클릭 시
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
            System.out.println(statusCode);
            System.out.println(responseBody);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case 100:
                    String photoPath = data.getStringExtra("photoPath");
                    int sepa = data.getIntExtra("sepa", 0);

                    this.photoPath = photoPath;
                    String fileName = photoPath.substring( photoPath.lastIndexOf('/')+1, photoPath.length() );

                    File file = new File(photoPath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

                    Matrix matrix = new Matrix();
                    if(sepa == 0) {
                        matrix.preRotate(90);
                    }
                    adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    imageView.setImageBitmap(adjustedBitmap);

                    break;
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