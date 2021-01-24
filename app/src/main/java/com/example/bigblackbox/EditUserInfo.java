package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigblackbox.entity.User;

public class EditUserInfo extends AppCompatActivity {

    private DbUtil mHelper;
    private SQLiteDatabase mDB;

    private TextView uid,uname;
    private EditText phone,email,edu,school,career;
    private RadioButton maleBtn,femaleBtn;

    SQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        uid = findViewById(R.id.uid);
        uname = findViewById(R.id.uname);
        phone = findViewById(R.id.uPhone);
        email = findViewById(R.id.uEmail);
        edu = findViewById(R.id.uEdu);
        school = findViewById(R.id.uSchool);
        career = findViewById(R.id.uCareer);
        maleBtn = findViewById(R.id.uMale);
        femaleBtn = findViewById(R.id.uFemale);

        helper = new DbUtil(this);
        User u = null;
        try(SQLiteDatabase db = helper.getReadableDatabase()){
            try(Cursor cursor = db.rawQuery("select * from userInfo where userID = ?",new String[]{String.valueOf(UserInfo.userID)})){
                if(cursor.moveToNext()){
                    u = new User(cursor.getInt(0),cursor.getString(1),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
                }
            }
        }

        uid.setText(UserInfo.userID);
        uname.setText(UserInfo.userName);
        phone.setText(u.getUserPhone());
        email.setText(u.getUserEmail());
        edu.setText(u.getUserEdu());
        school.setText(u.getUserSchool());
        career.setText(u.getUserCareer());

//        if(u.getUserGender() != "男"){
//            femaleBtn.isChecked();
//        }
//        else{
//            maleBtn.isChecked();
//        }
    }
    public void saveEdit(View view){
        String checkResult = checkInfo();
        if(checkResult != null){         //验证信息未通过
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!!!");
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
        if(UserInfo.userID != "-1") {
            mDB.execSQL("update userInfo set userPhone = ?,userEmail = ?,userEdu = ?, userTargetSchol = ?, userCareer = ? where userName = ?",
                    new String[]{phone.getText().toString(), email.getText().toString(),edu.getText().toString(),school.getText().toString(),career.getText().toString(), UserInfo.userName});
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!!!");
            builder.setMessage("修改失败");    //输出具体未通过原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }
    }
}