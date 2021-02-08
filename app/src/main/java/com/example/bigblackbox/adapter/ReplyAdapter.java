package com.example.bigblackbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bigblackbox.R;
import com.example.bigblackbox.entity.Reply;

import java.util.List;

public class ReplyAdapter extends BaseAdapter {
    private final List<Reply> mReply;
    private final LayoutInflater mInflater;

    public ReplyAdapter(Context context, List<Reply> reply) {
        this.mReply = reply;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mReply.size();
    }

    @Override
    public Object getItem(int position) {
        return mReply.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_reply, parent, false);
        } else {
            v = convertView;
        }
        Reply reply = mReply.get(position);
        /*
        将数据库查询结果显示在相应的TextView中
         */
        ((TextView)v.findViewById(R.id.replyContent)).setText(reply.getReplyContent());
        ((TextView)v.findViewById(R.id.replyUser)).setText(reply.getReplyName());
        ((TextView)v.findViewById(R.id.replyTime)).setText(reply.getReplyTime());
        return v;
    }
}
