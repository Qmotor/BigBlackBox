package com.example.bigblackbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigblackbox.activity.IndexActivity;

import java.util.Timer;
import java.util.TimerTask;

public class EditPwd extends AppCompatActivity {

    private EditText oldP,newP,enP;
    private SQLiteDatabase mDB;
    private static final long DELAY = 1500;     //设置延迟参数，默认值为1.5s

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);

        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();

        TextView user = findViewById(R.id.editUser);
        oldP = findViewById(R.id.oldPwd);
        newP = findViewById(R.id.newPwd);
        enP = findViewById(R.id.enNewPwd);
        Button clearBtn = findViewById(R.id.clear);

        /*
        设置状态栏为透明
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(oldP.getText().toString().trim()) && "".equals(newP.getText().toString().trim()) && "".equals(enP.getText().toString().trim())){
                    Toast.makeText(EditPwd.this,"本来就没东西就没必要清空了吧^_^",Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditPwd.this);
                    builder.setTitle("提示");
                    builder.setMessage("您确定要清空以上信息吗？");
                    builder.setPositiveButton("我手滑啦O_o", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            oldP.setText("");
                            newP.setText("");
                            enP.setText("");
                            Toast.makeText(EditPwd.this,"已清除",Toast.LENGTH_LONG).show();

                        }
                    });
                    builder.create().show();
                }
            }
        });
        user.setText("当前操作用户为："+UserInfo.userName);
    }

    public void edit(View view) {
        int amount;
        String op = oldP.getText().toString().trim();
        String np = newP.getText().toString().trim();
        String enp = enP.getText().toString().trim();

        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from userInfo where userName = ? and userPwd = ?",
                new String[]{UserInfo.userName, op});
        amount = c.getCount();
        // 将select查询结果数赋值给amount

        /*
        以下代码为系统验证用户所输入信息是否无误
        若信息有误，则向用户提示相应信息
         */
            if("".equals(op)){
                AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("旧密码不能为空");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
            }

            else if(enp.length() < 5 || enp.length() > 13){
                AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("新密码长度应在6-12位之间");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
            }

            else if(!np.equals(enp)){
                AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("新密码输入不一致");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
            }

            else  if(op.equals(np)){
                AlertDialog.Builder empBuilder = new AlertDialog.Builder(this);
                empBuilder.setTitle("错误提示！");
                empBuilder.setMessage("新密码与旧密码不能相同");
                empBuilder.setPositiveButton("确定",null);
                empBuilder.create().show();
            }


            // amount为0，即说明用户输入旧密码与当前账户不匹配
            else if(amount == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("错误提示！");
                builder.setMessage("旧密码输入有误，请重新输入");
                builder.setPositiveButton("确定",null);
                builder.create().show();
                oldP.setText("");
            }

            // 用户输入信息无误，则可以修改当前账户密码，修改密码完毕后，自动跳转至登录界面
            else {
                mDB.execSQL("update userInfo set userPwd = ? where userName = ?",
                        new String[]{np, UserInfo.userName});
                // 修改密码成功后，自动清空UserInfo中的用户信息，以防止出现数据错误
                UserInfo.userName = null;
                UserInfo.userID = null;

                Toast.makeText(EditPwd.this,"修改密码成功，即将跳转至登录界面", Toast.LENGTH_SHORT).show();
                final Intent localIntent = new Intent(this,MainActivity.class);
                Timer timer = new Timer();

                // 设置定时操作，保证程序结构合理
                TimerTask task = new TimerTask() {
                    @Override
                    public void run(){
                        startActivity(localIntent);
                        finish();
                    }
                };
                timer.schedule(task,DELAY);
            }
    }
}