package com.example.bigblackbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.bigblackbox.adapter.PostingAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private EditText searchText;
    private DbUtil mHelper;


    public void searchPost() {
        final List<Posting> p = new ArrayList<>();
        try (SQLiteDatabase db = mHelper.getReadableDatabase()) {
            try (Cursor cursor = db.rawQuery("select * from posting where postTitle like ?", new String[]{"%" + searchText.getText().toString() + "%"})) {
                while (cursor.moveToNext()) {
                    p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
                }
            }
        }
        if (p.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("未搜索到相关信息，请检查关键字或帖子是否存在!");
            builder.setPositiveButton("确定", null);
            builder.create().show();
        } else {
            ListView listView = findViewById(R.id.searchList);
            listView.setAdapter(new PostingAdapter(this, p));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Posting posting = p.get(position);
                    Intent intent = new Intent(Search.this, Post_detail.class);
                    intent.putExtra("postID", posting.getPostID());
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new DbUtil(this);
        setContentView(R.layout.activity_search);
        searchText = findViewById(R.id.edit_search);
        // 软键盘监听器
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchPost();
                    return true;
                }
                return false;
            }
        });
    }
}