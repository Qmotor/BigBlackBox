package com.example.bigblackbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bigblackbox.tool.CharacterUtils;
import com.example.bigblackbox.R;
import com.example.bigblackbox.entity.School;

import java.util.List;

public class SchoolAdapter extends BaseAdapter {
    private final List<School> mSchool;
    private final LayoutInflater mInflater;

    public SchoolAdapter(Context context, List<School> school) {
        this.mSchool = school;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSchool.size();
    }

    @Override
    public Object getItem(int position) {
        return mSchool.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        School school = mSchool.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_user, parent, false);
        }
        ViewHolder holder = getViewHolder(convertView);
        if (position == 0) {
            //第一个数据要显示字母和姓名
            holder.tv_firstCharacter.setVisibility(View.VISIBLE);
            holder.tv_firstCharacter.setText(school.getFirstCharacter());
        } else {
            //其他数据判断是否为同个字母，这里使用Ascii码比较大小
            if (CharacterUtils.getCnAscii(mSchool.get(position - 1).getFirstCharacter().charAt(0)) <
                    CharacterUtils.getCnAscii(school.getFirstCharacter().charAt(0))) {
                //后面字母的值大于前面字母的值，需要显示字母
                holder.tv_firstCharacter.setVisibility(View.VISIBLE);
                holder.tv_firstCharacter.setText(school.getFirstCharacter());
            } else {
                //后面字母的值等于前面字母的值，不显示字母
                holder.tv_firstCharacter.setVisibility(View.GONE);
            }
        }
        holder.tv_name.setText(school.getSchoolName());
        return convertView;
    }

    private ViewHolder getViewHolder(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    /**
     * 控件管理类
     */
    private static class ViewHolder {
        private final TextView tv_firstCharacter;
        private final TextView tv_name;
        ViewHolder(View view) {
            tv_firstCharacter = (TextView) view.findViewById(R.id.tv_firstCharacter);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }

    /**
     * 通过字符查找位置
     *
     * @param s
     * @return i
     */
    public int getSelectPosition(String s) {
        for (int i = 0; i < getCount(); i++) {
            String firChar = mSchool.get(i).getFirstCharacter();
            if (firChar.equals(s)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 通过名称查找位置
     *
     * @param s
     * @return i
     */
    public int getSearchPosition(String s) {
        for (int i = 0; i < getCount(); i++) {
            String name = mSchool.get(i).getSchoolName();
            if (name.equals(s)) {
                return i;
            }
        }
        return -1;
    }
}
