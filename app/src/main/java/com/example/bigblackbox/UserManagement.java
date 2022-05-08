package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.UserAdapter;
import com.example.bigblackbox.entity.User;
import com.example.bigblackbox.tool.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class UserManagement extends AppCompatActivity {
    private SQLiteDatabase mDB;
    private final List<User> u = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_user_management);
        // 绑定控件
        final ListView listView = findViewById(R.id.userList);
        ImageView searchUser = findViewById(R.id.searchUserBtn);
        // 检索用户信息
        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from userInfo", new String[0]);
        while (cursor.moveToNext()) {
            u.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getBlob(cursor.getColumnIndex("icon"))));
        }
        // 将数据传至适配器
        listView.setAdapter(new UserAdapter(this, u));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserManagement.this);
                final User user = u.get(position);
                String[] items = { "修改用户信息", "管理用户发布帖子","修改用户密码", "封禁用户", "解封用户" };
                builder.setTitle("操作选择");
                builder.setNegativeButton("取消", null);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which) {
                            case 0: // 修改用户信息
                                intent = new Intent(UserManagement.this, EditUserInfo.class);
                                intent.putExtra("userID", user.getUserID());
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(UserManagement.this, MyPosting.class);
                                intent.putExtra("userName", user.getUserName());
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(UserManagement.this, EditPwd.class);
                                intent.putExtra("userName", user.getUserName());
                                startActivity(intent);
                                break;
                            case 3: // 允
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserManagement.this);
                                builder.setTitle("提示");
                                builder.setMessage("您确定要允封该用户吗？");
                                builder.setPositiveButton("我手滑了0_o", null);
                                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (user.getAdmin() == 1) {
                                            Toast.makeText(UserManagement.this, "不能允封管理员噢~", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mDB.execSQL("update userInfo set is_banned = '1' where user_id = ? ",
                                                    new String[]{String.valueOf(user.getUserID())});
                                            Toast.makeText(UserManagement.this, "允封成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.create().show();
                                break;
                            case 4: // 解
                                AlertDialog.Builder build = new AlertDialog.Builder(UserManagement.this);
                                build.setTitle("提示");
                                build.setMessage("您确定要解封该用户吗？");
                                build.setPositiveButton("我手滑了0_o", null);
                                build.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDB.execSQL("update userInfo set is_banned = '0' where user_id = ? ",
                                                new String[]{String.valueOf(user.getUserID())});
                                        Toast.makeText(UserManagement.this, "解封成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                build.create().show();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

        searchUser.setOnClickListener(new View.OnClickListener() {  // 以用户名对用户做模糊查找
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserManagement.this);
                final LinearLayout linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.dialogname,null);
                builder.setTitle("用户查找");
                builder.setView(linearLayout);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        u.clear();
                        EditText albumName = linearLayout.findViewById(R.id.eUser);
                        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from userInfo where user_name like ?", new String[]{"%" + String.valueOf(albumName.getText()).trim() + "%"});
                        while (cursor.moveToNext()) {
                            u.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getBlob(cursor.getColumnIndex("icon"))));
                        }
                        listView.setAdapter(new UserAdapter(UserManagement.this, u));
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });
    }
}
