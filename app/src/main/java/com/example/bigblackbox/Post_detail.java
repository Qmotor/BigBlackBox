package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bigblackbox.adapter.ReplyAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.entity.Reply;
import com.example.bigblackbox.entity.User;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post_detail extends AppCompatActivity {
    private EditText comment;
    private SQLiteDatabase mDB;
    private String currentUser;
    private String commUser;
    private int lord;
    final List<Reply> r = new ArrayList<>();
    private int post_id = -1;

    ReplyAdapter replyAdapter;

    @SuppressLint("SetTextI18n")
    public void setTime(String postTime) {
        try {
            TextView time = findViewById(R.id.postTime);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pDate = ft.parse(postTime);
            Date nDate = new Date(System.currentTimeMillis());
            assert pDate != null;
            long diff = nDate.getTime() - pDate.getTime();// 这样得到的差值是毫秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            if (days == 0 && hours == 0 && minutes == 0) {
                time.setText((diff / 1000) + "秒前");
            } else if (days == 0 && hours == 0) {
                time.setText(minutes + "分钟前");
            } else if (days == 0) {
                time.setText(hours + "小时前");
            } else if (days <= 5) {
                time.setText(days + "天前");
            } else {
                time.setText(postTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ImageView pi = findViewById(R.id.postIcon);
        comment = findViewById(R.id.edt_comment);
        TextView name = findViewById(R.id.postName);
        TextView title = findViewById(R.id.postTitle);
        TextView content = findViewById(R.id.postContent);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        post_id = getIntent().getIntExtra("postID", -1);
        Posting p = null;
        try (Cursor cursor = mDB.rawQuery("select * from posting where postID = ?", new String[]{String.valueOf(post_id)})) {
            if (cursor.moveToNext()) {
                p = new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
                UserInfo.isLord = cursor.getString(1);
                lord = cursor.getInt(6);
                setTime(cursor.getString(4));
            }
        }
        assert p != null;
        if(lord == 1){
            name.setTextColor(Color.parseColor("#FF0000"));
        }
        name.setText(p.getName());
        title.setText(p.getTitle());
        content.setText(p.getContent());

        // 头像设置
        User u = null;
        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from userInfo where userName = ?", new String[]{String.valueOf(UserInfo.isLord)});
        if (cursor.moveToNext()) {
            u = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getBlob(cursor.getColumnIndex("icon")));
        }
        assert u != null;
        if(cursor.getBlob(cursor.getColumnIndex("icon")) == null){
            pi.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pig));
        }else {
            Bitmap bmpOut= BitmapFactory.decodeByteArray(u.getUserIcon(),0,u.getUserIcon().length);
            pi.setImageBitmap(bmpOut);
        }

        final ListView listView = findViewById(R.id.replyList);
        replyAdapter = new ReplyAdapter(this, r);
        listView.setAdapter(replyAdapter);
        showReply();

        // 长按事件——删除评论
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Reply reply = r.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(Post_detail.this);
                builder.setTitle("提示");
                builder.setMessage("您确定要删除该评论吗？");
                builder.setPositiveButton("我手滑了0_o", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from posting where postID = ?",
                                new String[]{String.valueOf(reply.getPostID())});
                        @SuppressLint("Recycle") Cursor cu = mDB.rawQuery("select * from postReply where replyID = ?",
                                new String[]{String.valueOf(reply.getReplyID())});
                        if (c.moveToNext()) {
                            currentUser = c.getString(1);
                        }
                        if (cu.moveToNext()) {
                            commUser = cu.getString(2);
                        }
                        if (currentUser.equals(UserInfo.userName) || (commUser.equals(UserInfo.userName)) || (UserInfo.isAdmin.equals("1"))) {
                            /*
                               判断当前评论用户与当前登录用户是否一致
                               判断所选帖子的发帖用户与当前登录用户是否一致
                               判断当前用户是否为管理员
                             */
                            mDB.execSQL("delete from postReply where replyID = ?", new String[]{String.valueOf(reply.getReplyID())});
                            // 实时刷新帖子数据
                            finish();
                            Intent intent = new Intent(Post_detail.this, Post_detail.class);
                            intent.putExtra("postID", post_id);
                            startActivity(intent);
                            Toast.makeText(Post_detail.this, "评论删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Post_detail.this, "这是你的评论吗？", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    public void subComment(View view) {
        String checkResult = checkInfo();
        if (checkResult != null) {         //验证信息未通过
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!!!");
            builder.setMessage(checkResult);    //输出具体未通过原因
            builder.setPositiveButton("确定", null);
            builder.create().show();
        } else {
            add();
        }
    }

    public String checkInfo() {
        String title = comment.getText().toString().trim();
        if (UserInfo.userName == null) {
            return "登录状态无效";
        }if ("".equals(title)) {
            return "评论内容不能为空！";
        }
        return null;
    }

    public void add() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        if (post_id != -1) {
            mDB.execSQL("insert into postReply values(null,?,?,?,?,?)",
                    new String[]{String.valueOf(post_id), UserInfo.userName, comment.getText().toString().trim(), simpleDateFormat.format(date), UserInfo.isAdmin});
            Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show();
            this.finish();
            Intent intent = new Intent(this, Post_detail.class);
            intent.putExtra("postID", post_id);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!!!");
            builder.setMessage("评论失败");    //输出具体未通过原因
            builder.setPositiveButton("确定", null);
            builder.create().show();
        }
    }

    public void showReply() {
        r.clear();
        try (Cursor cursor = mDB.rawQuery("select * from postReply where reply_postID = ? order by replyTime desc", new String[]{String.valueOf(post_id)})) {
            while (cursor.moveToNext()) {
                r.add(new Reply(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5)));
            }
        }
    }
}