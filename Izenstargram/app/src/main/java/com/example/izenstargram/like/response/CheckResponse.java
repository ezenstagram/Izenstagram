package com.example.izenstargram.like.response;

import android.app.Activity;
import android.widget.Toast;

import com.example.izenstargram.MainActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CheckResponse extends AsyncHttpResponseHandler {
    Activity activity;

    public CheckResponse(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            int result = jsonObject.getInt("result");
            if (result > 0) {
                ((MainActivity) activity).addBadgeView("♥" + result);
            } else if (result < 0) {
                Toast.makeText(activity, "실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Toast.makeText(activity, "접속실패", Toast.LENGTH_SHORT).show();
    }
}
