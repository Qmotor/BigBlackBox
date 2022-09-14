package com.example.bigblackbox.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.ExamActivity;
import com.example.bigblackbox.R;
import com.example.bigblackbox.fragment.QuestionFragment;

public class Examination extends AppCompatActivity implements View.OnClickListener{
    private TextView tv1, tv2, tv3, tv4;
    private Fragment fragment;
    private final int QUE = 1, COLLECT = 2, WRONG = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        tv1 = findViewById(R.id.tv_quest);
        tv1.setOnClickListener(this);
        tv2 = findViewById(R.id.tv_collect);
        tv2.setOnClickListener(this);
        tv3 = findViewById(R.id.tv_wrong);
        tv3.setOnClickListener(this);
        tv4 = findViewById(R.id.tv_record);
        tv4.setOnClickListener(this);
        setDefaultFragment();
    }

    //设置默认fragment
    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        QuestionFragment questionFragment = new QuestionFragment(QUE);
        transaction.replace(R.id.content, questionFragment);
        transaction.commit();
        tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv2.setTextColor(getResources().getColor(R.color.gray));
        tv3.setTextColor(getResources().getColor(R.color.gray));
        tv4.setTextColor(getResources().getColor(R.color.gray));
    }

    //点击下栏切换界面
    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        fragment = getFragmentManager().findFragmentById(R.id.content);
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_quest://题库
                QuestionFragment questionFragment = new QuestionFragment(QUE);
                transaction.hide(fragment).replace(R.id.content, questionFragment);
                tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv2.setTextColor(getResources().getColor(R.color.gray));
                tv3.setTextColor(getResources().getColor(R.color.gray));
                tv4.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.tv_collect://收藏
                QuestionFragment questionFragment1 = new QuestionFragment(COLLECT);
                transaction.hide(fragment).replace(R.id.content, questionFragment1);
                tv1.setTextColor(getResources().getColor(R.color.gray));
                tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv3.setTextColor(getResources().getColor(R.color.gray));
                tv4.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.tv_wrong://错题
                QuestionFragment questionFragment2 = new QuestionFragment(WRONG);
                transaction.hide(fragment).replace(R.id.content, questionFragment2);
                tv1.setTextColor(getResources().getColor(R.color.gray));
                tv2.setTextColor(getResources().getColor(R.color.gray));
                tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv4.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.tv_record://错题
                Intent intent = new Intent(this, ExamActivity.class);
                startActivity(intent);
                break;
        }
        transaction.commit();
    }
}
