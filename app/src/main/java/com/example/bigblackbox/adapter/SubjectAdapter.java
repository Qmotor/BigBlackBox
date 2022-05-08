package com.example.bigblackbox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigblackbox.R;
import com.example.bigblackbox.entity.Subject;
import com.example.bigblackbox.entity.Teacher;

import java.util.List;

public class SubjectAdapter extends BaseAdapter {
    private final List<Subject> mSubject;
    private final LayoutInflater mInflater;

    public SubjectAdapter(Context context, List<Subject> subject) {
        this.mSubject = subject;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSubject.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_subject, parent, false);
        } else {
            v = convertView;
        }
        Subject subject = mSubject.get(position);
        if(subject.getFollChoose() == 1){
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/math1.png");
            ((ImageView)v.findViewById(R.id.sPic)).setImageBitmap(bmp);
        }else if(subject.getFollChoose() == 2){
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/test2.png");
            ((ImageView)v.findViewById(R.id.sPic)).setImageBitmap(bmp);
        }else {
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/test3.png");
            ((ImageView)v.findViewById(R.id.sPic)).setImageBitmap(bmp);
        }
        ((TextView)v.findViewById(R.id.sTitle)).setText(subject.getSubTitle());
        return v;
    }
}
