package com.example.bigblackbox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigblackbox.R;
import com.example.bigblackbox.tool.UserInfo;
import com.example.bigblackbox.entity.Reply;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReplyAdapter extends BaseAdapter {
    private final List<Reply> mReply;
    private final LayoutInflater mInflater;
    Context context;
    public ReplyAdapter(Context context, List<Reply> reply) {
        this.mReply = reply;
        this.context = context;
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

    @SuppressLint({"SetTextI18n", "SdCardPath"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_reply, parent, false);
        } else {
            v = convertView;
        }

        Reply reply = mReply.get(position);
        ((TextView)v.findViewById(R.id.replyContent)).setText(reply.getReplyContent());
        // 判断评论人的身份并给予标签，优先级为 楼主 > 管理员 > 普通用户
        if(reply.getReplyName().equals(UserInfo.isLord) && reply.getPusherIdentity() == 1){
            ((TextView)v.findViewById(R.id.replyUser)).setText(Html.fromHtml(reply.getReplyName() + "<font color=\"#96B6C5\">（楼主）</font>" + "<font color=\"#FF0000\">（管理员）</font>"));
        }else if(reply.getPusherIdentity() == 1){
        ((TextView)v.findViewById(R.id.replyUser)).setText(Html.fromHtml(reply.getReplyName() + "<font color=\"#FF0000\">（管理员）</font>"));
        } else if(reply.getReplyName().equals(UserInfo.isLord)){
            ((TextView)v.findViewById(R.id.replyUser)).setText(Html.fromHtml(reply.getReplyName() + "<font color=\"#96B6C5\">（楼主）</font>"));
        }else {
            ((TextView)v.findViewById(R.id.replyUser)).setText(reply.getReplyName());
        }

        @SuppressLint("SdCardPath") Bitmap bmp;
        if(reply.getReplyUserGender().equals("男")){
            bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/boy.png");
        }else {
            bmp = BitmapFactory.decodeFile("/data/data/com.example.bigblackbox/pic/girl.png");
        }
        ((ImageView)v.findViewById(R.id.replyIcon)).setImageBitmap(bmp);

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pDate = ft.parse(reply.getReplyTime());
            Date nDate = new Date(System.currentTimeMillis());
            assert pDate != null;
            long diff = nDate.getTime() - pDate.getTime();// 这样得到的差值是毫秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            if (days == 0 && hours == 0 && minutes == 0) {
                ((TextView)v.findViewById(R.id.replyTime)).setText((diff / 1000) + "秒前");
            } else if (days == 0 && hours == 0) {
                ((TextView)v.findViewById(R.id.replyTime)).setText(minutes + "分钟前");
            } else if (days == 0) {
                ((TextView)v.findViewById(R.id.replyTime)).setText(hours + "小时前");
            } else if (days <= 5) {
                ((TextView)v.findViewById(R.id.replyTime)).setText(days + "天前");
            } else {
                ((TextView)v.findViewById(R.id.replyTime)).setText(reply.getReplyTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return v;
    }
}
