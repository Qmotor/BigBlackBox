package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.entity.Teacher;
import com.example.bigblackbox.tool.DbUtil;

public class Teacher_detail extends AppCompatActivity {
    private int tID = -1;
    private SQLiteDatabase mDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        TextView name = findViewById(R.id.tName);
        TextView content = findViewById(R.id.tContent);
        TextView b1 = findViewById(R.id.book1);
        TextView b2 = findViewById(R.id.book2);
        TextView b3 = findViewById(R.id.book3);
        TextView b4 = findViewById(R.id.book4);
        TextView b5 = findViewById(R.id.book5);
        ImageView icon = findViewById(R.id.tIv);

        tID = getIntent().getIntExtra("teacherID", -1);
        Teacher t = null;
        try (Cursor cursor = mDB.rawQuery("select * from teacher where teacher_id = ?", new String[]{String.valueOf(tID)})) {
            if (cursor.moveToNext()) {
               t = new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getBlob(cursor.getColumnIndex("teacher_icon")), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));            }
        }

        assert t != null;
        name.setText(t.getTeacherName());
        // byte[] 转 bitmap
        if(t.getTeacherIcon() != null) {
            Bitmap bmpOut = BitmapFactory.decodeByteArray(t.getTeacherIcon(), 0, t.getTeacherIcon().length);
            icon.setImageBitmap(bmpOut);
        }else {
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/noPic.png");
            icon.setImageBitmap(bmp);
        }
        content.setText(t.getTeacherIntro());


//         一个莫名其妙的BUG：书籍字段不能为null，但是可以为""，即空值
//         如果为null，则下面判空代码会报错，尚不清楚原因，可能是因为我太蠢了，没看出来
//         但是在看出来之前，就先这样

        if(t.getBook1().equals("") || t.getBook1() == null){
            b1.setVisibility(View.GONE);
        } else {
            b1.setText(t.getBook1());
        }
        if(t.getBook2().equals("")|| t.getBook2() == null){
            b2.setVisibility(View.GONE);
        }else {
            b2.setText(t.getBook2());
        }
        if(t.getBook3().equals("")|| t.getBook3() == null){
            b3.setVisibility(View.GONE);
        } else {
            b3.setText(t.getBook3());
        }
        if(t.getBook4().equals("")|| t.getBook4() == null){
            b4.setVisibility(View.GONE);
        } else {
            b4.setText(t.getBook4());
        }
        if(t.getBook5().equals("")|| t.getBook5() == null){
            b5.setVisibility(View.GONE);
        }else {
            b5.setText(t.getBook5());
        }
    }
}
