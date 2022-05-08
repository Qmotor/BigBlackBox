package com.example.bigblackbox.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.R;
import com.example.bigblackbox.SubjectDetail;
import com.example.bigblackbox.adapter.SubjectAdapter;
import com.example.bigblackbox.entity.Subject;
import com.example.bigblackbox.tool.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class Math extends Fragment {
    private SQLiteDatabase mDB;
    private SubjectAdapter mSubjectAdapter;
    private List<Subject> s = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(getContext());
        mDB = mHelper.getReadableDatabase();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_math, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = view.findViewById(R.id.mathList);

        mSubjectAdapter = new SubjectAdapter(requireContext(), s);
        listView.setAdapter(mSubjectAdapter);

        // 列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject subject = s.get(position);
                Intent intent = new Intent(getContext(), SubjectDetail.class);
                intent.putExtra("subjectID", subject.getSubID());
                startActivity(intent);
            }
        });
    }

    private void showData(){
        s.clear();
        try(Cursor cursor = mDB.rawQuery("select * from subject where sub_follow = '0' ",new String[0])){
            while (cursor.moveToNext()) {
                s.add(new Subject(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getBlob(cursor.getColumnIndex("sub_fir_pic")),cursor.getString(5),cursor.getBlob(cursor.getColumnIndex("sub_sec_pic")),cursor.getString(7),cursor.getInt(8),cursor.getInt(9)));
            }
        }
        mSubjectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        // 重写onResume()方法，保持显示数据常新
        super.onResume();
        showData();
    }
}
