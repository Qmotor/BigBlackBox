package com.example.bigblackbox;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bigblackbox.adpater.ReplyAdpater;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.entity.Reply;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post_detail extends AppCompatActivity {
    private TextView name,time,title,content;
    private EditText comment;
    private DbUtil mHelper;
    private SQLiteDatabase mDB;
    SQLiteOpenHelper helper;

    private int post_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        name = findViewById(R.id.postName);
        time = findViewById(R.id.postTime);
        title = findViewById(R.id.postTitle);
        content = findViewById(R.id.postContent);
        comment = findViewById(R.id.edt_comment);

        mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        helper = new DbUtil(this);

        post_id = getIntent().getIntExtra("postID",-1);
        Posting p = null;
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from posting where postID = ?",new String[]{String.valueOf(post_id)})){
                if(cursor.moveToNext()){
                    p = new Posting(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5));
                }
            }
        }
        name.setText(p.getName());
        time.setText(p.getTime());
        title.setText(p.getTitle());
        content.setText(p.getContent());

        final List<Reply> r = new ArrayList<>();
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from postReply where reply_postID = ?",new String[]{String.valueOf(post_id)})){
                while (cursor.moveToNext()){
                    r.add(new Reply(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                }
            }
        }
        final ListView listView = findViewById(R.id.replyList);
        listView.setAdapter(new ReplyAdpater(this,r));
    }

    public void subComment(View view){
        String checkResult = checkInfo();
        if(checkResult != null){         //验证信息未通过
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!!!");
            builder.setMessage(checkResult);    //输出具体未通过原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }else{
            add();
        }

    }

    public String checkInfo(){
        if(UserInfo.userName == null){
            return "登录状态无效";
        }
        String title= comment.getText().toString();
        if("".equals(title)){
            return "评论内容不能为空！";
        }
        return null;
    }

    public void add(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        if(post_id != -1) {
            mDB.execSQL("insert into postReply values(null,?,?,?,?)",
                    new String[]{String.valueOf(post_id), UserInfo.userName, comment.getText().toString(), simpleDateFormat.format(date)});
            Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show();
            this.comment.setText("");
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!!!");
            builder.setMessage("评论失败");    //输出具体未通过原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }
    }


}