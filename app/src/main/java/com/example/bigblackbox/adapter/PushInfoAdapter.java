package com.example.bigblackbox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.bigblackbox.R;
import com.example.bigblackbox.entity.PushInfo;

import java.util.List;
import java.util.Random;

public class PushInfoAdapter extends BaseAdapter {

    private final List<PushInfo> mPushInfo;
    private final LayoutInflater mInflater;

    public PushInfoAdapter(Context context, List<PushInfo> pushInfo) {
        this.mPushInfo = pushInfo;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mPushInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mPushInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_pushinfo, parent, false);
        } else {
            v = convertView;
        }
        PushInfo pushInfo = mPushInfo.get(position);
        /*
        将数据库查询结果显示在相应的TextView中
         */
        if(pushInfo.getPushIcon() == 1){
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/news.png");
            ((ImageView)v.findViewById(R.id.pushPic)).setImageBitmap(bmp);
        }else if(pushInfo.getPushIcon() == 2){
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/hot.png");
            ((ImageView)v.findViewById(R.id.pushPic)).setImageBitmap(bmp);
        }else {
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/pInfo.png");
            ((ImageView)v.findViewById(R.id.pushPic)).setImageBitmap(bmp);
        }

        ((TextView) v.findViewById(R.id.title)).setText(pushInfo.getPushTitle());
        ((TextView) v.findViewById(R.id.time)).setText(pushInfo.getPushTime());
        return v;
    }
}
