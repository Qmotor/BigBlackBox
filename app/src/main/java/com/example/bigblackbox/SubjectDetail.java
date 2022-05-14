package com.example.bigblackbox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
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
            if(!s.getSecCont().equals("")){
                ssc.setText(s.getSecCont());
            }else {
                ssc.setVisibility(View.GONE);
            }
            if(!s.getThrCont().equals("")){
                stc.setText(s.getThrCont());
            }else {
                stc.setVisibility(View.GONE);
            }

            // byte[] 转 bitmap
            Bitmap bmpOut;
            if(s.getFirPic() != null) {
                bmpOut = BitmapFactory.decodeByteArray(s.getFirPic(), 0, s.getFirPic().length);
                sfp.setImageBitmap(bmpOut);
            }else {
                sfp.setVisibility(View.GONE);
            }
            if(s.getSecPic() != null) {
                bmpOut = BitmapFactory.decodeByteArray(s.getSecPic(), 0, s.getSecPic().length);
                ssp.setImageBitmap(bmpOut);
            }else {
                ssp.setVisibility(View.GONE);
            }

            if(s.getSubFollow() == 1){ // 数学
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
            }else if(s.getSubFollow() == 2){  // 英语
                switch (s.getFollChoose()){
                    case 1:
                        viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eng1));
                        break;
                    case 2:
                        viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eng2));
                        break;
                    case 3:
                        viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eng3));
                        break;
                }
            }else {
                switch (s.getFollChoose()){  // 政治
                    case 1:
                        viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.polity1));
                        break;
                    case 2:
                        viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.polity2));
                        break;
                    case 3:
                        viewing.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.polity3));
                        break;
                }
            }
        }
    }
}
