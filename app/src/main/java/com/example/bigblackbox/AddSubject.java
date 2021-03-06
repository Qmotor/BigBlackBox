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
import com.example.bigblackbox.tool.UserInfo;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class AddSubject extends AppCompatActivity {
    private static boolean flagCon2 = false;
    private static boolean flagCon3 = false;
    private static Bitmap photo1, photo2;
    private SQLiteDatabase mDB;
    private RadioButton mathSubBtn,engSubBtn;
    private View v1,v2,v3,v4,vt1,vt2,vt3,vt4;
    private EditText sTitle,con1,con2,con3;
    private ImageView pic1,pic2;
    private TextView add1,add2,num1,num2,num3,addCon2,addCon3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        sTitle = findViewById(R.id.subTitle);
        add1 = findViewById(R.id.addPic1);
        add2 = findViewById(R.id.addPic2);
        pic1 = findViewById(R.id.pic1);
        pic2 = findViewById(R.id.pic2);
        con1 = findViewById(R.id.con1);
        con2 = findViewById(R.id.con2);
        con3 = findViewById(R.id.con3);
        num1 = findViewById(R.id.conNum1);
        num2 = findViewById(R.id.conNum2);
        num3 = findViewById(R.id.conNum3);
        pic1 = findViewById(R.id.pic1);
        pic2 = findViewById(R.id.pic2);
        addCon2 = findViewById(R.id.addCon2);
        addCon3 = findViewById(R.id.addCon3);
        v1 = findViewById(R.id.picView1);
        v2 = findViewById(R.id.picView2);
        v3 = findViewById(R.id.conView2);
        v4 = findViewById(R.id.conView3);
        vt1 = findViewById(R.id.vt1);
        vt2 = findViewById(R.id.vt2);
        vt3 = findViewById(R.id.vt3);
        vt4 = findViewById(R.id.vt4);
        mathSubBtn = findViewById(R.id.mathB);
        engSubBtn = findViewById(R.id.engB);

        pic1.setVisibility(View.GONE);
        pic2.setVisibility(View.GONE);
        con2.setVisibility(View.GONE);
        con3.setVisibility(View.GONE);
        num2.setVisibility(View.GONE);
        num3.setVisibility(View.GONE);
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        v4.setVisibility(View.GONE);


        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic1.setVisibility(View.VISIBLE);
                v1.setVisibility(View.VISIBLE);
                add1.setVisibility(View.GONE);
                vt1.setVisibility(View.GONE);
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic2.setVisibility(View.VISIBLE);
                v2.setVisibility(View.VISIBLE);
                add2.setVisibility(View.GONE);
                vt2.setVisibility(View.GONE);
            }
        });

        addCon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con2.setVisibility(View.VISIBLE);
                num2.setVisibility(View.VISIBLE);
                v3.setVisibility(View.VISIBLE);
                addCon2.setVisibility(View.GONE);
                vt3.setVisibility(View.GONE);
                flagCon2 = true;
            }
        });

        addCon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con3.setVisibility(View.VISIBLE);
                num3.setVisibility(View.VISIBLE);
                v4.setVisibility(View.VISIBLE);
                addCon3.setVisibility(View.GONE);
                vt4.setVisibility(View.GONE);
                flagCon3 = true;
            }
        });

        con1.addTextChangedListener(new TextWatcher() {
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
                num1.setText(s.length() + "/400");
                int start = con1.getSelectionStart();
                int end = con1.getSelectionEnd();
                if (wordNum.length() > con1.length()) {
                    //????????????????????????????????????????????????
                    s.delete(start - 1, end);
                    con1.setText(s);
                    //?????????????????????
                    con1.setSelection(end);
                }
            }
        });

        con2.addTextChangedListener(new TextWatcher() {
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
                num2.setText(s.length() + "/400");
                int start = con2.getSelectionStart();
                int end = con2.getSelectionEnd();
                if (wordNum.length() > con2.length()) {
                    //????????????????????????????????????????????????
                    s.delete(start - 1, end);
                    con2.setText(s);
                    //?????????????????????
                    con2.setSelection(end);
                }
            }
        });

        con3.addTextChangedListener(new TextWatcher() {
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
                num3.setText(s.length() + "/400");
                int start = con3.getSelectionStart();
                int end = con3.getSelectionEnd();
                if (wordNum.length() > con3.length()) {
                    //????????????????????????????????????????????????
                    s.delete(start - 1, end);
                    con3.setText(s);
                    //?????????????????????
                    con3.setSelection(end);
                }
            }
        });

        pic1.setOnClickListener(new View.OnClickListener() {
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

        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAlbumIntent;
                if (android.os.Build.VERSION.SDK_INT >= 28){
                    openAlbumIntent = new Intent(Intent.ACTION_PICK);
                } else {
                    openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // ??????????????????????????????
            switch (requestCode) {
                case 0:
                    startPhotoOneZoom(data.getData()); // ?????????????????????????????????
                    break;
                case 1:
                    startPhotoTwoZoom(data.getData()); // ?????????????????????????????????
                    break;
                case 2:
                    if (data != null) {
                        setImageOneToView(data); // ??????????????????????????????????????????????????????
                    } else {
                        Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if (data != null) {
                        setImageTwoToView(data); // ??????????????????????????????????????????????????????
                    } else {
                        Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    protected void startPhotoOneZoom(Uri uri) {
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
                startActivityForResult(intent, 2);
            }
        }catch (Exception e){
            Toast.makeText(this, "???????????????", Toast.LENGTH_LONG).show();
        }
    }

    protected void startPhotoTwoZoom(Uri uri) {
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
                startActivityForResult(intent, 3);
            }
        }catch (Exception e){
            Toast.makeText(this, "???????????????", Toast.LENGTH_LONG).show();
        }
    }

    protected void setImageOneToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo1 = extras.getParcelable("data");
            pic1.setImageBitmap(photo1);
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("test","null");
        }
    }

    protected void setImageTwoToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo2 = extras.getParcelable("data");
            pic2.setImageBitmap(photo2);
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("test","null");
        }
    }

    public void subInfoChk(View view){
        String checkResult = checkInfo();
        if(checkResult != null){                                                    //?????????????????????????????????
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("????????????");
            builder.setMessage(checkResult);                                        //??????????????????
            builder.setPositiveButton("??????",null);
            builder.create().show();
        }else{
            int follow = 3;                    //??????follow????????????3
            if(mathSubBtn.isChecked()){
                follow = 1;
            }
            else if(engSubBtn.isChecked()){
                follow = 2;
            }
            add(follow);                       //???follow????????????add??????
        }
    }

    public String checkInfo(){
        String title = sTitle.getText().toString().trim();
        String content = con1.getText().toString().trim();
        if(UserInfo.userName == null){
            return "??????????????????";
        }if("".equals(title)){
            return "?????????????????????";
        }if(title.length() < 2){
            return "?????????????????????2??????";
        }if("".equals(content)){
            return "????????????1???????????????";
        }if(content.length() < 6){
            return "????????????1??????????????????6?????????";
        }if(content.length() > 400){
            return "????????????1??????????????????400?????????";
        }
        return null;
    }

    public void subInfoClean(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage("????????????????????????????????????");
        builder.setPositiveButton("????????????0_o", null);
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sTitle.setText("");
                con1.setText("");
                vt1.setVisibility(View.VISIBLE);
                vt2.setVisibility(View.VISIBLE);
                vt3.setVisibility(View.VISIBLE);
                vt4.setVisibility(View.VISIBLE);
                add1.setVisibility(View.VISIBLE);
                add2.setVisibility(View.VISIBLE);
                addCon2.setVisibility(View.VISIBLE);
                addCon3.setVisibility(View.VISIBLE);
                pic1.setVisibility(View.GONE);
                pic2.setVisibility(View.GONE);
                con2.setVisibility(View.GONE);
                con3.setVisibility(View.GONE);
                num2.setVisibility(View.GONE);
                num3.setVisibility(View.GONE);
                v1.setVisibility(View.GONE);
                v2.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                v4.setVisibility(View.GONE);
                flagCon2 = false;
                flagCon3 = false;
                Toast.makeText(AddSubject.this,"?????????",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    public void add(int follow){
        if(UserInfo.isAdmin.equals("1")){
            Random random = new Random();
            int num = random.nextInt(3) + 1;
            try {
                    if (flagCon2) {
                        if (flagCon3) {
                            // 2,3?????????
                            mDB.execSQL("insert into subject values(null,?,?,?,null,?,null,?,?,?)",
                                    new String[]{UserInfo.userName, sTitle.getText().toString().trim(), con1.getText().toString().trim(), con2.getText().toString().trim(),
                                    con3.getText().toString().trim(), String.valueOf(follow), String.valueOf(num)});
                        } else {
                            // 2?????????3??????
                            mDB.execSQL("insert into subject values(null,?,?,?,null,?,null,?,?,?)",
                                    new String[]{UserInfo.userName, sTitle.getText().toString().trim(), con1.getText().toString().trim(), con2.getText().toString().trim(),
                                    "", String.valueOf(follow), String.valueOf(num)});
                        }
                    } else if (flagCon3) {
                        // 2?????????3??????
                        mDB.execSQL("insert into subject values(null,?,?,?,null,?,null,?,?,?)",
                                new String[]{UserInfo.userName, sTitle.getText().toString().trim(), con1.getText().toString().trim(), "",
                                con3.getText().toString().trim(), String.valueOf(follow), String.valueOf(num)});
                    }
                    if (!flagCon2 && !flagCon3) {
                        // 2,3?????????
                        mDB.execSQL("insert into subject values(null,?,?,?,null,?,null,?,?,?)",
                                new String[]{UserInfo.userName, sTitle.getText().toString().trim(), con1.getText().toString().trim(), "",
                                 "", String.valueOf(follow), String.valueOf(num)});
                    }
            if (photo1 != null) { // ???????????????1
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo1.compress(Bitmap.CompressFormat.PNG, 100, baos);
                ContentValues cv = new ContentValues();
                cv.put("sub_fir_pic", baos.toByteArray());
                mDB.update("subject", cv, "sub_title = ?", new String[]{sTitle.getText().toString().trim()});
            }
            if (photo2 != null) { // ???????????????2
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo2.compress(Bitmap.CompressFormat.PNG, 100, baos);
                ContentValues cv = new ContentValues();
                cv.put("sub_sec_pic", baos.toByteArray());
                mDB.update("subject", cv, "sub_title = ?", new String[]{sTitle.getText().toString().trim()});
            }
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                this.finish();      //??????????????????????????????Activity
            } catch (Exception e){
                 AlertDialog.Builder builder = new AlertDialog.Builder(this);
                 builder.setTitle("????????????");
                 builder.setMessage("???????????????????????????????????????????????????");                                        //??????????????????
                 builder.setPositiveButton("??????",null);
                 builder.create().show();
            }
        }else {
            Toast.makeText(this,"???????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flagCon2 = false;
        flagCon3 = false;
    }
}
