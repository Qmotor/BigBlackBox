package com.example.bigblackbox;


import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


import com.example.bigblackbox.adpater.ReplyAdpater;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.entity.Reply;

import java.util.ArrayList;
import java.util.List;

public class Post_detail extends AppCompatActivity {
    private TextView name,time,title,content;
    SQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        name = findViewById(R.id.postName);
        time = findViewById(R.id.postTime);
        title = findViewById(R.id.postTitle);
        content = findViewById(R.id.postContent);
        helper = new DbUtil(this);
        int post_id = getIntent().getIntExtra("postID",-1);
        Posting p = null;
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from posting where postID = ?",new String[]{String.valueOf(post_id)})){
                if(cursor.moveToNext()){
                    p = new Posting(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5));
                }
            }
        }
        name.setText(p.getName());
        time.setText(p.getTime());
        title.setText(p.getTitle());
        content.setText(p.getContent());

        final List<Reply> r = new ArrayList<>();
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from postReply where reply_postID = ?",new String[]{String.valueOf(post_id)})){
                while (cursor.moveToNext()){
                    r.add(new Reply(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                }
            }
        }
        final ListView listView = findViewById(R.id.replyList);
        listView.setAdapter(new ReplyAdpater(this,r));
    }

}