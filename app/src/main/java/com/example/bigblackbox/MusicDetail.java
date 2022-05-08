package com.example.bigblackbox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.entity.Music;
import com.example.bigblackbox.tool.DbUtil;

public class MusicDetail extends AppCompatActivity {
    private int mID = -1;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        setContentView(R.layout.activity_music_detail);
        TextView mName = findViewById(R.id.music_title_tv);
        TextView singer = findViewById(R.id.music_artist_tv);
        mID = getIntent().getIntExtra("musicID", -1);
        Music m = null;
        try (Cursor cursor = mDB.rawQuery("select * from music where music_id = ?", new String[]{String.valueOf(mID)})) {
            if (cursor.moveToNext()) {
                m = new Music(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));            }
        }
        assert m != null;
        mName.setText(m.getMusicName());
        singer.setText(m.getMusicAuthor());
    }

}
