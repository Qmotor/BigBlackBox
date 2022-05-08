package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigblackbox.entity.User;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;

import java.util.Timer;
import java.util.TimerTask;

public class EditUserInfo extends AppCompatActivity {
    private SQLiteDatabase mDB;
    private EditText phone, email, edu, school, career;
    private RadioButton mGender;
    public String uname;
    private static final long DELAY = 1500;            //设置延迟参数，默认值为1.5s

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        TextView uid = findViewById(R.id.uid);
        TextView uName = findViewById(R.id.uname);
        mGender = findViewById(R.id.uMale);
        RadioButton fGender = findViewById(R.id.uFemale);
        phone = findViewById(R.id.uPhone);
        email = findViewById(R.id.uEmail);
        edu = findViewById(R.id.uEdu);
        school = findViewById(R.id.uSchool);
        career = findViewById(R.id.uCareer);

        int uID = getIntent().getIntExtra("userID", -1);

        User u = null;
        @SuppressLint("Recycle") Cursor cursor;
        if(uID == -1) {
            cursor = mDB.rawQuery("select * from userInfo where user_id = ?", new String[]{String.valueOf(UserInfo.userID)});
        }else {
            cursor = mDB.rawQuery("select * from userInfo where user_id = ?", new String[]{String.valueOf(uID)});
        }
        if (cursor.moveToNext()) {
            u = new User(cursor.getInt(0), cursor.getString(1), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getBlob(cursor.getColumnIndex("icon")));
            uname = cursor.getString(1);
        }

        assert u != null;
        uid.setText(String.valueOf(u.getUserID()));
        uName.setText(u.getUserName());
        // 性别判断
        if (u.getUserGender().equals("男")) {
            mGender.setChecked(true);
        } else {
            fGender.setChecked(true);
        }
        phone.setText(u.getUserPhone());
        email.setText(u.getUserEmail());
        edu.setText(u.getUserEdu());
        school.setText(u.getUserSchool());
        career.setText(u.getUserCareer());
    }

    public void saveEdit(View view) {
        String checkResult = checkInfo();
        if (checkResult != null) {                //用户个人信息修改未通过
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage(checkResult);    //输出具体未通过原因
            builder.setPositiveButton("确定", null);
            builder.create().show();
        } else {
            save();
        }
    }

    public String checkInfo() {
        if (UserInfo.userName == null) {
            return "登录状态无效";
        }
        return null;
    }

    public void save() {
        // 此处不上传头像
        if (!UserInfo.userID.equals("-1")) {
            if (mGender.isChecked()) {
                mDB.execSQL("update userInfo set user_gender = '男', user_phone = ?,user_email = ?,user_edu = ?, user_target_school = ?, user_career = ? where user_name = ?",
                        new String[]{phone.getText().toString().trim(), email.getText().toString().trim(), edu.getText().toString().trim(), school.getText().toString().trim(), career.getText().toString().trim(), uname});
            } else {
                mDB.execSQL("update userInfo set user_gender = '女', user_phone = ?,user_email = ?,user_edu = ?, user_target_school = ?, user_career = ? where user_name = ?",
                        new String[]{phone.getText().toString().trim(), email.getText().toString().trim(), edu.getText().toString().trim(), school.getText().toString().trim(), career.getText().toString().trim(), uname});
            }
            Toast.makeText(EditUserInfo.this, "修改个人信息成功，正在返回上个页面。。", Toast.LENGTH_SHORT).show();

            //设置延时操作，时间间隔为1.5s
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            };
            timer.schedule(task, DELAY);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告");
            builder.setMessage("未知错误，修改个人信息失败");
            builder.setPositiveButton("确定", null);
            builder.create().show();
        }
    }

    public void back(View view){
        this.finish();
    }
}