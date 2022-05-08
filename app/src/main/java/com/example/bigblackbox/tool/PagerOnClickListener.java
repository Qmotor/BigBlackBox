package com.example.bigblackbox.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.bigblackbox.Post_detail;
import com.example.bigblackbox.Push_detail;
import com.example.bigblackbox.R;
import com.example.bigblackbox.SecurityQuestion;
import com.example.bigblackbox.Teacher_detail;

public class PagerOnClickListener implements View.OnClickListener{
    Context mContext;
    public PagerOnClickListener(Context mContext){
        this.mContext=mContext;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.pager_img1:
                intent = new Intent(mContext, Teacher_detail.class);
                intent.putExtra("teacherID", 101);
                mContext.startActivity(intent);
                break;
            case R.id.pager_img2:
                intent = new Intent(mContext, Push_detail.class);
                intent.putExtra("pushInfoID", 10011);
                mContext.startActivity(intent);
                break;
            case R.id.pager_img3:
                intent = new Intent(mContext, Push_detail.class);
                intent.putExtra("pushInfoID", 10020);
                mContext.startActivity(intent);
                break;
            case R.id.pager_img4:
                intent = new Intent(mContext, Post_detail.class);
                intent.putExtra("postID", 149);
                mContext.startActivity(intent);
                break;
            case R.id.pager_img5:
                intent = new Intent(mContext, SecurityQuestion.class);
                mContext.startActivity(intent);
                break;
        }
    }
}
