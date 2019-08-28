package com.example.izenstargram.feed.Interface;

import com.example.izenstargram.feed.model.PostAll;

import java.util.List;

public interface ILoadListener {
    void onLoadSuccess(List<PostAll> feedPostList);
    void onLoadFail(String message);
}
