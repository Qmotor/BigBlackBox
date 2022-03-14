package com.example.bigblackbox.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bigblackbox.Add_post;
import com.example.bigblackbox.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.Search;
import com.example.bigblackbox.UserInfo;
import com.example.bigblackbox.adapter.PushInfoAdapter;
import com.example.bigblackbox.entity.PushInfo;
import com.example.bigblackbox.Push_detail;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    private ImageView addBtn,searchBtn;
    SQLiteOpenHelper helper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DbUtil(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addBtn = view.findViewById(R.id.addBtnHome);
        searchBtn = view.findViewById(R.id.searchBtnHome);
        final List<PushInfo> pi = new ArrayList<>();
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from pushPosting order by pushPostingTime desc",new String[0])){
                while(cursor.moveToNext()){
                    pi.add(new PushInfo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                }
            }
        }
        /*
        绑定控件，将数据库查询结果显示在相应的ListView中
         */
        ListView listView = view.findViewById(R.id.homeList);
        listView.setAdapter(new PushInfoAdapter(getContext(),pi));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushInfo pushInfo = pi.get(position);
                Intent intent = new Intent(getContext(), Push_detail.class);
                /*
                根据用户点击的帖子，从实体类获取推送消息ID，并将该值传入Push_detail中
                 */
                intent.putExtra("pushInfoID",pushInfo.getPushID());
                startActivity(intent);
            }
        });

        // 判断当前登录用户是否为管理员，若是，则在”主页“显示相应控件，否则直接移除控件
        if(UserInfo.isAdmin.equals("1")) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                    /*
                    当ListView不滚动时，悬浮按钮状态为可见
                     */
                        case 0:
                            addBtn.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);
                            break;
                     /*
                      当ListView滚动时，悬浮按钮状态为隐藏
                      */
                        case 1:
                        case 2:
                            addBtn.setVisibility(View.GONE);
                            searchBtn.setVisibility(View.GONE);
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }else {
            addBtn.setVisibility(View.GONE);
            searchBtn.setVisibility(View.GONE);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"123",Toast.LENGTH_SHORT).show();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"456",Toast.LENGTH_SHORT).show();
            }
        });
    }
}