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

        // ???????????????????????????
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
                    //????????????????????????????????????????????????
                    s.delete(start - 1, end);
                    tIntro.setText(s);
                    //?????????????????????
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
        if (resultCode == RESULT_OK) { // ??????????????????????????????
            switch (requestCode) {
                case 0:
                    startPhotoZoom(data.getData()); // ?????????????????????????????????
                    break;
                case 1:
                    if (data != null) {
                        setImageToView(data); // ??????????????????????????????????????????????????????
                    } else {
                        Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
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
                // ????????????
                intent.putExtra("crop", "true");
                // aspectX aspectY ??????????????????
                intent.putExtra("aspectX", 0.1);
                intent.putExtra("aspectY", 0.1);
                // outputX outputY ?????????????????????
                intent.putExtra("outputX", 450);
                intent.putExtra("outputY", 240);
                intent.putExtra("return-data", true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(intent, 1);
            }
        }catch (Exception e){
            Toast.makeText(this, "???????????????", Toast.LENGTH_LONG).show();
        }
    }

    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            im.setImageBitmap(photo);
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("test","null");
        }
    }

    public void InfoChk(View view){
        String checkResult = checkInfo();
        if(checkResult != null){                                                    //?????????????????????????????????
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("????????????");
            builder.setMessage(checkResult);                                        //??????????????????
            builder.setPositiveButton("??????",null);
            builder.create().show();
        }else{
            int follow = 2;                    //??????follow????????????3
            if(mathBtn.isChecked()){
                follow = 0;
            }
            else if(engBtn.isChecked()){
                follow = 1;
            }
            add(follow);                       //???follow????????????add??????
        }
    }

    public String checkInfo(){
        String title = tName.getText().toString().trim();
        String content = tIntro.getText().toString().trim();
        if(UserInfo.userName == null){
            return "??????????????????";
        }if("".equals(title)){
            return "???????????????????????????";
        }if(title.length() < 2){
            return "?????????????????????2??????";
        }if("".equals(content)){
            return "???????????????????????????";
        }if(content.length() < 6){
            return "??????????????????????????????6?????????";
        }if(content.length() > 400){
            return "??????????????????????????????400?????????";
        }
        return null;
    }

    public void teacherInfoClean(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage("????????????????????????????????????");
        builder.setPositiveButton("????????????0_o", null);
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tName.setText("");
                tIntro.setText("");
                b1.setText("");
                b2.setText("");
                b3.setText("");
                b4.setText("");
                b5.setText("");
                Toast.makeText(AddTeacher.this,"?????????",Toast.LENGTH_SHORT).show();
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
                if (photo != null) { // ???????????????
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    ContentValues cv = new ContentValues();
                    cv.put("teacher_icon", baos.toByteArray());
                    mDB.update("teacher", cv, "teacher_name = ?", new String[]{tName.getText().toString().trim()});
                }
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                this.finish();      //??????????????????????????????Activity
            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTeacher.this);
                builder.setTitle("????????????");
                builder.setMessage("???????????????????????????????????????");                                        //??????????????????
                builder.setPositiveButton("??????",null);
                builder.create().show();
            }
        }else {
            Toast.makeText(this,"???????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }
}
