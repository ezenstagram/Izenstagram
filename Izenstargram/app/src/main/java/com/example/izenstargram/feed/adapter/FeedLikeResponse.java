package com.example.izenstargram.feed.adapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class FeedLikeResponse extends AsyncHttpResponseHandler {
    AsyncHttpClient client;
    int mode;

    public FeedLikeResponse(FeedAdapter.ViewHolder viewHolder, int mode) {
        this.mode = mode;
    }

    @Override
    public void onStart() {
      client = new AsyncHttpClient();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
         switch (mode){
             case 1: //좋아요 데이터가 존재하면 mode 1로 가서 데이터를 삭제하고 isLike 상태를 false 로 바꿔줌
                 String document1 = new String(responseBody);
                 try {
                     JSONObject json = new JSONObject(document1);
                     int result = json.getInt("result");
                     if(result>0) {

                     }else{
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 break;

             case 2:
                 String document2 = new String(responseBody);
                 try {
                     JSONObject json = new JSONObject(document2);
                     int result = json.getInt("result");
                     if(result>0) {

                     }else{

                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 break;

         }


    }


    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }







    class HttpResponse extends  AsyncHttpResponseHandler{

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }
}
