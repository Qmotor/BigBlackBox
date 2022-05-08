package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPost extends AppCompatActivity {
    private EditText titleText,contentText;
    private RadioButton talkBtn,lectureBtn;
    private TextView numShow;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        titleText = findViewById(R.id.edit_title);
        contentText= findViewById(R.id.edit_text);
        talkBtn = findViewById(R.id.talk);
        lectureBtn = findViewById(R.id.lecture);
        numShow = findViewById(R.id.textNum);

        // 实时显示输入框字数
        contentText.addTextChangedListener(new TextWatcher() {
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
                numShow.setText(s.length() + "/300");
                int start = contentText.getSelectionStart();
                int end = contentText.getSelectionEnd();
                if (wordNum.length() > contentText.length()) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(start - 1, end);
                    contentText.setText(s);
                    //设置光标在最后
                    contentText.setSelection(end);
                }
            }
        });

    }

    public void RegChk(View view){
        String checkResult = checkInfo();
        if(checkResult != null){                                                    //系统验证帖子信息不合法
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage(checkResult);                                        //输出具体原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }else{
            int follow = 3;                    //默认follow初始值为3
           if(talkBtn.isChecked()){
               follow = 1;
           }
           else if(lectureBtn.isChecked()){
               follow = 2;
           }
            add(follow);                       //将follow参数传入add方法
        }
    }

    public String checkInfo(){
        String title = titleText.getText().toString().trim();
        String content = contentText.getText().toString().trim();
        if(UserInfo.userName == null){
            return "登录状态无效";
        }if("".equals(title)){
            return "标题不能为空！";
        }if(title.length() < 4){
            return "标题长度至少为4位！";
        }if("".equals(content)){
            return "内容不能为空！";
        }if(content.length() < 6){
            return "帖子内容长度至少包含6个字符";
        }if(content.length() > 250){
            return "帖子内容长度最多包含200个字符";
        }
        return null;
    }

    public void postClean(View view){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("您确定要清空以上信息吗？");
            builder.setPositiveButton("我手滑了0_o", null);
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    titleText.setText("");
                    contentText.setText("");
                    Toast.makeText(AddPost.this,"已清除",Toast.LENGTH_SHORT).show();
                }
            });
            builder.create().show();
    }


    public void add(int follow){
        // 获取当前系统时间
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        if(UserInfo.isAdmin.equals("1")){
            mDB.execSQL("insert into posting values(null,?,?,?,?,?,?)",
                    new String[]{UserInfo.userName,titleText.getText().toString().trim(),contentText.getText().toString().trim(),simpleDateFormat.format(date),String.valueOf(follow),"1"});
        }else {
            mDB.execSQL("insert into posting values(null,?,?,?,?,?,?)",
                    new String[]{UserInfo.userName, titleText.getText().toString().trim(), contentText.getText().toString().trim(), simpleDateFormat.format(date), String.valueOf(follow),"0"});
        }
        UserInfo.flag = false;
        Toast.makeText(this,"发帖成功", Toast.LENGTH_SHORT).show();
        this.finish();      //发帖成功后，关闭当前Activity
    }
}