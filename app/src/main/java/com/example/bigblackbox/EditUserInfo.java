package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigblackbox.entity.User;

import java.util.Timer;
import java.util.TimerTask;

public class EditUserInfo extends AppCompatActivity {
    private SQLiteDatabase mDB;
    private EditText phone,email,edu,school,career;
    private RadioButton mGender, fGender;
    private static final long DELAY = 2000;            //设置延迟参数，默认值为2s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        TextView uid = findViewById(R.id.uid);
        TextView uName = findViewById(R.id.uname);
        mGender = findViewById(R.id.uMale);
        fGender = findViewById(R.id.uFemale);
        phone = findViewById(R.id.uPhone);
        email = findViewById(R.id.uEmail);
        edu = findViewById(R.id.uEdu);
        school = findViewById(R.id.uSchool);
        career = findViewById(R.id.uCareer);

        User u = null;
            Cursor cursor = mDB.rawQuery("select * from userInfo where userID = ?",new String[]{String.valueOf(UserInfo.userID)});
                if(cursor.moveToNext()){
                    u = new User(cursor.getInt(0),cursor.getString(1),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
                }

        uid.setText(UserInfo.userID);
        uName.setText(UserInfo.userName);
        assert u != null;
        // 性别判断
        if(u.getUserGender().equals("男")){
            mGender.setChecked(true);
        }else {
            fGender.setChecked(true);
        }
        phone.setText(u.getUserPhone());
        email.setText(u.getUserEmail());
        edu.setText(u.getUserEdu());
        school.setText(u.getUserSchool());
        career.setText(u.getUserCareer());
    }

    public void saveEdit(View view){
        String checkResult = checkInfo();
        if(checkResult != null){                //用户个人信息修改未通过
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage(checkResult);    //输出具体未通过原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }else{
            save();
        }
    }

    public String checkInfo(){
        if(UserInfo.userName == null){
            return "登录状态无效";
        }
        return null;
    }

    public void save(){
        if(!UserInfo.userID.equals("-1")) {
            if(mGender.isChecked()){
                mDB.execSQL("update userInfo set userGender = '男', userPhone = ?,userEmail = ?,userEdu = ?, userTargetSchol = ?, userCareer = ? where userName = ?",
                        new String[]{phone.getText().toString(), email.getText().toString(),edu.getText().toString(),school.getText().toString(),career.getText().toString(), UserInfo.userName});
            }else {
                mDB.execSQL("update userInfo set userGender = '女', userPhone = ?,userEmail = ?,userEdu = ?, userTargetSchol = ?, userCareer = ? where userName = ?",
                        new String[]{phone.getText().toString(), email.getText().toString(), edu.getText().toString(), school.getText().toString(), career.getText().toString(), UserInfo.userName});
            }
            Toast.makeText(EditUserInfo.this,"修改个人信息成功，正在返回上个页面。。", Toast.LENGTH_LONG).show();
            /*
            设置延时操作，时间间隔为2s
             */
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run(){
                    finish();
                }
            };
            timer.schedule(task,DELAY);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告");
            builder.setMessage("未知错误，修改个人信息失败");
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }
    }
}