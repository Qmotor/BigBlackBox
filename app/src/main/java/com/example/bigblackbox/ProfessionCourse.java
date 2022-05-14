package com.example.bigblackbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.ProfessionAdapter;
import com.example.bigblackbox.entity.Profession;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ProfessionCourse extends AppCompatActivity {
    private TextView choose;
    private SQLiteDatabase mDB;
    private ProfessionAdapter mProfessionAdapter;
    private final List<Profession> p = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_profession_course);

        final ImageView im = findViewById(R.id.addProCBtn);
        final ListView listView = findViewById(R.id.professionCList);
        choose = findViewById(R.id.chooseProC);

        mProfessionAdapter = new ProfessionAdapter(this, p);
        listView.setAdapter(mProfessionAdapter);

        if(!UserInfo.isAdmin.equals("1")){
            im.setVisibility(View.GONE);
        }

        // 列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Profession profession = p.get(position);
                    Intent intent = new Intent(ProfessionCourse.this, ProfessionDetail.class);
                    intent.putExtra("proID", profession.getProID());
                    startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(UserInfo.isAdmin.equals("1")){
                    final Profession profession = p.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfessionCourse.this);
                    builder.setTitle("提示");
                    builder.setMessage("您确定要删除该信息吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (UserInfo.isAdmin.equals("1")) {
                                mDB.execSQL("delete from profession where pro_id = ?",
                                        new String[]{String.valueOf(profession.getProID())});
                                Toast.makeText(ProfessionCourse.this, "删除成功", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(ProfessionCourse.this, ProfessionCourse.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ProfessionCourse.this, "权限不足!", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(ProfessionCourse.this, AddProfession.class);
                intent.putExtra("judgeID",5287);
                startActivity(intent);
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfessionCourse.this, ChooseTeacher.class);
                startActivity(intent);
            }
        });
    }

    private void showData(){
        p.clear();
        if(UserInfo.jug.equals("全部")) {
            try (Cursor cursor = mDB.rawQuery("select * from profession", new String[0])) {
                while (cursor.moveToNext()) {
                    p.add(new Profession(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getBlob(cursor.getColumnIndex("pro_fir_pic")),cursor.getString(5),cursor.getBlob(cursor.getColumnIndex("pro_sec_pic")),cursor.getString(7),cursor.getString(8),cursor.getBlob(cursor.getColumnIndex("pro_face"))));
                }
            }
        }else {
            try (Cursor cursor = mDB.rawQuery("select * from profession where pro_follow = ? ", new String[]{UserInfo.jug})) {
                while (cursor.moveToNext()) {
                    p.add(new Profession(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getBlob(cursor.getColumnIndex("pro_fir_pic")),cursor.getString(5),cursor.getBlob(cursor.getColumnIndex("pro_sec_pic")),cursor.getString(7),cursor.getString(8),cursor.getBlob(cursor.getColumnIndex("pro_face"))));
                }
            }
        }
        mProfessionAdapter.notifyDataSetChanged();
        if(p.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessionCourse.this);
            builder.setTitle("温馨提示");
            builder.setMessage("当前板块还没有内容噢，看看别的板块吧~");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ProfessionCourse.this, ChooseTeacher.class);
                    startActivity(intent);
                }
            });
            builder.create().show();
        }
        choose.setText(UserInfo.jug);
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
