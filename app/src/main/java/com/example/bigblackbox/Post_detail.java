package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.ReplyAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.entity.Reply;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post_detail extends AppCompatActivity {
    private EditText comment;
    private SQLiteDatabase mDB;
    private String currentUser;
    private String commUser;

    private int post_id = -1;

    ReplyAdapter replyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        TextView name = findViewById(R.id.postName);
        TextView time = findViewById(R.id.postTime);
        TextView title = findViewById(R.id.postTitle);
        TextView content = findViewById(R.id.postContent);
        comment = findViewById(R.id.edt_comment);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        post_id = getIntent().getIntExtra("postID",-1);
        Posting p = null;
            try(Cursor cursor = mDB.rawQuery("select * from posting where postID = ?",new String[]{String.valueOf(post_id)})){
                if(cursor.moveToNext()){
                    p = new Posting(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5));
                }
            }
        assert p != null;
        name.setText(p.getName());
        time.setText(p.getTime());
        title.setText(p.getTitle());
        content.setText(p.getContent());

        final List<Reply> r = new ArrayList<>();
            try(Cursor cursor = mDB.rawQuery("select * from postReply where reply_postID = ? order by replyTime desc",new String[]{String.valueOf(post_id)})){
                while (cursor.moveToNext()){
                    r.add(new Reply(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                }
            }

        final ListView listView = findViewById(R.id.replyList);
        replyAdapter = new ReplyAdapter(this,r);
        listView.setAdapter(replyAdapter);
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
                        if(c.moveToNext()){
                            currentUser = c.getString(1);
                        }
                        if(cu.moveToNext()){
                            commUser = cu.getString(2);
                        }
                        if(!currentUser.equals(UserInfo.userName)){        //判断所选帖子发帖用户与当前登录用户是否一致
                            if(!commUser.equals(UserInfo.userName)){       //判断当前评论用户与当前登录用户是否一致
                                Toast.makeText(Post_detail.this,"这是你的评论吗？",Toast.LENGTH_LONG).show();
                            }
                            else {
                                /*
                                此部分删除的是他人帖子下自己的评论
                                 */
                                mDB.execSQL("delete from postReply where replyID = ?", new String[]{String.valueOf(reply.getReplyID())});
                                Toast.makeText(Post_detail.this,"评论删除成功", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            /*
                            此部分删除的是自己发布的帖子下的任意评论
                             */
                            mDB.execSQL("delete from postReply where replyID = ?", new String[]{String.valueOf(reply.getReplyID())});
                            Toast.makeText(Post_detail.this,"评论删除成功", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
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