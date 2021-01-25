package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText nameText,pwdText,repeatPwdText;
    private RadioButton maleBtn;
    private DbUtil mHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_register);

        nameText = findViewById(R.id.Uid);
        pwdText = findViewById(R.id.Upwd);
        repeatPwdText= findViewById(R.id.Enpwd);
        maleBtn = findViewById(R.id.male);
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
        Cursor c = mDB.rawQuery("select * from userInfo where userName = ?", new String[]{nameText.getText().toString()});
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
            mDB.execSQL("insert into userInfo values(null,?,?,?,?,?,?,?,?)",
                    new String[]{nameText.getText().toString(), pwdText.getText().toString(), male});
            Toast.makeText(Register.this,"注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("regName",nameText.getText().toString());
            intent.putExtra("regPwd",pwdText.getText().toString());
            startActivity(intent);
        }
    }

}