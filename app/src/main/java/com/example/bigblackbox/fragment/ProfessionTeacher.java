package com.example.bigblackbox.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.AddTeacher;
import com.example.bigblackbox.ChooseTeacher;
import com.example.bigblackbox.MainActivity;
import com.example.bigblackbox.ProfessionCourse;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.Teacher_detail;
import com.example.bigblackbox.tool.UserInfo;
import com.example.bigblackbox.adapter.TeacherAdapter;
import com.example.bigblackbox.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class ProfessionTeacher extends Fragment {
    private SQLiteDatabase mDB;
    private TextView choose;
    private TeacherAdapter mTeacherAdapter;
    private final List<Teacher> t = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(getContext());
        mDB = mHelper.getReadableDatabase();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profession_teacher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageView im = view.findViewById(R.id.addProTeacherBtn);
        final ListView listView = view.findViewById(R.id.professionList);
        choose = view.findViewById(R.id.choosePro);
        setTextViewStyles(choose);
        mTeacherAdapter = new TeacherAdapter(requireContext(), t);
        listView.setAdapter(mTeacherAdapter);


        if(!UserInfo.isAdmin.equals("1")){
            im.setVisibility(View.GONE);
        }

        // 列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Teacher teacher = t.get(position);
                Intent intent = new Intent(getContext(), Teacher_detail.class);
                intent.putExtra("teacherID", teacher.getTeacherID());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(UserInfo.isAdmin.equals("1")){
                    final Teacher teacher = t.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("提示");
                    builder.setMessage("您确定要删除该老师信息吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (UserInfo.isAdmin.equals("1")) {
                                mDB.execSQL("delete from teacher where teacher_id = ?",
                                        new String[]{String.valueOf(teacher.getTeacherID())});
                                Toast.makeText(requireContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                requireActivity().onBackPressed();
                                Intent intent = new Intent(getActivity(), com.example.bigblackbox.activity.Teacher.class);
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
                Intent intent = new Intent(getContext(), AddTeacher.class);
                intent.putExtra("judgeID",5287);
                startActivity(intent);
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChooseTeacher.class);
                startActivity(intent);
            }
        });
    }

    private void showData(){
        t.clear();
        if(UserInfo.jug.equals("全部")) {
            try (Cursor cursor = mDB.rawQuery("select * from teacher where teacher_follow = '3' ", new String[0])) {
                while (cursor.moveToNext()) {
                    t.add(new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getBlob(cursor.getColumnIndex("teacher_icon")), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10)));
                }
            }
        }else {
            try (Cursor cursor = mDB.rawQuery("select * from teacher where teacher_follow = '3' and pro_follow = ? ", new String[]{UserInfo.jug})) {
                while (cursor.moveToNext()) {
                    t.add(new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getBlob(cursor.getColumnIndex("teacher_icon")), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10)));
                }
            }
        }
        mTeacherAdapter.notifyDataSetChanged();
        if(t.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("温馨提示");
            builder.setMessage("当前板块还没有内容噢，看看别的板块吧~");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getContext(), ChooseTeacher.class);
                    startActivity(intent);
                }
            });
            builder.create().show();
        }
        choose.setText(UserInfo.jug);
    }

    private void setTextViewStyles(TextView textView) {
        LinearGradient mLinearGradient = new LinearGradient(0, 0, textView.getPaint().getTextSize()* textView.getText().length(), 0, Color.parseColor("#FFFF68FF"), Color.parseColor("#FFFED732"), Shader.TileMode.CLAMP);
        textView.getPaint().setShader(mLinearGradient);
        textView.invalidate();
    }

    @Override
    public void onResume() {
        // 重写onResume()方法，保持显示数据常新
        super.onResume();
        showData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserInfo.jug = "全部";
    }
}
