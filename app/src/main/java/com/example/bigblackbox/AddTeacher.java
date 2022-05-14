package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.tool.ImageUtils;
import com.example.bigblackbox.tool.UserInfo;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTeacher extends AppCompatActivity {
    String[] item;
    String content;
    private EditText tName,tIntro,b1,b2,b3,b4,b5;
    private RadioGroup rg;
    private Spinner sp;
    private RadioButton mathBtn,engBtn;
    private ImageView im;
    private static Bitmap photo;
    private TextView numShow;
    public static int judge;
    private SQLiteDatabase mDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        tName = findViewById(R.id.tn);
        tIntro = findViewById(R.id.ti);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        numShow = findViewById(R.id.tNum);
        mathBtn = findViewById(R.id.mathT);
        engBtn = findViewById(R.id.engT);
        im = findViewById(R.id.tPic);
        rg = findViewById(R.id.tcType);
        sp = findViewById(R.id.proT);

        judge = getIntent().getIntExtra("judgeID",-1);

        if(judge != -1){
            rg.setVisibility(View.GONE);
        }else {
            sp.setVisibility(View.GONE);
        }

        item = getResources().getStringArray(R.array.professionalTeacher);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                content = item[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 实时显示输入框字数
        tIntro.addTextChangedListener(new TextWatcher() {
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
                numShow.setText(s.length() + "/400");
                int start = tIntro.getSelectionStart();
                int end = tIntro.getSelectionEnd();
                if (wordNum.length() > tIntro.length()) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(start - 1, end);
                    tIntro.setText(s);
                    //设置光标在最后
                    tIntro.setSelection(end);
                }
            }
        });

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAlbumIntent;
                if (android.os.Build.VERSION.SDK_INT >= 28){
                    openAlbumIntent = new Intent(Intent.ACTION_PICK);
                } else {
                    openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case 0:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case 1:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    } else {
                        Toast.makeText(this, "图片路径有误", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    protected void startPhotoZoom(Uri uri) {
        try {
            if (uri == null) {
                Log.i("tag", "The uri is not exist.");
            } else {
                Log.i("tag", "The uri is existing.");
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                // 设置裁剪
                intent.putExtra("crop", "true");
                // aspectX aspectY 是宽高的比例
                intent.putExtra("aspectX", 0.1);
                intent.putExtra("aspectY", 0.1);
                // outputX outputY 是裁剪图片宽高
                intent.putExtra("outputX", 450);
                intent.putExtra("outputY", 240);
                intent.putExtra("return-data", true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(intent, 1);
            }
        }catch (Exception e){
            Toast.makeText(this, "未知错误！", Toast.LENGTH_LONG).show();
        }
    }

    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            im.setImageBitmap(photo);
            Toast.makeText(this, "上传图片成功", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("test","null");
        }
    }

    public void InfoChk(View view){
        String checkResult = checkInfo();
        if(checkResult != null){                                                    //系统验证帖子信息不合法
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage(checkResult);                                        //输出具体原因
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }else{
            int follow = 2;                    //默认follow初始值为3
            if(mathBtn.isChecked()){
                follow = 0;
            }
            else if(engBtn.isChecked()){
                follow = 1;
            }
            add(follow);                       //将follow参数传入add方法
        }
    }

    public String checkInfo(){
        String title = tName.getText().toString().trim();
        String content = tIntro.getText().toString().trim();
        if(UserInfo.userName == null){
            return "登录状态无效";
        }if("".equals(title)){
            return "老师姓名不能为空！";
        }if(title.length() < 2){
            return "老师姓名至少为2位！";
        }if("".equals(content)){
            return "老师简介不能为空！";
        }if(content.length() < 6){
            return "老师简介长度至少包含6个字符";
        }if(content.length() > 400){
            return "老师简介长度最多包含400个字符";
        }
        return null;
    }

    public void teacherInfoClean(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您确定要清空以上信息吗？");
        builder.setPositiveButton("我手滑了0_o", null);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tName.setText("");
                tIntro.setText("");
                b1.setText("");
                b2.setText("");
                b3.setText("");
                b4.setText("");
                b5.setText("");
                Toast.makeText(AddTeacher.this,"已清除",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    public void add(int follow){
        if(UserInfo.isAdmin.equals("1")){
            try {
                if (judge == -1) {
                    mDB.execSQL("insert into teacher values(null,?,null,?,null,?,?,?,?,?,?)",
                            new String[]{tName.getText().toString().trim(), String.valueOf(follow), tIntro.getText().toString().trim(),
                                    b1.getText().toString().trim(), b2.getText().toString().trim(), b3.getText().toString().trim(),
                                    b4.getText().toString().trim(), b5.getText().toString().trim()});
                } else {
                    mDB.execSQL("insert into teacher values(null,?,null,?,?,?,?,?,?,?,?)",
                            new String[]{tName.getText().toString().trim(), "3", content, tIntro.getText().toString().trim(),
                                    b1.getText().toString().trim(), b2.getText().toString().trim(), b3.getText().toString().trim(),
                                    b4.getText().toString().trim(), b5.getText().toString().trim()});
                }
                if (photo != null) { // 添加了图片
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    ContentValues cv = new ContentValues();
                    cv.put("teacher_icon", baos.toByteArray());
                    mDB.update("teacher", cv, "teacher_name = ?", new String[]{tName.getText().toString().trim()});
                }
                Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
                this.finish();      //发布成功后，关闭当前Activity
            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTeacher.this);
                builder.setTitle("温馨提示");
                builder.setMessage("该老师已存在，请重新编辑！");                                        //输出具体原因
                builder.setPositiveButton("确定",null);
                builder.create().show();
            }
        }else {
            Toast.makeText(this,"权限不足，发布失败", Toast.LENGTH_SHORT).show();
        }
    }
}
