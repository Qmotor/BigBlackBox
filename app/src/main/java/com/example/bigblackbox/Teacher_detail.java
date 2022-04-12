package com.example.bigblackbox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.TeacherAdapter;
import com.example.bigblackbox.entity.Teacher;

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
        Bitmap bmpOut= BitmapFactory.decodeByteArray(t.getTeacherIcon(),0,t.getTeacherIcon().length);
        icon.setImageBitmap(bmpOut);
        content.setText(t.getTeacherIntro());
        b1.setText(t.getBook1());
        b2.setText(t.getBook2());
        b3.setText(t.getBook3());
        b4.setText(t.getBook4());
        b5.setText(t.getBook5());
    }
}
