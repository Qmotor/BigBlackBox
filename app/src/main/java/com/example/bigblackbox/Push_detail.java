package com.example.bigblackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bigblackbox.entity.PushInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Push_detail extends AppCompatActivity {
    SQLiteOpenHelper helper;

    @SuppressLint("SetTextI18n")
    public void setTime(String pushTime){
        try {
            TextView time = findViewById(R.id.pushTime);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pDate = ft.parse(pushTime);
            Date nDate = new Date(System.currentTimeMillis());
            assert pDate != null;
            long diff = nDate.getTime() - pDate.getTime();//这样得到的差值是毫秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            if (days == 0 && hours == 0 && minutes == 0) {
                time.setText((diff / 1000) + "秒前");
            } else if (days == 0 && hours == 0) {
                time.setText(minutes + "分钟前");
            } else if (days == 0) {
                time.setText(hours + "小时前");
            } else if(days <= 5){
                time.setText(days + "天前");
            } else {
                time.setText(pushTime);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_detail);
        TextView name = findViewById(R.id.pushName);
        TextView title = findViewById(R.id.pushTitle);
        TextView content = findViewById(R.id.pushContent);
        helper = new DbUtil(this);
        int push_id = getIntent().getIntExtra("pushInfoID",-1);
        PushInfo p = null;
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from pushPosting where pushPostingID = ?",new String[]{String.valueOf(push_id)})){
                if(cursor.moveToNext()){
                    p = new PushInfo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
                    setTime(cursor.getString(4));
                }
            }
        }
        assert p != null;
        name.setText(p.getPushUser());
        title.setText(p.getPushTitle());
        content.setText(p.getPushContent());
    }
}