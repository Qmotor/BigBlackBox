package com.example.bigblackbox.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigblackbox.Collection;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.EditPwd;
import com.example.bigblackbox.EditUserInfo;
import com.example.bigblackbox.tool.ImageUtils;
import com.example.bigblackbox.MainActivity;
import com.example.bigblackbox.MyPosting;
import com.example.bigblackbox.R;
import com.example.bigblackbox.SecurityQuestion;
import com.example.bigblackbox.tool.UserInfo;
import com.example.bigblackbox.UserManagement;
import com.example.bigblackbox.entity.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class Mine extends Fragment {
    protected static Uri tempUri;
    protected ImageView uIcon;
    private SQLiteDatabase mDB;
    private static boolean flag = true;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(getContext());
        mDB = mHelper.getReadableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uIcon = view.findViewById(R.id.head);
        View v = view.findViewById(R.id.view);
        TextView name = view.findViewById(R.id.uName);
        TextView id = view.findViewById(R.id.uID);
        TextView edit = view.findViewById(R.id.edit_userInfo);
        TextView myPost = view.findViewById(R.id.myPosting);
        TextView editPwd = view.findViewById(R.id.editPwd);
        TextView loginOut = view.findViewById(R.id.login_out);
        TextView safeAsk = view.findViewById(R.id.safeQuestion);
        TextView manUser = view.findViewById(R.id.managerUser);
        TextView collect = view.findViewById(R.id.collection);
        TextView about = view.findViewById(R.id.aboutUs);

        name.setText(UserInfo.userName);
        if(UserInfo.isAdmin.equals("1")){
            name.setTextColor(Color.parseColor("#FF0000"));
            name.setText(UserInfo.userName + "（管理员）");
        }

        if(!UserInfo.isAdmin.equals("1")){
            manUser.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
        }

        id.setText("用户ID："+UserInfo.userID);

        uIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showChoosePicDialog();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditUserInfo.class);
                startActivity(intent);
            }
        });

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyPosting.class);
                startActivity(intent);
            }
        });

        editPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditPwd.class);
                startActivity(intent);
            }
        });

        safeAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SecurityQuestion.class);
                startActivity(intent);
            }
        });

        manUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserManagement.class);
                startActivity(intent);
            }
        });

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Collection.class);
                startActivity(intent); }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"我们啥也不是", Toast.LENGTH_SHORT).show();
            }
        });

        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("温馨提示");
                builder.setMessage("你确定要注销当前登录账号吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfo.userName = null;
                        UserInfo.userID = null;
                        UserInfo.isAdmin = null;
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getContext(),"注销成功",Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();  // 关闭当前fragment
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });
    }

    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent openAlbumIntent;
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        // 模拟器用ACTION_GET_CONTENT
                        // 真机用ACTION_PICK
                        if (android.os.Build.VERSION.SDK_INT >= 28){
                            openAlbumIntent = new Intent(Intent.ACTION_PICK);
                        } else {
                            openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        }
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(requireContext(), permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(requireContext(), "com.example.bigblackbox.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
                switch (requestCode) {
                    case TAKE_PICTURE:
                        startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                        break;
                    case CHOOSE_PICTURE:
                        startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                        break;
                    case CROP_SMALL_PICTURE:
                        if (data != null) {
                            setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        }else {
                            Toast.makeText(getContext(), "图片路径有误", Toast.LENGTH_SHORT).show();
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
                tempUri = uri;
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                // 设置裁剪
                intent.putExtra("crop", "true");
                // aspectX aspectY 是宽高的比例
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                // outputX outputY 是裁剪图片宽高
                intent.putExtra("outputX", 240);
                intent.putExtra("outputY", 240);
                intent.putExtra("return-data", true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                /*
                    目前出现一个BUG（？）：使用模拟器进行调试时无法上传头像，使用真机调试则无此问题
                    初步考虑是设备上去掉了支持Crop的应用
                    解决思路是在跳转前做检测，或者是全局做检测
                    https://blog.csdn.net/weixin_51116314/article/details/112343601
                    https://www.cnblogs.com/xing-star/p/11803731.html
                */
                startActivityForResult(intent, CROP_SMALL_PICTURE);
            }
        }catch (Exception e){
            // 报这个错误大概率是因为当前测试环境是模拟器
            // 真机测试环境下，头像上传功能可以完美运行，模拟器报错原因未知
            Toast.makeText(requireContext(), "未知错误！", Toast.LENGTH_LONG).show();
        }
    }

    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            flag = false;
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            if(!flag) {
                uIcon.setImageBitmap(photo);
            }
            /*
              掉坑记录：SQLite中存储图片的字段类型为BLOB，即存入数据库中该字段的数据类型为byte[]
              而在安卓应用中 ImageView 控件需要的数据类型为bitmap
              则有安卓 -> SQLite：bitmap转byte[]，且必须保持数据上传格式不变，不能转为其它类型后再转回原数据，否则会导致数据丢失
              SQLite -> 安卓：byte[]转bitmap
             */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 此处用 ContentValues 做载体，就是为了防止数据类型发生变化
            ContentValues cv = new ContentValues();
            // "icon"为数据库字段名，baos.toByteArray()为图片数据
            cv.put("icon",baos.toByteArray());
            // 此处参考 https://cxybb.com/article/gaotong2055/84513834
            // 由于此前极少采用安卓原生的的SQLiteDatabase方法操作数据库，都是直接使用SQL语句修改数据库，故特此记录
            // update(数据库表名，需要更新的数据，表中约束，约束参数)
            mDB.update("userInfo",cv,"user_name = ?",new String[]{UserInfo.userName});
            Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        User u = null;
        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from userInfo where user_id = ?", new String[]{String.valueOf(UserInfo.userID)});
        if (cursor.moveToNext()) {
            u = new User(cursor.getInt(0), cursor.getString(1), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getBlob(cursor.getColumnIndex("icon")));
        }
        assert u != null;
        if(flag) {
            if (!UserInfo.userIcon) {  // 没有头像，使用默认头像
                if (u.getUserGender().equals("男")) {
                    uIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.boy));
                } else {
                    uIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.girl));
                }
            } else {
                byte[] pic = cursor.getBlob(cursor.getColumnIndex("icon"));
                Bitmap bmpOut = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                uIcon.setImageBitmap(bmpOut);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = true;
    }
}