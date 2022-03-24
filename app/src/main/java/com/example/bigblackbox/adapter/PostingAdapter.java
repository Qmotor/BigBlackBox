package com.example.bigblackbox.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bigblackbox.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.entity.Posting;

import java.util.List;

public class PostingAdapter extends BaseAdapter {
    private static final String TAG = "test";
    private final List<Posting> mPosting;
    private final LayoutInflater mInflater;

    public PostingAdapter(Context context, List<Posting> posting) {
        this.mPosting = posting;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPosting.size();
    }

    @Override
    public Object getItem(int position) {
        return mPosting.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        Posting posting = mPosting.get(position);
//        Log.d(TAG, "getView: position = " + position);
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_posting, parent, false);
        } else {
            v = convertView;
        }
        /*
        listView滑动机制：listView被滑动时，移出视图的item会被缓存下来，下面的item会复用移出的item的样式，listview的item长度一般为10
        listview的视图样式也需要设置为“match_parent”或“fill_parent”，防止出现数据重复
        解决方法是：不满足if条件时也进行相应设置。这样每次getView就会重新设置，不会加载之前的缓存
         */
        if(posting.getPosterIdentity() == 1) {
            ((TextView)v.findViewById(R.id.showUserName)).setTextColor(Color.parseColor("#FF0000"));
            ((TextView)v.findViewById(R.id.postTitle)).setText(posting.getTitle());
            ((TextView)v.findViewById(R.id.showUserName)).setText(posting.getName() + "（管理员）");
        }else {
            ((TextView)v.findViewById(R.id.showUserName)).setTextColor(0x8A000000);
            ((TextView)v.findViewById(R.id.postTitle)).setText(posting.getTitle());
            ((TextView)v.findViewById(R.id.showUserName)).setText(posting.getName());
        }
        ((TextView)v.findViewById(R.id.postTime)).setText(posting.getTime());
        ((TextView)v.findViewById(R.id.postContent)).setText(posting.getContent());
        return v;
    }
}
