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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText nameText,pwdText,repeatPwdText;
    private RadioButton maleBtn;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_register);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        nameText = findViewById(R.id.Uid);
        pwdText = findViewById(R.id.Upwd);
        repeatPwdText= findViewById(R.id.enPwd);
        maleBtn = findViewById(R.id.male);
        Button clearBtn = findViewById(R.id.clear);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(nameText.getText().toString()) && "".equals(pwdText.getText().toString()) && "".equals(repeatPwdText.getText().toString())){
                    Toast.makeText(Register.this,"本来就没东西就没必要清空了吧^_^",Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("提示");
                    builder.setMessage("您确定要清空以上信息吗？");
                    builder.setPositiveButton("我手滑了0_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nameText.setText("");
                            pwdText.setText("");
                            repeatPwdText.setText("");
                            Toast.makeText(Register.this,"已清除",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }
    public void RegChk(View view){
        String checkResult = checkInfo();
        if(checkResult != null){         //验证信息未通过
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage(checkResult);    //输出具体未通过原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }else{
            String male = "男";
            if(!maleBtn.isChecked()){
                male = "女";
            }
            add(male);
        }
    }
    public String checkInfo(){
        String name = nameText.getText().toString();
        if("".equals(name)){
            return "用户名不能为空！";
        }
        if(name.length() < 3 || name.length() > 10){
            return "用户名长度为3-10位！";
        }
        String pwd = pwdText.getText().toString();
        if(pwd.length() < 6 ||pwd.length() > 15){
            return "密码长度应为6-15位之间!";
        }
        String repwd = repeatPwdText.getText().toString();
        if(!repwd.equals(pwd)){
            return "两次密码输入不一致！";
        }
        return null;
    }

    public void add(String male){
        int amount;
        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from userInfo where userName = ?", new String[]{nameText.getText().toString()});
        amount = c.getCount();         //如果amount不为0，说明该用户名已被使用
        if(amount != 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误提示！");
            builder.setMessage("该用户名已被使用！");
            builder.setPositiveButton("确定",null);
            builder.create().show();
            this.nameText.setText("");
        }
        else {           //amount为0，用户名未被使用
            mDB.execSQL("insert into userInfo values(null,?,?,?,?,?,?,?,?,?,?)",
                    new String[]{nameText.getText().toString(), pwdText.getText().toString(), male});
            Toast.makeText(Register.this,"注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("regName",nameText.getText().toString());
            intent.putExtra("regPwd",pwdText.getText().toString());
            startActivity(intent);
            this.finish();
        }
    }
}