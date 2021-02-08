package com.example.bigblackbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bigblackbox.R;
import com.example.bigblackbox.entity.Posting;

import java.util.List;

public class PostingAdapter extends BaseAdapter {
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
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_posting, parent, false);
        } else {
            v = convertView;
        }
        Posting posting = mPosting.get(position);
        /*
        将数据库查询结果显示在相应的TextView中
         */
        ((TextView)v.findViewById(R.id.postTitle)).setText(posting.getTitle());
        ((TextView)v.findViewById(R.id.userName)).setText(posting.getName());
        ((TextView)v.findViewById(R.id.postTime)).setText(posting.getTime());
        ((TextView)v.findViewById(R.id.postContent)).setText(posting.getContent());
        return v;
    }
}
