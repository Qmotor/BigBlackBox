package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bigblackbox.adpater.PostingAdpater;
import com.example.bigblackbox.entity.Posting;

import java.util.ArrayList;
import java.util.List;

public class MyPosting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        SQLiteDatabase mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_my_posting);

        final List<Posting> p = new ArrayList<>();
        try (SQLiteDatabase db = mHelper.getReadableDatabase()) {
            try(Cursor cursor = db.rawQuery("select * from posting where postUserName = ? order by postTime desc",new String[]{UserInfo.userName})){
                while(cursor.moveToNext()){
                    p.add(new Posting(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5)));
                }
            }
        }

        if(p.size() == 0){
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