package com.example.izenstargram.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.izenstargram.R;
import com.example.izenstargram.search.adapter.SearchPagerAdapter;

public class SearchFragment extends Fragment implements View.OnClickListener {

    SearchPagerAdapter searchPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    Button button;
    EditText editText;
    String letter_to_search;
    MenuItem mSearch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        Log.d("[INFO]", "SEARCH : onCreateView() ");
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
        editText = view.findViewById(R.id.editText);
       // searchPagerAdapter = new SearchPagerAdapter(
      //          getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        // 수정수정이로 수정
        searchPagerAdapter = new SearchPagerAdapter(
                this.getChildFragmentManager(), 4); ///???
        viewPager.setAdapter(searchPagerAdapter);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        viewPager.onSaveInstanceState();    // 수정수정이 코드

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("[INFO]", "SEARCH : tab.getPosition() : " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
                searchPagerAdapter.notifyDataSetChanged();  // 수정수정이
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                searchPagerAdapter.notifyDataSetChanged();  //수정수정이
            }
        });
        // 수정수정이
        TabLayout.Tab tab = tabLayout.getTabAt(0);  // 0: 검색아이콘눌렀을 때 첫 탭
        tab.select();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();



    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id= item.getItemId();
//        if(id==R.id.button){
//            Log.d("[INFO]", "SEARCH : onOptionsItemSelected()");
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //    // Search 메뉴 구현 시작(방법1)
//    // 메뉴 생성하는 onCreateOptionsMenu
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//
//        // search_menu.xml 등록
//        MenuInflater menuInflater = inflater;
//        menuInflater.inflate(R.menu.search_menu, menu);
//        mSearch = menu.findItem(R.id.search);
//
//        // 메뉴 아이콘 클릭했을 시 확장, 취소했을 시 축소
//        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {  //확장
//                Log.d("[INFO]", "TabUserFragment : onMenuItemExpand()");
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {    //축소
//                Log.d("[INFO]", "TabUserFragment : onMenuItemActionCllapse()");
//                return false;
//            }
//        });
//
//        // menuItem 을 이용해서 SearchView 변수 생성
//        SearchView searchView = (SearchView) mSearch.getActionView();
//        // 확인버튼 활성화
//        searchView.setSubmitButtonEnabled(true);
//        // SearchView의 검색 이벤트
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            // 검색버튼 눌렀을 경우
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.d("[INFO]", "TabUserFragment : onQueryTextSubmit (검색버튼눌림)");
//                editText.setText(query);
//                return true;
//            }
//            // 텍스트가 바뀔 때마다 호출됨
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.d("[INFO]", "TabUserFragment : onQueryTextChange (텍스트바뀌고있음)");
//                editText.setText(newText);
//                return true;
//            }
//        });
//    }






    @Override
    public void onClick(View v) {
        Log.d("[INFO]", "SearchFragment : onClick() 실행됨");
        letter_to_search = editText.getText().toString().trim();

         //Fragment로 letter_to_search 값 전달하기......외않되
        SearchTabTagFragment searchTabTagFragment = new SearchTabTagFragment();
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
        Log.d("[INFO]", "SearchFrag에서 letter_to_search (" + letter_to_search + ")를 TabTag로 전달하려함");
        bundle.putString("letter_to_search", letter_to_search); // key , value
        searchTabTagFragment.setArguments(bundle);
        TabLayout.Tab tab = tabLayout.getTabAt(3);
        tab.select();

       // mSearch.expandActionView();
    }


}