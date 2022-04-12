package com.example.bigblackbox.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bigblackbox.AddPush;
import com.example.bigblackbox.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.UserInfo;
import com.example.bigblackbox.adapter.PushInfoAdapter;
import com.example.bigblackbox.entity.PushInfo;
import com.example.bigblackbox.Push_detail;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    final List<PushInfo> pi = new ArrayList<>();
    private PushInfoAdapter mPushInfoAdapter;
    private ImageView addBtn,searchBtn;
    String isPushAdmin;

    private SQLiteDatabase mDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DbUtil mHelper = new DbUtil(getContext());
        mDB = mHelper.getReadableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addBtn = view.findViewById(R.id.addBtnHome);
        searchBtn = view.findViewById(R.id.searchBtnHome);

        // 绑定控件，将数据库查询结果显示在相应的ListView中
        ListView listView = view.findViewById(R.id.homeList);

        mPushInfoAdapter = new PushInfoAdapter(getContext(), pi);
        listView.setAdapter(mPushInfoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushInfo pushInfo = pi.get(position);
                Intent intent = new Intent(getContext(), Push_detail.class);

                // 根据用户点击的帖子，从实体类获取推送消息ID，并将该值传入Push_detail中
                intent.putExtra("pushInfoID",pushInfo.getPushID());
                startActivity(intent);
            }
        });

        // 列表长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final PushInfo pushInfo = pi.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("提示");
                builder.setMessage("您确定要删除该帖子吗？");
                builder.setPositiveButton("我手滑了0_o", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select is_admin from userInfo where userName = ?",
                                new String[]{UserInfo.userName});
                        if(c.moveToNext()){
                            isPushAdmin = c.getString(0);
                        }
                        if(isPushAdmin.equals("1")){
                            mDB.execSQL("delete from pushPosting where pushPostingID = ?",
                                    new String[]{String.valueOf(pushInfo.getPushID())});
                            Toast.makeText(getContext(),"删除成功", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(),"权限不足!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        // 判断当前登录用户是否为管理员，若是，则在”主页“显示相应控件，否则直接移除控件
        if(UserInfo.isAdmin.equals("1")) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                    // 当ListView不滚动时，悬浮按钮状态为可见
                        case 0:
                            addBtn.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);
                            break;
                      // 当ListView滚动时，悬浮按钮状态为隐藏
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
                Intent intent = new Intent(getContext(), AddPush.class);
                startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"456",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showData() {
        pi.clear();                //清除List中的数据，防止数据显示出错
            try(Cursor cursor = mDB.rawQuery("select * from pushPosting order by pushPostingTime desc",new String[0])){
                while(cursor.moveToNext()){
                    pi.add(new PushInfo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                }
            }
        //通知观察者数据已经变更
        mPushInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        // 重写onResume()方法，保持显示数据常新
        super.onResume();
        showData();
    }
}