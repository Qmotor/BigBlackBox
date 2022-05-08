package com.example.bigblackbox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bigblackbox.entity.Subject;
import com.example.bigblackbox.tool.DbUtil;

public class SubjectDetail extends AppCompatActivity {
    private int SID = -1;
    private SQLiteDatabase mDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        TextView sT = findViewById(R.id.sTit);
        TextView sfc = findViewById(R.id.sFirstC);
        TextView ssc = findViewById(R.id.sSecC);
        TextView stc = findViewById(R.id.sThirdC);
        ImageView viewing = findViewById(R.id.sIv);
        ImageView sfp = findViewById(R.id.sFirstP);
        ImageView ssp = findViewById(R.id.sSecP);

        SID = getIntent().getIntExtra("subjectID", -1);
        Subject s = null;
        try (Cursor cursor = mDB.rawQuery("select * from subject where sub_id = ?", new String[]{String.valueOf(SID)})) {
            if (cursor.moveToNext()) {
                s = new Subject(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(cursor.getColumnIndex("sub_fir_pic")), cursor.getString(5), cursor.getBlob(cursor.getColumnIndex("sub_sec_pic")), cursor.getString(7), cursor.getInt(8), cursor.getInt(9));
            }

            assert s != null;
            sT.setText(s.getSubTitle());
            sfc.setText(s.getFirCont());
            ssc.setText(s.getSecCont());
            stc.setText(s.getThrCont());
            // byte[] 转 bitmap
            Bitmap bmpOut;
            bmpOut = BitmapFactory.decodeByteArray(s.getFirPic(), 0, s.getFirPic().length);
            sfp.setImageBitmap(bmpOut);
            bmpOut = BitmapFactory.decodeByteArray(s.getSecPic(), 0, s.getSecPic().length);
            ssp.setImageBitmap(bmpOut);
            switch (s.getFollChoose()){
                case 1:
                    viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.math1));
                    break;
                case 2:
                    viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.math2));
                    break;
                case 3:
                    viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.math3));
                    break;
            }
        }
    }
}
