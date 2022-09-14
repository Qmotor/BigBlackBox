package com.example.bigblackbox.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.bigblackbox.PracticeActivity;
import com.example.bigblackbox.QuestionActivity;
import com.example.bigblackbox.R;
import com.example.bigblackbox.tool.MyTag;
import com.example.bigblackbox.tool.ToolHelper;
import com.example.bigblackbox.tool.UserInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


@SuppressLint("ValidFragment")
public class QuestionFragment extends Fragment implements View.OnClickListener {
    private int tab;
    private boolean tag = false;
    private View rootView, itemView = null;
    private ListView lv;
    private String table, content;
    public static String field, value;
    private TextView tv1, tv2, info;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private static boolean flagTest = false;
    private static boolean flagPrac = false;
    private FloatingActionButton fabTest, fabPrac;

    @SuppressLint("ValidFragment")
    public QuestionFragment(int tab) {
        this.tab = tab;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_question, container, false);
        tv1 = rootView.findViewById(R.id.top_source);
        tv2 = rootView.findViewById(R.id.top_kind);
        info = rootView.findViewById(R.id.tv_info1);
        info.setText("无内容");
        tv1.setText("来源");
        tv2.setText("分类");
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        lv = rootView.findViewById(R.id.lv_que);
        fabTest = rootView.findViewById(R.id.fab_test);
        fabTest.setOnClickListener(this);
        fabPrac = rootView.findViewById(R.id.fab_prac);
        fabPrac.setOnClickListener(this);
        return rootView;
    }

    //加载来源题库
    private void tab1() {
        tv1.setTextColor(getResources().getColor(R.color.colorAccent));
        tv2.setTextColor(getResources().getColor(R.color.gray));
        queList("source");
    }

    //加载分类题库
    private void tab2() {
        tv1.setTextColor(getResources().getColor(R.color.gray));
        tv2.setTextColor(getResources().getColor(R.color.colorAccent));
        queList("kind");
    }

    private void loadData() {
        switch (tab) {
            case MyTag.QUE://题库
                table = "que";
                content = "题库";
                flagTest = true;
                flagPrac = true;
                break;
            case MyTag.COLLECT://收藏
                table = "collection ,que where collection.qid=que._id and uid =" + UserInfo.userID;
                fabTest.setVisibility(View.GONE);
                fabPrac.setVisibility(View.GONE);
                content = "收藏";
                flagTest = false;
                flagPrac = false;
                break;
            case MyTag.WRONG://错题
                table = "wrong,que where wrong.qid=que._id and uid =" + UserInfo.userID;
                fabTest.setVisibility(View.GONE);
                fabPrac.setVisibility(View.GONE);
                content = "错题";
                flagTest = false;
                flagPrac = false;
                break;
        }
    }

    //加载内容到列表
    private void queList(final String type) {
        cursor = ToolHelper.loadDB(getActivity(), "select que._id, que." + type + ",count(que._id) as num from " + table + " group by que." + type + " order by source desc");
        if (cursor.getCount() > 0) {
            if (!tag) {
                adapter = new SimpleCursorAdapter(getActivity(), R.layout.listitem, cursor,
                        new String[]{type, "num"}, new int[]{R.id.tv_item, R.id.tv_item1},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = view.findViewById(R.id.tv_item);
                        if(flagTest || flagPrac){
                            if (itemView != null) {
                                TextView tv1 = itemView.findViewById(R.id.tv_item);
                                tv1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            }
                            tv.setTextColor(getResources().getColor(R.color.colorAccent));
                            itemView = view;
                            field = type; // type的值为source或kind
                            value = cursor.getString(cursor.getColumnIndex(type));
                        }else { // 从收藏或错题板块进入，默认以练习模式开始答题
                            itemView = view;
                            field = type;
                            value = cursor.getString(cursor.getColumnIndex(type));
                            askDialog("练习：", 1);
                        }
                    }
                });
                info.setVisibility(View.GONE);
            } else {
                adapter.notify();
            }
        } else {
            lv.setVisibility(View.GONE);
            info.setVisibility(View.VISIBLE);
            info.setText("无记录");
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_test:
                if (itemView != null)
                    askDialog("考试：", 2);
                else
                    Toast.makeText(getActivity(), "请选择题目集", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_prac:
                if (itemView != null)
                    askDialog("练习：", 1);
                else
                    Toast.makeText(getActivity(), "请选择题目集", Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_source:
                tab1();
                break;
            case R.id.top_kind:
                tab2();
                break;
        }
    }

    private void askDialog(String str, final int c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(str + "(" + content + ")" + value + "?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                if (c == 2) {
                    intent = new Intent(getActivity(), QuestionActivity.class);
                } else {
                    intent = new Intent(getActivity(), PracticeActivity.class);
                }
                intent.putExtra("tab", tab);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public void onResume() {
        // 重写onResume()方法，保持显示数据常新
        super.onResume();
        loadData();
        tab1();
    }
}
