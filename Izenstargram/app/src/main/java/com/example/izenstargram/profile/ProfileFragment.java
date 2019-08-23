package com.example.izenstargram.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.izenstargram.R;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    Animation translateLeftAnim;
    Animation translateRightAnim;
    Animation translateLeftmainAnim;
    Animation translateRightmainAnim;
    LinearLayout slidingPanel;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    Button button;
    TextView textViewLogin_id1, textViewLogin_id2;
    LinearLayout linearLayout, linearLayouttouch;
    boolean isPageOpen=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);// attachToRoot는 일단 false로..

        SharedPreferences pref = getActivity().getSharedPreferences("CONFIG", MODE_PRIVATE);
        String login_id = pref.getString("login_id", null);
        int user_id = pref.getInt("user_id", 0);

        // 메뉴
        translateLeftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translate_left);
        translateRightAnim =AnimationUtils.loadAnimation(getContext(),R.anim.translate_right);
        translateLeftmainAnim =AnimationUtils.loadAnimation(getContext(), R.anim.translatemain_left);
        translateRightmainAnim =AnimationUtils.loadAnimation(getContext(),R.anim.translatemain_right);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        linearLayouttouch = (LinearLayout) view.findViewById(R.id.linearLayouttouch);
        slidingPanel = (LinearLayout) view.findViewById(R.id.slidingPanel);
        button = (Button)view.findViewById(R.id.button);

        linearLayouttouch.setVisibility(View.GONE);

        textViewLogin_id1 = view.findViewById(R.id.TextViewLogin_id1);
        textViewLogin_id2 = view.findViewById(R.id.TextViewLogin_id2);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        textViewLogin_id1.setText(login_id);
        textViewLogin_id2.setText(login_id);

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        translateLeftAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(isPageOpen){
                    // slidingPanel.setVisibility(View.INVISIBLE);
                    isPageOpen=false;
                }else{
                    isPageOpen=true;
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        translateRightAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(isPageOpen){
                    //slidingPanel.setVisibility(View.INVISIBLE);
                    isPageOpen=false;
                }else{
                    isPageOpen=true;
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPageOpen){
                    moveToRight();
                }else{
                    moveToLeft();
                }
            }
        });
        linearLayouttouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen) {
                    moveToRight();
                }
            }
        });
        return view;
    }
    public void moveToLeft() {
        slidingPanel.startAnimation(translateLeftAnim);
        linearLayout.startAnimation(translateLeftmainAnim);
        slidingPanel.setVisibility(View.VISIBLE);
        button.setClickable(false);
        linearLayout.setEnabled(false);
        disableEnableControls(false, linearLayout);
        linearLayouttouch.setVisibility(View.VISIBLE);
    }
    public void moveToRight() {
        slidingPanel.startAnimation(translateRightAnim);
        linearLayout.startAnimation(translateRightmainAnim);
        slidingPanel.setVisibility(View.GONE);
        button.setClickable(true);
        disableEnableControls(true, linearLayout);
        linearLayouttouch.setVisibility(View.GONE);
    }
    // linearLayout 기능 죽이기
    private void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }
}