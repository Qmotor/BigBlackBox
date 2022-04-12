package com.example.bigblackbox.adapter;

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
import com.example.bigblackbox.entity.Teacher;

import java.util.List;

public class TeacherAdapter extends BaseAdapter {
    private final List<Teacher> mTeacher;
    private final LayoutInflater mInflater;

    public TeacherAdapter(Context context, List<Teacher> teacher) {
        this.mTeacher = teacher;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mTeacher.size();
    }

    @Override
    public Object getItem(int position) {
        return mTeacher.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_teacher, parent, false);
        } else {
            v = convertView;
        }
        Teacher teacher = mTeacher.get(position);
        /*
             byte[] 转 bitmap
             SQLite: byte[]
             ImageView: bitmap
         */
        Bitmap bmpOut= BitmapFactory.decodeByteArray(teacher.getTeacherIcon(),0,teacher.getTeacherIcon().length);
        ((ImageView)v.findViewById(R.id.iv)).setImageBitmap(bmpOut);
        ((TextView)v.findViewById(R.id.teacherName)).setText(teacher.getTeacherName());
        return v;
    }
}
