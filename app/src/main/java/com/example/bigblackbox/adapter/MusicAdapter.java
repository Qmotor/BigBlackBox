package com.example.bigblackbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bigblackbox.R;
import com.example.bigblackbox.entity.Music;

import java.util.List;

public class MusicAdapter extends BaseAdapter {
    private final List<Music> mMusic;
    private final LayoutInflater mInflater;

    public MusicAdapter(Context context, List<Music> music) {
        this.mMusic = music;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mMusic.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusic.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        Music music = mMusic.get(position);
        if (convertView == null) {
            v = mInflater.inflate(R.layout.list_music, parent, false);
        } else {
            v = convertView;
        }
        ((TextView)v.findViewById(R.id.mTitle)).setText(music.getMusicName());
        ((TextView)v.findViewById(R.id.authorName)).setText(music.getMusicAuthor());
        return v;
    }
}
