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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindPwd extends AppCompatActivity {
    private TextView userName,ver;
    private ImageView verImage;
    private CodeUtils codeUtils;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_find_pwd);

        userName = findViewById(R.id.Uid);
        ver = findViewById(R.id.verInput);
        verImage = findViewById(R.id.verCode);

        /*
        设置状态栏为透明
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        /*
        在第一次进入修改密码界面时，自动刷新ImageView中的验证码窗体控件
        保证验证码显示正常，防止程序闪退
         */
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        verImage.setImageBitmap(bitmap);
    }

    public void flash(View view){
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        verImage.setImageBitmap(bitmap);
    }

    public void nextStep(View view){
        int amount;
        String codeStr = ver.getText().toString().trim();     //验证码输入框字符串
        String code = codeUtils.getCode();                    //验证码控件字符串
        String uName = userName.getText().toString();         //用户名

        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from userInfo where userName = ?",
                new String[]{uName});
        amount = c.getCount();
        /*
        利用用户输入的用户名在数据库内进行查询，将查询的结果数赋值给amount
         */

        if (TextUtils.isEmpty(codeStr)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (code.equalsIgnoreCase(codeStr)) {
            if(uName.equals("")){
                AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("用户名不能为空");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
            }

            /*
            amount为0，即说明用户输入的用户名在数据库中不存在
             */
             else if(amount == 0){
                AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("该用户不存在，请重新输入");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
                flash(view);                    //调用flash()方法，自动刷新验证码
                userName.setText("");           //将用户输入的用户名和密码置空
                ver.setText("");
            }
             /*
             验证通过，自动跳转至找回密码界面
              */
             else{
                Intent intent = new Intent(this, FindPwd_detail.class);
                intent.putExtra("editPwdUser",uName);
                startActivity(intent);
                flash(view);              //调用flash()方法，刷新验证码
                ver.setText("");          //将验证码输入框置空
            }
        }
        else {
            Toast.makeText(this, "验证码输入有误", Toast.LENGTH_LONG).show();
            flash(view);                  //调用flash()方法，刷新验证码
            ver.setText("");              //将验证码输入框置空
        }
    }
}