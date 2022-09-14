package com.example.bigblackbox.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.MusicTest;
import com.example.bigblackbox.R;
import com.example.bigblackbox.adapter.MusicAdapter;
import com.example.bigblackbox.entity.Music;
import com.example.bigblackbox.tool.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class ProfessionData extends Fragment {
    private SQLiteDatabase mDB;
    private MusicAdapter mMusicAdapter;
    private ListView listView;
    private ImageView im;
    private List<Music> m = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(getContext());
        mDB = mHelper.getReadableDatabase();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pro_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        im = view.findViewById(R.id.nodata);
        listView = view.findViewById(R.id.proDataList);

        im.setVisibility(View.GONE);
        mMusicAdapter = new MusicAdapter(requireContext(), m);
        listView.setAdapter(mMusicAdapter);

        if(m.size() == 0){
            listView.setVisibility(View.GONE);
            im.setVisibility(View.VISIBLE);
        }

        // 列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = m.get(position);
                Intent intent = new Intent(getContext(), MusicTest.class);
                intent.putExtra("musicID", music.getMusicID());
                startActivity(intent);
            }
        });
    }

    private void showData(){
        m.clear();
        try(Cursor cursor = mDB.rawQuery("select * from music where music_follow = '3' ",new String[0])){
            while (cursor.moveToNext()) {
                m.add(new Music(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4)));
            }
        }
        mMusicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        // 重写onResume()方法，保持显示数据常新
        super.onResume();
        showData();
        if(m.size() == 0){
            listView.setVisibility(View.GONE);
            im.setVisibility(View.VISIBLE);
        }
    }
}
