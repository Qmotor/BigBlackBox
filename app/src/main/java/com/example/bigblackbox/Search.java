package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.PostingAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Search extends AppCompatActivity {
    private EditText searchText;
    private DbUtil mHelper;
    private String name;

    public void searchPost() {
        final List<Posting> p = new ArrayList<>();
        final SQLiteDatabase db = mHelper.getReadableDatabase();
            try (Cursor cursor = db.rawQuery("select * from posting where post_title like ? order by post_time desc", new String[]{"%" + searchText.getText().toString().trim() + "%"})) {
                while (cursor.moveToNext()) {
                    p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
                }
            }
        if (p.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("未搜索到相关信息，请检查关键字或帖子是否存在!");
            builder.setNegativeButton("确定", null);
            builder.create().show();
        } else {
            ListView listView = findViewById(R.id.searchList);
            listView.setAdapter(new PostingAdapter(this, p));
            // 列表点击事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Posting posting = p.get(position);
                    Intent intent = new Intent(Search.this, Post_detail.class);
                    intent.putExtra("postID", posting.getPostID());
                    startActivity(intent);
                }
            });
            // 列表长按事件
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Posting posting = p.get(position);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
                    builder.setTitle("提示");
                    String[] items = {"删除帖子", "收藏帖子"};
                    builder.setNegativeButton("取消", null);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: // 删除帖子
                                    builder.setMessage("您确定要删除该帖子吗？");
                                    builder.setPositiveButton("我手滑了0_o", null);
                                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            @SuppressLint("Recycle") Cursor c = db.rawQuery("select * from posting where post_id = ?",
                                                    new String[]{String.valueOf(posting.getPostID())});
                                            if (c.moveToNext()) {
                                                name = c.getString(1);
                                            }
                                            if (name.equals(UserInfo.userName) || UserInfo.isAdmin.equals("1")) {
                                                // 删除操作会将帖子及帖子下的所以评论删除
                                                db.execSQL("delete from posting where post_id = ?",
                                                        new String[]{String.valueOf(posting.getPostID())});
                                                db.execSQL("delete from replying where reply_post_id = ?",
                                                        new String[]{String.valueOf(posting.getPostID())});
                                                Toast.makeText(Search.this, "删除成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Search.this, "权限不足!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    builder.create().show();
                                    break;
                                case 1: // 收藏帖子
                                    int amount;
                                    @SuppressLint("Recycle") Cursor c = db.rawQuery("select * from collection where post_id = ?", new String[]{String.valueOf(posting.getPostID())});
                                    amount = c.getCount();
                                    if (amount == 0) {
                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = new Date(System.currentTimeMillis());
                                        db.execSQL("insert into collection values(null,?,?,?)",
                                                new String[]{UserInfo.userID, String.valueOf(posting.getPostID()), simpleDateFormat.format(date)});
                                        Toast.makeText(Search.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Search.this, "已收藏该帖子，不能重复收藏", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new DbUtil(this);
        setContentView(R.layout.activity_search);
        searchText = findViewById(R.id.edit_search);
        // 软键盘监听器
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchPost();
                    return true;
                }
                return false;
            }
        });



    }
}