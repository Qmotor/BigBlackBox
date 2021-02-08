package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class FindPwd_detail extends AppCompatActivity {
    String[] item;
    String content;
    String userName;
    private EditText answer,np,enp;
    private SQLiteDatabase mDB;
    private static final long DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd_detail);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        answer = findViewById(R.id.answer);
        np = findViewById(R.id.newPwd);
        enp = findViewById(R.id.enNewPwd);
        Spinner question = findViewById(R.id.question);
        TextView showText = findViewById(R.id.show);
        Button clearBtn = findViewById(R.id.clear);

        /*
        设置状态栏为透明
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        userName = getIntent().getStringExtra("editPwdUser");
        showText.setText(userName+"，您正在找回密码");

        item = getResources().getStringArray(R.array.securityQuestion);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(answer.getText().toString()) && "".equals(np.getText().toString()) && "".equals(enp.getText().toString())){
                    Toast.makeText(FindPwd_detail.this,"本来就没东西就没必要清空了吧^_^",Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPwd_detail.this);
                    builder.setTitle("提示");
                    builder.setMessage("您确定要清空以上信息吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            answer.setText("");
                            np.setText("");
                            enp.setText("");
                            Toast.makeText(FindPwd_detail.this,"已清除",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.create().show();
                }
            }
        });

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

    public void editPwd(View view){
        int amount;
        String ans = answer.getText().toString();
        String nPwd = np.getText().toString();
        String enPwd = enp.getText().toString();

        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from userInfo where userName = ? and question = ? and answer = ?",
                new String[]{userName, content, answer.getText().toString()});
        amount = c.getCount();

        if(ans.equals("")){
            AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
            empBuilder.setTitle("错误提示！");
            empBuilder.setMessage("密保问题答案不能为空");
            empBuilder.setPositiveButton("确定",null);
            empBuilder.create().show();
        }
        else if(nPwd.equals("")){
            AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
            empBuilder.setTitle("错误提示！");
            empBuilder.setMessage("新密码不能为空");
            empBuilder.setPositiveButton("确定",null);
            empBuilder.create().show();
        }
        else if(nPwd.length() < 5 || nPwd.length() > 13){
            AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
            empBuilder.setTitle("错误提示！");
            empBuilder.setMessage("新密码长度应为6-12位");
            empBuilder.setPositiveButton("确定",null);
            empBuilder.create().show();
            np.setText("");
            enp.setText("");
        }
        else if(!nPwd.equals(enPwd)){
            AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
            empBuilder.setTitle("错误提示！");
            empBuilder.setMessage("两次输入的密码不一致，请重新输入");
            empBuilder.setPositiveButton("确定",null);
            empBuilder.create().show();
            np.setText("");
            enp.setText("");
        }
         else if(amount == 0){
            AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
            empBuilder.setTitle("错误提示！");
            empBuilder.setMessage("密保问题或答案有误，请检查输入");
            empBuilder.setPositiveButton("确定",null);
            empBuilder.create().show();
            answer.setText("");
        }
         else{
            mDB.execSQL("update userInfo set userPwd = ? where userName = ?",
                    new String[]{nPwd, userName});
            Toast.makeText(FindPwd_detail.this,"修改密码成功，正在返回登录界面", Toast.LENGTH_LONG).show();
            final Intent localIntent = new Intent(this,MainActivity.class);
            /*
            设置延时操作，时间间隔为2s
             */
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run(){
                    startActivity(localIntent);
                    finish();
                }
            };
            timer.schedule(task,DELAY);
        }
    }
}