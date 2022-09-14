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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.AddPost;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.Search;
import com.example.bigblackbox.tool.UserInfo;
import com.example.bigblackbox.adapter.PostingAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.Post_detail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AllNews extends Fragment {
    private ImageView sb, sb1, sb2;
    private String name;
    public static int count = 0;
    private SQLiteDatabase mDB;
    private PostingAdapter mPostingAdapter;
    static String[] items;
    private final List<Posting> p = new ArrayList<>();
    public List<Integer> list = new LinkedList<>();


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

        return inflater.inflate(R.layout.fragment_all_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sb = view.findViewById(R.id.speakBtn);
        sb1 = view.findViewById(R.id.search_Btn);
        sb2 = view.findViewById(R.id.sort_Btn);
        final ListView listView = view.findViewById(R.id.allNewsList);
        mPostingAdapter = new PostingAdapter(requireContext(), p);
        listView.setAdapter(mPostingAdapter);

        // 列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 根据用户点击的帖子，在Posting实体类中获取相应的帖子ID，并将该值传入Post_detail中
                Posting posting = p.get(position);
                Intent intent = new Intent(getContext(), Post_detail.class);
                intent.putExtra("postID", posting.getPostID());
                startActivity(intent);
            }
        });


        // 列表长按事件
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
                                @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from coll_post where post_id = ?", new String[]{String.valueOf(posting.getPostID())});
                                amount = c.getCount();
                                if (amount == 0) {
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = new Date(System.currentTimeMillis());
                                    mDB.execSQL("insert into coll_post values(null,?,?,?)",
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


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当ListView不滚动时，悬浮按钮状态为可见
                    case 0:
                        sb.setVisibility(View.VISIBLE);
                        sb1.setVisibility(View.VISIBLE);
                        sb2.setVisibility(View.VISIBLE);
                        break;
                    // 当ListView滚动时，悬浮按钮状态为隐藏
                    case 1:
                    case 2:
                        sb.setVisibility(View.GONE);
                        sb1.setVisibility(View.GONE);
                        sb2.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPost.class);
                startActivity(intent);
            }
        });
        sb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Search.class);
                startActivity(intent);
            }
        });
        sb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("设置帖子显示顺序");
                if (UserInfo.flag) {
                    items = new String[]{"发布时间", "回复时间（当前）"};
                } else {
                    items = new String[]{"发布时间（当前）", "回复时间"};
                }
                builder.setNegativeButton("取消", null);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 发布时间
                                UserInfo.flag = false;
                                sb2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.reversesort));
                                showPostData();
                                break;
                            case 1: // 回复时间
                                UserInfo.flag = true;
                                sb2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.sort));
                                flashData();
                                showPostTimeData();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    private void flashData() {
        list.clear();         // 清除List中的数据，防止数据显示出错
        /*
                1、数据不能实时刷新，尚未找到原因(已解决)
                2、存在没有评论的帖子，需要其它方法将这些帖子显示出来
                2的解决方法（可能）：将所有的帖子编号查出来放在list中，与有评论的帖子做判断
                有相同数据的删除，剩下的就是没有评论的帖子，再按发布时间倒序排序即可
                2存在的问题：不知道怎么将有评论的帖子和没有评论的帖子“缝”起来，使之成为一个整体
         */
        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from replying order by reply_time desc", new String[0]);
        while (cursor.moveToNext()) {
            if (count == 0) {
                list.add(cursor.getInt(1));
                count++;
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (cursor.getInt(1) != list.get(i)) {
                        list.add(cursor.getInt(1));
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        count = 0;
    }

    private void showPostTimeData() {  // 回复时间
        p.clear();                //清除List中的数据，防止数据显示出错
        for (int c = 0; c < list.size(); c++) {
            @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from posting where post_id = ?", new String[]{String.valueOf(list.get(c))});
            while (cursor.moveToNext()) {
                p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
            }
        }
        //通知观察者数据已经变更
        mPostingAdapter.notifyDataSetChanged();
    }

    private void showPostData() {      // 发布时间
        p.clear();                //清除List中的数据，防止数据显示出错
        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from posting order by post_time desc", new String[0]);
        while (cursor.moveToNext()) {
            p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
        }
        //通知观察者数据已经变更
        mPostingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        // 重写onResume()方法，保持显示数据常新
        super.onResume();
        if (UserInfo.flag) {
            flashData();
            showPostTimeData();
        } else {
            showPostData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserInfo.flag = true;
    }
}
