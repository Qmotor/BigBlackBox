package com.example.bigblackbox.fragment;

import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.AddPost;
import com.example.bigblackbox.AddSubject;
import com.example.bigblackbox.AddTeacher;
import com.example.bigblackbox.R;
import com.example.bigblackbox.SubjectDetail;
import com.example.bigblackbox.adapter.SubjectAdapter;
import com.example.bigblackbox.entity.Subject;
import com.example.bigblackbox.entity.Teacher;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;

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
        final ImageView im = view.findViewById(R.id.addSubBtn);
        final ListView listView = view.findViewById(R.id.mathList);

        mSubjectAdapter = new SubjectAdapter(requireContext(), s);
        listView.setAdapter(mSubjectAdapter);

        if(!UserInfo.isAdmin.equals("1")){
            im.setVisibility(View.GONE);
        }

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(UserInfo.isAdmin.equals("1")){
                    final Subject subject = s.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("提示");
                    builder.setMessage("您确定要删除该信息吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (UserInfo.isAdmin.equals("1")) {
                                mDB.execSQL("delete from subject where sub_id = ?",
                                        new String[]{String.valueOf(subject.getSubID())});
                                Toast.makeText(requireContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                requireActivity().onBackPressed();
                                Intent intent = new Intent(getActivity(), com.example.bigblackbox.activity.GenSubject.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "权限不足!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.create().show();
                }
                return true;
            }
        });

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSubject.class);
                startActivity(intent);
            }
        });

    }

    private void showData(){
        s.clear();
        try(Cursor cursor = mDB.rawQuery("select * from subject where sub_follow = '1' order by sub_id desc",new String[0])){
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
