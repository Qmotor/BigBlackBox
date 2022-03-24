package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.bigblackbox.adapter.PostingAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MyPosting extends AppCompatActivity {
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();


        setContentView(R.layout.activity_my_posting);

        final List<Posting> p = new ArrayList<>();
        /*
        当Sql语句写在try方法里的时候，try方法执行完毕后，程序会自动将mDB对象关闭，使得后面的程序无法调用该对象
        具体解决方法为，单次数据库调用结束后，只需要关闭当前游标，不需要将数据库对象关闭
        只有当前Activity即将关闭或确认之后不再调用，再关闭数据库对象即可
         */
        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from posting where postUserName = ? order by postTime desc", new String[]{UserInfo.userName});
        while (cursor.moveToNext()) {
            p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
        }

        if (p.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示信息");
            builder.setMessage("您还未发过帖哦，快去和大家交流吧");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else {
            ListView listView = findViewById(R.id.myPostList);
            listView.setAdapter(new PostingAdapter(this, p));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Posting posting = p.get(position);
                    Intent intent = new Intent(MyPosting.this, Post_detail.class);
                    intent.putExtra("postID", posting.getPostID());
                    startActivity(intent);
                }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Posting posting = p.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyPosting.this);
                    builder.setTitle("提示");
                    builder.setMessage("您确定要删除该帖子吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDB.execSQL("delete from posting where postUserName = ? and postID = ?",
                                    new String[]{UserInfo.userName, String.valueOf(posting.getPostID())});
                            mDB.execSQL("delete from postReply where reply_postID = ?",
                                    new String[]{String.valueOf(posting.getPostID())});
                            Toast.makeText(MyPosting.this, "删除成功", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }
    }
}