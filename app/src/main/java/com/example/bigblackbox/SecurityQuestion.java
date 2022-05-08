package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;

import java.util.Timer;
import java.util.TimerTask;

public class SecurityQuestion extends AppCompatActivity {
    String[] item;
    String content;
    private EditText answer,enAnswer;
    private SQLiteDatabase mDB;
    private static final long DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_security_question);

        answer = findViewById(R.id.answer1);
        Spinner question = findViewById(R.id.question);
        enAnswer = findViewById(R.id.enAnswer1);
        Button clearBtn = findViewById(R.id.clear);


        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(answer.getText().toString().trim()) && "".equals(enAnswer.getText().toString().trim())){
                    Toast.makeText(SecurityQuestion.this,"本来就没东西就没必要清空了吧^_^",Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecurityQuestion.this);
                    builder.setTitle("提示");
                    builder.setMessage("您确定要清空以上信息吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            answer.setText("");
                            enAnswer.setText("");
                            Toast.makeText(SecurityQuestion.this,"已清除",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();
                }
            }
        });

        /*
        设置状态栏为透明
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        item = getResources().getStringArray(R.array.securityQuestion);

        question.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                content = item[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void confirmQuestion(View view){
        int amount;
        String an = answer.getText().toString().trim();
        String enAn = enAnswer.getText().toString().trim();
        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select question from userInfo where user_name = ? and question != '' ",
                new String[]{UserInfo.userName});
        amount = c.getCount();
         if(an.trim().equals("") || enAn.trim().equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(SecurityQuestion.this);
            builder.setTitle("提示");
            builder.setMessage("密保问题答案不能为空");
            builder.setPositiveButton("明白了", null);
            builder.create().show();
        }
        else if(!an.equals(enAn)){
            AlertDialog.Builder builder = new AlertDialog.Builder(SecurityQuestion.this);
            builder.setTitle("提示");
            builder.setMessage("两次输入的密保问题答案不一致");
            builder.setPositiveButton("明白了", null);
            builder.create().show();
        }
         else if(amount != 0){
             AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
             empBuilder.setTitle("温馨提示");
             empBuilder.setMessage("正在修改当前账号原有密保问题，是否继续");
             empBuilder.setPositiveButton("取消",null);
             empBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     mDB.execSQL("update userInfo set question = ?, answer = ? where user_name = ?",
                             new String[]{content,answer.getText().toString().trim(),UserInfo.userName});
                     Toast.makeText(SecurityQuestion.this,"修改密保问题成功，即将返回", Toast.LENGTH_SHORT).show();
                     Timer timer = new Timer();
                     TimerTask task = new TimerTask() {
                         @Override
                         public void run(){
                             finish();
                         }
                     };
                     timer.schedule(task,DELAY);
                 }
             });
             empBuilder.create().show();
         }
        else{
             AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
             empBuilder.setTitle("温馨提示");
             empBuilder.setMessage("即将保存，请确认输入的信息是否无误");
             empBuilder.setPositiveButton("我再看看",null);
             empBuilder.setNegativeButton("我已检查完毕", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     mDB.execSQL("update userInfo set question = ?, answer = ? where user_name = ?",
                             new String[]{content,answer.getText().toString().trim(),UserInfo.userName});
                     Toast.makeText(SecurityQuestion.this,"密保问题保存成功，正在返回", Toast.LENGTH_SHORT).show();
                     Timer timer = new Timer();
                     TimerTask task = new TimerTask() {
                         @Override
                         public void run(){
                             finish();
                         }
                     };
                     timer.schedule(task,DELAY);
                 }
             });
             empBuilder.create().show();
        }
    }
}