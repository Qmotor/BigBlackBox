package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPush extends AppCompatActivity {
    private TextView pushNum;
    private EditText pushTitle, pushText;
    private SQLiteDatabase mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_push);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        pushTitle = findViewById(R.id.edit_push_title);
        pushText = findViewById(R.id.edit_push_text);
        pushNum = findViewById(R.id.pushTextNum);
        
        // 实时显示输入框字数
        pushText.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                pushNum.setText(s.length() + "/250");
                int start = pushText.getSelectionStart();
                int end = pushText.getSelectionEnd();
                if (wordNum.length() > pushText.length()) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(start - 1, end);
                    pushText.setText(s);
                    //设置光标在最后
                    pushText.setSelection(end);
                }
            }
        });
    }



    public void AddPushChk(View view){
        String checkResult = checkInfo();
        if(checkResult != null){                                                    //系统验证帖子信息不合法
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage(checkResult);                                        //输出具体原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }else{
            add();
        }
    }

    public String checkInfo(){
        String title = pushTitle.getText().toString().trim();
        String content = pushText.getText().toString().trim();
        if(UserInfo.userName == null){
            return "登录状态无效";
        }if("".equals(title)){
            return "标题不能为空！";
        }if(title.length() < 4){
            return "标题长度至少为4位！";
        }if(title.length() > 24){
            return "标题长度至多为24位！";
        }if("".equals(content)){
            return "内容不能为空！";
        }if(content.length() < 6){
            return "帖子内容长度至少包含6个字符";
        }if(content.length() > 250){
            return "帖子内容长度最多包含250个字符";
        }
        return null;
    }

    public void pushClean(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您确定要清空以上信息吗？");
        builder.setPositiveButton("我手滑了0_o", null);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pushTitle.setText("");
                pushText.setText("");
                Toast.makeText(AddPush.this,"已清除",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    public void add(){
        // 获取当前系统时间
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        mDB.execSQL("insert into pushPosting values(null,?,?,?,?)",
                new String[]{UserInfo.userName,pushTitle.getText().toString().trim(),pushText.getText().toString().trim(),simpleDateFormat.format(date)});
        Toast.makeText(this,"发帖成功", Toast.LENGTH_SHORT).show();
        this.finish();      //发帖成功后，关闭当前Activity
    }
}
