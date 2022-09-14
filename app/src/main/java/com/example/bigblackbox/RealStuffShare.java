package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.PushInfoAdapter;
import com.example.bigblackbox.entity.PushInfo;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class RealStuffShare extends AppCompatActivity {
    final List<PushInfo> pi = new ArrayList<>();
    private PushInfoAdapter mPushInfoAdapter;
    private ImageView addBtn,searchBtn;

    private SQLiteDatabase mDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_real_stuff_share);

        addBtn = findViewById(R.id.addBtnShare);
        searchBtn = findViewById(R.id.searchBtnShare);

        // 绑定控件，将数据库查询结果显示在相应的ListView中
        final ListView listView = findViewById(R.id.shareList);

        mPushInfoAdapter = new PushInfoAdapter(this, pi);
        listView.setAdapter(mPushInfoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushInfo pushInfo = pi.get(position);
                Intent intent = new Intent(RealStuffShare.this, Push_detail.class);
                // 根据用户点击的帖子，从实体类获取推送消息ID，并将该值传入Push_detail中
                intent.putExtra("pushInfoID",pushInfo.getPushID());
                startActivity(intent);
            }
        });

        // 列表长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(UserInfo.isAdmin.equals("1")) {
                    final PushInfo pushInfo = pi.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(RealStuffShare.this);
                    builder.setTitle("提示");
                    builder.setMessage("您确定要删除该帖子吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (UserInfo.isAdmin.equals("1")) {
                                mDB.execSQL("delete from pushing where push_id = ?",
                                        new String[]{String.valueOf(pushInfo.getPushID())});
                                Toast.makeText(RealStuffShare.this, "删除成功", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(RealStuffShare.this, RealStuffShare.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RealStuffShare.this, "权限不足!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.create().show();
                }
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
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RealStuffShare.this, AddPush.class);
                intent.putExtra("follow",1);
                intent.putExtra("judgeID",5287);
                startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RealStuffShare.this);
                final LinearLayout linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.dialogname,null);
                final EditText albumName = linearLayout.findViewById(R.id.eUser);
                albumName.setHint("请输入关键字");
                builder.setTitle("帖子查找");
                builder.setView(linearLayout);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pi.clear();
                        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from pushing where push_title like ? and push_follow = '1' order by push_time desc", new String[]{"%" + String.valueOf(albumName.getText()).trim() + "%"});
                        while (cursor.moveToNext()) {
                            pi.add(new PushInfo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6)));
                        }if(pi.size() == 0){
                            AlertDialog.Builder b = new AlertDialog.Builder(RealStuffShare.this);
                            b.setTitle("提示");
                            b.setMessage("未搜索到相关信息，请检查关键字或帖子是否存在!");
                            b.setNegativeButton("确定", null);
                            b.create().show();
                        }else {
                            listView.setAdapter(new PushInfoAdapter(RealStuffShare.this, pi));
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });
    }

    private void showData() {
        pi.clear();                //清除List中的数据，防止数据显示出错
        try(Cursor cursor = mDB.rawQuery("select * from pushing where push_follow = '1' order by push_time desc",new String[0])){
            while(cursor.moveToNext()){
                pi.add(new PushInfo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6)));
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
