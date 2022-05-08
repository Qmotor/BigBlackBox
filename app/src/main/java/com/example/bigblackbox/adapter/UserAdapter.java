package com.example.bigblackbox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigblackbox.R;
import com.example.bigblackbox.entity.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private final List<User> mUser;
    private final LayoutInflater mInflater;

    public UserAdapter(Context context, List<User> user) {
        this.mUser = user;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mUser.size();
    }

    @Override
    public Object getItem(int position) {
        return mUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        User user = mUser.get(position);
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_user, parent, false);
        } else {
            v = convertView;
        }
        if(user.getAdmin() == 1){         // 判断用户是否为管理员
            ((TextView)v.findViewById(R.id.listUname)).setTextColor(Color.parseColor("#FF0000"));
            ((TextView)v.findViewById(R.id.listUname)).setText(user.getUserName() + "（管理员）");
        }else {
            ((TextView)v.findViewById(R.id.listUname)).setTextColor(Color.parseColor("#000000"));
            ((TextView)v.findViewById(R.id.listUname)).setText(user.getUserName());
        }
        if(user.getUserIcon() != null) {  // 显示用户头像
            Bitmap bmpOut = BitmapFactory.decodeByteArray(user.getUserIcon(), 0, user.getUserIcon().length);
            ((ImageView)v.findViewById(R.id.listHead)).setImageBitmap(bmpOut);
        }else if(user.getUserGender().equals("男")){
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/boy.png");
            ((ImageView)v.findViewById(R.id.listHead)).setImageBitmap(bmp);
        }else {
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/girl.png");
            ((ImageView)v.findViewById(R.id.listHead)).setImageBitmap(bmp);
        }

        if(user.getBanned() == 1){        // 判断用户账号状态
            ((TextView)v.findViewById(R.id.userState)).setTextColor(Color.parseColor("#FF0000"));
            ((TextView)v.findViewById(R.id.userState)).setText("封禁中");
        }else {
            ((TextView)v.findViewById(R.id.userState)).setTextColor(0x8A000000);
            ((TextView)v.findViewById(R.id.userState)).setText("正常");
        }
        ((TextView)v.findViewById(R.id.listUID)).setText("UID：" + user.getUserID());
        return v;
    }
}
