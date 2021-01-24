package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Add_post extends AppCompatActivity {
    private EditText titleText,contentText;
    private RadioButton talkBtn,sellBtn,lectureBtn;
    private DbUtil mHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        titleText = findViewById(R.id.edt_title);
        contentText= findViewById(R.id.edt_text);
        talkBtn = findViewById(R.id.talk);
        lectureBtn = findViewById(R.id.lecture);
        sellBtn = findViewById(R.id.info);

    }



    public void RegChk(View view){
        String checkResult = checkInfo();
        if(checkResult != null){         //验证信息未通过
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!!!");
            builder.setMessage(checkResult);    //输出具体未通过原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }else{
            int follow = 3 ;
           if(talkBtn.isChecked()){
               follow = 1;
           }
           else if(lectureBtn.isChecked()){
               follow = 2;
           }
            add(follow);
        }
    }

    public String checkInfo(){
        if(UserInfo.userName == null){
            return "登录状态无效";
        }
        String title= titleText.getText().toString();
        if("".equals(title)){
            return "标题不能为空！";
        }
        if(title.length() < 4){
            return "标题长度至少为4位！";
        }
        String content = contentText.getText().toString();
        if("".equals(content)){
            return "内容不能为空！";
        }
        if(content.length() < 6){
            return "帖子内容长度至少为6位";
        }
        return null;
    }

    public void add(int follow){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
            mDB.execSQL("insert into posting values(null,?,?,?,?,?)",
                    new String[]{UserInfo.userName,titleText.getText().toString(),contentText.getText().toString(),simpleDateFormat.format(date),String.valueOf(follow)});
            Toast.makeText(this,"发帖成功", Toast.LENGTH_SHORT).show();
            this.finish();
        }
}