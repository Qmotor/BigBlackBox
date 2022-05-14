package com.example.bigblackbox.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.tool.UserInfo;
import com.example.bigblackbox.adapter.PostingAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.Post_detail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecondHandInfo extends Fragment {
    private String name;
    private PostingAdapter mPostingAdapter;
    private SQLiteDatabase mDB;
    private List<Posting> p = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(getContext());
        mDB = mHelper.getReadableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_info_sell, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView listView = view.findViewById(R.id.infoSellList);

        mPostingAdapter = new PostingAdapter(requireContext(), p);
        listView.setAdapter(mPostingAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                根据用户点击的帖子，从实体类获取相应的帖子编号，并将该值传入Post_detail中
                 */
                Posting posting = p.get(position);
                Intent intent = new Intent(getContext(), Post_detail.class);
                intent.putExtra("postID", posting.getPostID());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Posting posting = p.get(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("提示");
                String[] items = {"删除帖子", "收藏帖子"};
                builder.setNegativeButton("取消", null);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 删除帖子
                                builder.setMessage("您确定要删除该帖子吗？");
                                builder.setPositiveButton("我手滑了0_o", null);
                                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from posting where post_id = ?",
                                                new String[]{String.valueOf(posting.getPostID())});
                                        if (c.moveToNext()) {
                                            name = c.getString(1);
                                        }
                                        if (name.equals(UserInfo.userName) || UserInfo.isAdmin.equals("1")) {
                                            // 删除操作会将帖子及帖子下的所以评论删除
                                            mDB.execSQL("delete from posting where post_id = ?",
                                                    new String[]{String.valueOf(posting.getPostID())});
                                            mDB.execSQL("delete from replying where reply_post_id = ?",
                                                    new String[]{String.valueOf(posting.getPostID())});
                                            Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                            requireActivity().onBackPressed();
                                            Intent intent = new Intent(getActivity(), com.example.bigblackbox.activity.Chat.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getContext(), "权限不足!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.create().show();
                                break;
                            case 1: // 收藏帖子
                                int amount;
                                @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from collection where post_id = ?", new String[]{String.valueOf(posting.getPostID())});
                                amount = c.getCount();
                                if (amount == 0) {
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = new Date(System.currentTimeMillis());
                                    mDB.execSQL("insert into collection values(null,?,?,?)",
                                            new String[]{UserInfo.userID, String.valueOf(posting.getPostID()), simpleDateFormat.format(date)});
                                    Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "已收藏该帖子，不能重复收藏", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
    private void showSellData(){
        p.clear();           //清空List中的数据，防止数据多次显示
            try (Cursor cursor = mDB.rawQuery("select * from posting where post_follow = 3 order by post_time desc", new String[0])) {
                while (cursor.moveToNext()) {
                    p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
                }
            }
        mPostingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        /*
        重写onResume()方法，保持显示数据常新
         */
        super.onResume();
        showSellData();
    }
}
