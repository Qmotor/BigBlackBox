package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.PostingAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CollPost extends AppCompatActivity {
    static int count = 0;
    private SQLiteDatabase mDB;
    public List<Integer> idList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        setContentView(R.layout.activity_collection);

        final List<Posting> p = new ArrayList<>();
        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from coll_post where user_id = ? order by coll_time desc", new String[]{UserInfo.userID});
        while (c.moveToNext()) {
            if(count == 0){
                idList.add(c.getInt(2));
                count++;
            }else {
                for (int i = 0; i < idList.size(); i++) {
                    if (c.getInt(1) != idList.get(i)) {
                        idList.add(c.getInt(2));
                        break;
                    }
                }
            }
        }
        count = 0;

        if (idList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示信息");
            builder.setMessage("您还未收藏过帖子哦，快去找找感兴趣的帖子吧");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else {
            for(int i = 0; i < idList.size(); i++) {
                @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from posting where post_id = ?", new String[]{String.valueOf(idList.get(i))});
                while (cursor.moveToNext()) {
                    p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
                }
            }

            ListView listView = findViewById(R.id.collectionList);
            listView.setAdapter(new PostingAdapter(this, p));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Posting posting = p.get(position);
                    Intent intent = new Intent(CollPost.this, Post_detail.class);
                    intent.putExtra("postID", posting.getPostID());
                    startActivity(intent);
                }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Posting posting = p.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(CollPost.this);
                    builder.setTitle("提示");
                    builder.setMessage("您确定要取消收藏该帖子吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDB.execSQL("delete from coll_post where user_id = ? and post_id = ?",
                                    new String[]{UserInfo.userID, String.valueOf(posting.getPostID())});
                            Toast.makeText(CollPost.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }
    }

}
