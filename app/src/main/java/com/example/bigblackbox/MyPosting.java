package com.example.bigblackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bigblackbox.adpater.PostingAdpater;
import com.example.bigblackbox.entity.Posting;

import java.util.ArrayList;
import java.util.List;

public class MyPosting extends AppCompatActivity {
    private DbUtil mHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_my_posting);

        final List<Posting> p = new ArrayList<>();
        try (SQLiteDatabase db = mHelper.getReadableDatabase()) {
            try(Cursor cursor = db.rawQuery("select * from posting where postUserName = ? order by postTime desc",new String[]{UserInfo.userName})){
                while(cursor.moveToNext()){

                    p.add(new Posting(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5)));
                }
            }
        }
        ListView listView = findViewById(R.id.myPostList);
        listView.setAdapter(new PostingAdpater(this, p));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Posting posting = p.get(position);
                Intent intent = new Intent(MyPosting.this, Post_detail.class);
                intent.putExtra("postID", posting.getPostID());
                startActivity(intent);
            }
        });
    }


}