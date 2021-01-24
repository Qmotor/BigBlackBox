package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.bigblackbox.activity.IndexActivity;

public class login extends AppCompatActivity {
    private EditText LnameText,LpwdText;
    private DbUtil mHelper;
    private SQLiteDatabase mDB;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_login);

        LnameText = findViewById(R.id.LUid);
        LpwdText = findViewById(R.id.LUpwd);

        String userName = getIntent().getStringExtra("regName");
        String userPwd = getIntent().getStringExtra("regPwd");

        LnameText.setText("五二八七");
        LpwdText.setText("123456");

        if(userName != null){
            LnameText.setText(userName);
            LpwdText.setText(userPwd);
        }

    }

    public void LoginApp(View view) {
        int amount = 0;
        Cursor c = mDB.rawQuery("select userID from userInfo where userName = ? and userPwd = ?",
                new String[]{LnameText.getText().toString(),LpwdText.getText().toString()});
        amount = c.getCount();
        while (c.moveToNext()) {
             id = c.getInt(0);
        }

        if(amount == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误提示！");
            builder.setMessage("用户名或密码输入错误，请重新输入");
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }
        else {
                Intent intent = new Intent(this, IndexActivity.class);
                UserInfo.userName = LnameText.getText().toString();
                UserInfo.userID = String.valueOf(id);
                startActivity(intent);
        }
    }
}