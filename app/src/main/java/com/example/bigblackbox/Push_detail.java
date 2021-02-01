package com.example.bigblackbox;


import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;


import com.example.bigblackbox.entity.PushInfo;

public class Push_detail extends AppCompatActivity {
    SQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_detail);
        TextView name = findViewById(R.id.pushName);
        TextView time = findViewById(R.id.pushTime);
        TextView title = findViewById(R.id.pushTitle);
        TextView content = findViewById(R.id.pushContent);
        helper = new DbUtil(this);
        int push_id = getIntent().getIntExtra("pushInfoID",-1);
        PushInfo p = null;
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from pushPosting where pushPostingID = ?",new String[]{String.valueOf(push_id)})){
                if(cursor.moveToNext()){
                    p = new PushInfo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
                }
            }
        }
        name.setText(p.getPushUser());
        time.setText(p.getPushTime());
        title.setText(p.getPushTitle());
        content.setText(p.getPushContent());
    }

}