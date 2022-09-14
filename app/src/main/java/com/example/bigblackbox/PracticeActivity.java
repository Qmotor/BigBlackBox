package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bigblackbox.fragment.QuestionFragment;
import com.example.bigblackbox.tool.MyTag;
import com.example.bigblackbox.tool.ToolHelper;
import com.example.bigblackbox.tool.UserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener {
    private int tab;
    private String table, content, from;
    private TextView tvTitle;
    private Cursor cursor;
    private boolean isCollect = false;
    private int num;
    private int index = 0, index1;
    public static List<String> anList;
    private String source;
    private String qid, type, que, A, B, C, D, answer, detail;
    private ImageView imgPre, imgNext;
    private AdapterViewFlipper vf;
    private BaseAdapter adapter;
    private ProgressBar pb;
    private View root;
    private TextView tvQue, tvDetail, tvAns;
    private CheckBox cb1, cb2, cb3, cb4;
    private ImageView imgCollect, imgCard;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        //ActionBar工具栏设置
        Toolbar toolbar = findViewById(R.id.toolbar_que);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getPreferences(Context.MODE_PRIVATE);
        Intent intent = getIntent();
        tab = intent.getIntExtra("tab", 1);
        initTable();
        initView();
    }

    private void initTable() {
        switch (tab) {
            case MyTag.QUE://题库
                table = "que";
                content = "题库";
                from = "que";
                break;
            case MyTag.COLLECT://收藏
                table = "collection ,que where collection.qid=que._id ";
                content = "收藏";
                from = "collect";
                break;
            case MyTag.WRONG://错题
                table = "wrong,que where wrong.qid=que._id ";
                content = "错题";
                from = "wrong";
                break;
        }
        getProgress(from);
    }

    private void getProgress(String t) {
        index1 = sp.getInt(t + "index", 0);
    }

    private void saveProgress(String t) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(t + "index", index);
        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        //初始化收藏按钮
        imgCollect = findViewById(R.id.img_collect);
        imgCollect.setOnClickListener(this);
        //初始化答题卡按钮
        imgCard = findViewById(R.id.img_card);
        imgCard.setOnClickListener(this);

        findViewById(R.id.mytime).setVisibility(View.GONE);

        //获取题目集关键字
        String field = QuestionFragment.field;
        String value = QuestionFragment.value;
        source = value;
        //设置标题
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(source);
        //获取SQLite数据库中题库数据
        if (tab == MyTag.QUE)
            cursor = ToolHelper.loadDB(this,
                    "select que.* from " + table + " where " + field + "='" + value + "' order by type");
        else
            cursor = ToolHelper.loadDB(this,
                    "select que.* from " + table + " and " + field + "='" + value + "' order by type");
        num = cursor.getCount();
        //答案List初始化
        anList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            anList.add("");
        }

        //设置进度条
        pb = findViewById(R.id.pb);
        pb.setMax(num - 1);
        pb.setProgress(0);
        //前后按钮
        imgPre = findViewById(R.id.img_pre);
        imgNext = findViewById(R.id.img_next);
        imgPre.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        //设置ViewFlipper
        vf = findViewById(R.id.vf);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return num;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                index = position;
                createView(position);
                return root;
            }
        };
        vf.setAdapter(adapter);
        moveToItem(index1);
    }

    //答题卡设置
    @SuppressLint("SetTextI18n")
    private void createView(int pos) {
        root = LayoutInflater.from(PracticeActivity.this).inflate(R.layout.queitem, null);
        tvQue = root.findViewById(R.id.tv_que1);
        cb1 = root.findViewById(R.id.cb_choice1);
        cb2 = root.findViewById(R.id.cb_choice2);
        cb3 = root.findViewById(R.id.cb_choice3);
        cb4 = root.findViewById(R.id.cb_choice4);
        tvAns = root.findViewById(R.id.tv_answer1);
        tvDetail = root.findViewById(R.id.tv_detail1);

        //获取数据
        cursor.moveToPosition(pos);
        type = cursor.getString(cursor.getColumnIndex("type"));
        que = cursor.getString(cursor.getColumnIndex("que"));
        A = "A." + cursor.getString(cursor.getColumnIndex("choiceA"));
        B = "B." + cursor.getString(cursor.getColumnIndex("choiceB"));
        C = "C." + cursor.getString(cursor.getColumnIndex("choiceC"));
        D = "D." + cursor.getString(cursor.getColumnIndex("choiceD"));
        answer = cursor.getString(cursor.getColumnIndex("answer"));
        detail = cursor.getString(cursor.getColumnIndex("detail"));
        qid = cursor.getString(cursor.getColumnIndex("_id"));
        //加载内容
        tvQue.setText((pos + 1) + ".(" + type + ")" + que);
        cb1.setText(A);
        cb2.setText(B);
        cb3.setText(C);
        cb4.setText(D);
        cb1.setButtonDrawable(R.drawable.cb);
        cb2.setButtonDrawable(R.drawable.cb);
        cb3.setButtonDrawable(R.drawable.cb);
        cb4.setButtonDrawable(R.drawable.cb);
        cb1.setEnabled(true);
        cb2.setEnabled(true);
        cb3.setEnabled(true);
        cb4.setEnabled(true);
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
        tvAns.setText("【正确答案】" + answer);
        tvDetail.setText("【解析】" + detail);
        if (anList.get(pos).equals("")) {
            tvAns.setVisibility(View.GONE);
            tvDetail.setVisibility(View.GONE);
        } else {
            //已答题设置为不可操作
            disableChecked(pos);
        }
        //设置当前进度
        pb.setProgress(pos);
        //设置是否被收藏
        if (queCollect()) {
            isCollect = true;
            imgCollect.setImageResource(R.drawable.star_on);
        } else {
            isCollect = false;
            imgCollect.setImageResource(R.drawable.star1);
        }
        //滑动切换
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startX = v.getWidth() / 2, endX = v.getWidth() / 2, min = 100;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        break;
                }
                if (startX - endX > min) {
                    vf.showNext();
                } else if (endX - startX > min) {
                    vf.showPrevious();
                }
                return true;
            }
        });

    }

    //判断选择答案对错
    private void isAnswerTrue(int pos) {
        if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked() || cb4.isChecked()) {
            //获取答案
            StringBuffer sb = new StringBuffer();
            if (cb1.isChecked()) sb.append("A");
            if (cb2.isChecked()) sb.append("B");
            if (cb3.isChecked()) sb.append("C");
            if (cb4.isChecked()) sb.append("D");
            String you = sb.toString();
            //保存答案
            anList.set(pos, you);
            // 判断对错
            // 答案正确且当前练习题目为两题或以上
            if (you.equals(answer) && num > 1) {
                moveCorrect();
            }
            // 答案正确且当前练习题目为最后一题
            else if (you.equals(answer) && num == 1) {
                int c = ToolHelper.loadDB(this, "select _id from wrong where qid=" + qid + " and uid = " + UserInfo.userID).getCount();
                if (c > 0)
                    ToolHelper.excuteDB(this, "delete from wrong where qid=" + qid + " and uid = " + UserInfo.userID);
                disableChecked(pos);
            } else {
                // 错误则保存错题，显示答案
                saveWrong(sb.toString());
                disableChecked(pos);
            }
        } else {
            Toast.makeText(PracticeActivity.this, "请选择答案", Toast.LENGTH_SHORT).show();
        }
    }

    //移除正确题目
    @SuppressLint("SetTextI18n")
    private void moveCorrect() {
        vf.showNext();
        int c = ToolHelper.loadDB(this, "select _id from wrong where qid=" + qid + " and uid = " + UserInfo.userID).getCount();
        if (c > 0)
            ToolHelper.excuteDB(this, "delete from wrong where qid=" + qid + " and uid = " + UserInfo.userID);
    }

    //已做题不可再做
    private void disableChecked(int pos) {
        tvAns.setVisibility(View.VISIBLE);
        tvDetail.setVisibility(View.VISIBLE);

        if (answer.contains("A")) cb1.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("B")) cb2.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("C")) cb3.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("D")) cb4.setButtonDrawable(R.drawable.cb_right);
    }

    //保存错题
    private void saveWrong(String ans) {
        int c = ToolHelper.loadDB(this, "select _id from wrong where qid=" + qid + " and uid = " + UserInfo.userID).getCount();
        if (c == 0) {
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String mydate = ft.format(date);
            ToolHelper.excuteDB(this,  // values("+null+","+6+","+27+",'"+C+"','"2022-05-19 20:57:43')
                    "insert into wrong (_id,qid,uid,answer,anTime) values (" + null + "," + qid + "," + UserInfo.userID + ",'" + ans + "','" + mydate + "')");
        }
    }

    //判断当前题目是否被收藏
    private boolean queCollect() {
        int c = ToolHelper.loadDB(this, "select _id from collection where qid=" + qid + " and uid = " + UserInfo.userID).getCount();
        return c > 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.que, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.que_ok://提交答案
                isAnswerTrue(index);
                break;
            case android.R.id.home://返回
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否退出练习？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveProgress(from);
                        PracticeActivity.this.finish();
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_next:
                vf.showNext();
                break;
            case R.id.img_pre:
                vf.showPrevious();
                break;
            case R.id.img_collect://收藏
                if (!isCollect) {
                    imgCollect.setImageResource(R.drawable.star_on);  // values (null","+5+","+27+")
                    ToolHelper.excuteDB(this, "insert into collection (_id,qid,uid) values (" + null + "," + qid + "," + UserInfo.userID + ")");
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                    isCollect = true;
                } else {
                    imgCollect.setImageResource(R.drawable.star1);
                    ToolHelper.excuteDB(this, "delete from collection where qid=" + qid + " and uid = " + UserInfo.userID);
                    Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
                    isCollect = false;
                }
                break;
            case R.id.img_card:
                Intent intent = new Intent(this, com.example.bigblackbox.CardActivity.class);
                intent.putExtra("num", num);
                intent.putExtra("from", 2);
                startActivityForResult(intent, MyTag.CARD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyTag.CARD && resultCode == MyTag.CARD) {
            int select = data.getIntExtra("card", 0);
            moveToItem(select);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void moveToItem(int t) {
        if (t != index) {
            if (t > index) {
                int d = t - index;
                for (int i = 0; i < d; i++)
                    vf.showNext();
            } else {
                int p = index - t;
                for (int i = 0; i < p; i++)
                    vf.showPrevious();
            }
        }
    }
}
