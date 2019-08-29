package com.example.izenstargram.helper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.example.izenstargram.MainActivity;
import com.example.izenstargram.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpannableStringMaker {
    private static Activity activity;
    private static SpannableStringMaker instance = null;

    public static SpannableStringMaker getInstance(Activity mainAct) {
        if(instance==null) {
            instance = new SpannableStringMaker();
            activity = mainAct;
        }
        return instance;
    }

    public static void freeInstance(){
        instance = null;
    }

    public SpannableString makeSpannableString(String text, final Map<String, Fragment> clickStrMap, final String fragmentName) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#949494")), 0, text.length(), 0);
        Iterator iterator = clickStrMap.keySet().iterator();
        while (iterator.hasNext()) {
            final String keyStr = (String) iterator.next();
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    ((MainActivity) activity).replaceFragment(R.id.frame_layout, clickStrMap.get(keyStr), fragmentName);
                }
            };
            spanString.setSpan(clickableSpan, text.indexOf(keyStr), text.indexOf(keyStr) + keyStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanString;
    }

    public List<String> getUserTagList(String text) {
        Matcher matcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(text);
        List<String> strList = new ArrayList<>();
        while (matcher.find()) {
            strList.add(text.substring(matcher.start(), matcher.end()));
        }
        return strList;
    }
}
