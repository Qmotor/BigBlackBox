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
import com.example.bigblackbox.entity.Profession;

import java.util.List;

public class ProfessionAdapter extends BaseAdapter {
    private final List<Profession> mProfession;
    private final LayoutInflater mInflater;

    public ProfessionAdapter(Context context, List<Profession> profession) {
        this.mProfession = profession;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mProfession.size();
    }

    @Override
    public Object getItem(int position) {
        return mProfession.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_profession, parent, false);
        } else {
            v = convertView;
        }
        Profession profession = mProfession.get(position);
        if(profession.getProFace() != null) {
            Bitmap bmpOut = BitmapFactory.decodeByteArray(profession.getProFace(), 0, profession.getProFace().length);
            ((ImageView) v.findViewById(R.id.pPic)).setImageBitmap(bmpOut);
        }else {
            @SuppressLint("SdCardPath") Bitmap bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/noPic.png");
            ((ImageView)v.findViewById(R.id.pPic)).setImageBitmap(bmp);
        }
        ((TextView)v.findViewById(R.id.pTitle)).setText(profession.getProTitle());
        return v;
    }
}
