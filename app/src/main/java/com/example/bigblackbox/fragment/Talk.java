package com.example.bigblackbox.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.adpater.PostingAdpater;
import com.example.bigblackbox.entity.Posting;

import java.util.ArrayList;
import java.util.List;

public class Talk extends Fragment {
    SQLiteOpenHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DbUtil(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_talk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Posting> p = new ArrayList<>();
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from posting where postFollow = 1 order by postTime desc",new String[0])){
                while(cursor.moveToNext()){

                    p.add(new Posting(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5)));
                }
            }
        }
        ListView listView = view.findViewById(R.id.talkList);
        listView.setAdapter(new PostingAdpater(getContext(),p));
    }
}
