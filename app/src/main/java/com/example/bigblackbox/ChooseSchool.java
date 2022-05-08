package com.example.bigblackbox;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.SchoolAdapter;

import com.example.bigblackbox.entity.School;
import com.example.bigblackbox.tool.CharacterUtils;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.NavView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChooseSchool extends AppCompatActivity {
    private SQLiteDatabase mDB;
    private EditText et;
    private ListView lv;
    private NavView nv;

    private SchoolAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_school);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        et = findViewById(R.id.search_school);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        initView();
        initData();
    }

    private void search(){
        String schoolName = et.getText().toString().trim();
        int position = adapter.getSearchPosition(schoolName);
        if (position != -1) {
            lv.setSelection(position);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("未搜索到相关院校，请检查关键字是否为院校全称");
            builder.setNegativeButton("确定", null);
            builder.create().show();
        }
    }

    private void initView() {
        TextView tv = (TextView) findViewById(R.id.tv);
        lv = (ListView) findViewById(R.id.lv);
        nv = (NavView) findViewById(R.id.nv);
        nv.setTextView(tv);
    }

    private void initData() {
        //初始化数据
        final List<School> list = new ArrayList<>();
            try (Cursor cursor = mDB.rawQuery("select * from school", new String[0])) {
                while (cursor.moveToNext()) {
                    list.add(new School(cursor.getInt(0),cursor.getString(1), cursor.getString(2), CharacterUtils.getFirstSpell(cursor.getString(1)).toUpperCase()));
                }
            }

        //将拼音排序
        Collections.sort(list, new Comparator<School>() {
            @Override
            public int compare(School lhs, School rhs) {
                return lhs.getFirstCharacter().compareTo(rhs.getFirstCharacter());
            }
        });
        //填充ListView
        adapter = new SchoolAdapter(this, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                School school = list.get(position);
                Uri uri = Uri.parse(school.getSchoolUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        //ListView连动接口
        nv.setListener(new NavView.onTouchCharacterListener() {
            @Override
            public void touchCharacterListener(String s) {
                int position = adapter.getSelectPosition(s);
                if (position != -1) {
                    lv.setSelection(position);
                }
            }
        });
    }
}
