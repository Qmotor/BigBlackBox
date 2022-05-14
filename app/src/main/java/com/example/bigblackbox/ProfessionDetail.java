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

import com.example.bigblackbox.entity.Profession;
import com.example.bigblackbox.tool.DbUtil;

public class ProfessionDetail extends AppCompatActivity {
    private int PID = -1;
    private SQLiteDatabase mDB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession_detail);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        TextView pT = findViewById(R.id.pTit);
        TextView pfc = findViewById(R.id.pFirstC);
        TextView psc = findViewById(R.id.pSecC);
        TextView stc = findViewById(R.id.pThirdC);
        ImageView viewing = findViewById(R.id.pIv);
        ImageView pfp = findViewById(R.id.pFirstP);
        ImageView psp = findViewById(R.id.pSecP);

        PID = getIntent().getIntExtra("proID", -1);
        Profession p = null;
        try (Cursor cursor = mDB.rawQuery("select * from profession where pro_id = ?", new String[]{String.valueOf(PID)})) {
            if (cursor.moveToNext()) {
                p = new Profession(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getBlob(cursor.getColumnIndex("pro_fir_pic")),cursor.getString(5),cursor.getBlob(cursor.getColumnIndex("pro_sec_pic")),cursor.getString(7),cursor.getString(8),cursor.getBlob(cursor.getColumnIndex("pro_face")));
            }

            assert p != null;
            pT.setText(p.getProTitle());
            pfc.setText(p.getProFirCont());

            if(!p.getProSecCont().equals("")){
                psc.setText(p.getProSecCont());
            }else {
                psc.setVisibility(View.GONE);
            }

            if(!p.getProThrCont().equals("")){
                stc.setText(p.getProThrCont());
            }else {
                stc.setVisibility(View.GONE);
            }

            // byte[] 转 bitmap
            Bitmap bmpOut;
            if(p.getProFirPic() != null) {
                bmpOut = BitmapFactory.decodeByteArray(p.getProFirPic(), 0, p.getProFirPic().length);
                pfp.setImageBitmap(bmpOut);
            }else {
                pfp.setVisibility(View.GONE);
            }
            if(p.getProSecPic() != null) {
                bmpOut = BitmapFactory.decodeByteArray(p.getProSecPic(), 0, p.getProSecPic().length);
                psp.setImageBitmap(bmpOut);
            }else {
                psp.setVisibility(View.GONE);
            }
            if(p.getProFace() != null){
                bmpOut = BitmapFactory.decodeByteArray(p.getProFace(), 0, p.getProFace().length);
                viewing.setImageBitmap(bmpOut);
            }else {
                @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/noPic.png");
                viewing.setImageBitmap(bmp);
            }
        }
    }
}
