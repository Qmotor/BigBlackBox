package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bigblackbox.activity.IndexActivity;
import com.example.bigblackbox.tool.CodeUtils;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.UserInfo;


public class MainActivity extends AppCompatActivity {
    private EditText nameText, pwdText;
    private ImageView verCode;
    private EditText verInput;
    private CodeUtils codeUtils;
    private SQLiteDatabase mDB;
    private int id;
    private int judgeAdmin;
    private int isBanned;
    private String uGender;
    private byte[] icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_main);

        /*
        设置状态栏为透明
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        nameText = findViewById(R.id.LUid);
        pwdText = findViewById(R.id.LUpwd);
        verCode = findViewById(R.id.verCode);
        verInput = findViewById(R.id.ver);

        /*
        第一次进入登录界面时刷新验证码窗体控件，使之可以显示验证码
         */
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        verCode.setImageBitmap(bitmap);

        // 测试模块
        verInput.setText(codeUtils.getCode());

    }

    public void flashVer(View view){
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        verCode.setImageBitmap(bitmap);
        verInput.setText(codeUtils.getCode());
    }

    @SuppressLint({"WrongConstant", "ShowToast"})
    public void LoginApp(View view) {
        int amount;
        String codeStr = verInput.getText().toString().trim();
        String code = codeUtils.getCode();

        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from userInfo where user_name = ? and user_pwd = ?",
                new String[]{nameText.getText().toString().trim(), pwdText.getText().toString().trim()});
        amount = c.getCount();
        while (c.moveToNext()) {
            id = c.getInt(0);
            judgeAdmin = c.getInt(3);
            isBanned = c.getInt(4);
            uGender = c.getString(5);
            icon = c.getBlob(c.getColumnIndex("icon"));
        }

        if(icon == null){
            UserInfo.userIcon = false;
        }else {
            UserInfo.userIcon = true;
        }

        if (TextUtils.isEmpty(codeStr)) {
            Toast.makeText(this, "验证码不能为空", 0).show();
            flashVer(view);
            return;
        }

        if (code.equalsIgnoreCase(codeStr)) {
            AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
            if("".equals(nameText.getText().toString()) && "".equals(pwdText.getText().toString())){
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("用户名或密码不能为空");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
            }
            else if(amount == 0){
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("用户名或密码不正确，请重新输入");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
                flashVer(view);
                verInput.setText("");
                pwdText.setText("");
            }
            else if(isBanned == 1){
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("您的账号已被封禁，请联系管理员进行处理！");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
            }
            else {
                Intent intent = new Intent(this, IndexActivity.class);
                //向UserInfo类中的静态变量赋值，方便后续程序调用
                UserInfo.userName = nameText.getText().toString().trim();
                UserInfo.userID = String.valueOf(id);
                UserInfo.isAdmin = String.valueOf(judgeAdmin);
                UserInfo.gender = uGender;
                startActivity(intent);
                flashVer(view);
                verInput.setText("");
                this.finish();
            }
        } else {
            Toast.makeText(this, "验证码输入有误", 0).show();
            flashVer(view);
            verInput.setText("");
        }
    }

    public void jumpReg(View view){
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }

    public void findPwd(View view){
        Intent intent = new Intent(this,FindPwd.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 判断是否有数据从注册页面传入登录页面
        if(UserInfo.regUName != null && UserInfo.regUPwd != null){
            nameText.setText(UserInfo.regUName);
            pwdText.setText(UserInfo.regUPwd);
            // 用完即置空
            UserInfo.regUName = null;
            UserInfo.regUPwd = null;
        }
    }
}