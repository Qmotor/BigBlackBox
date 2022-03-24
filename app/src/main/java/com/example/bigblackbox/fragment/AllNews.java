package com.example.bigblackbox.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.AddPost;
import com.example.bigblackbox.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.Search;
import com.example.bigblackbox.UserInfo;
import com.example.bigblackbox.adapter.PostingAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.Post_detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllNews extends Fragment {
    private ImageView sb,sb1;
    private String name;
    private SQLiteDatabase mDB;
    private PostingAdapter mPostingAdapter;
    private TextView adminName;
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

        return inflater.inflate(R.layout.fragment_all_news, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sb = view.findViewById(R.id.speakBtn);
        sb1 = view.findViewById(R.id.search_Btn);
        adminName = view.findViewById(R.id.showUserName);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("提示");
                builder.setMessage("您确定要删除该帖子吗？");
                builder.setPositiveButton("我手滑了0_o", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        @SuppressLint("Recycle") Cursor c = mDB.rawQuery("select * from posting where postID = ?",
                                new String[]{String.valueOf(posting.getPostID())});
                        if(c.moveToNext()){
                            name = c.getString(1);
                        }
                        if(name.equals(UserInfo.userName) || UserInfo.isAdmin.equals("1")){
                            // 删除操作会将帖子及帖子下的所以评论删除
                            mDB.execSQL("delete from posting where postUserName = ?",
                                    new String[]{name});
                            mDB.execSQL("delete from postReply where reply_postID = ?",
                                    new String[]{String.valueOf(posting.getPostID())});
                            Toast.makeText(getContext(),"删除成功", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(),"权限不足!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
        showData();



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当ListView不滚动时，悬浮按钮状态为可见
                    case 0:
                        sb.setVisibility(View.VISIBLE);
                        sb1.setVisibility(View.VISIBLE);
                        break;

                    // 当ListView滚动时，悬浮按钮状态为隐藏
                    case 1:
                    case 2:
                        sb.setVisibility(View.GONE);
                        sb1.setVisibility(View.GONE);
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
                Intent intent = new Intent(getContext(),Search.class);
                startActivity(intent);
            }
        });

    }

    private void showData() {
        p.clear();                //清除List中的数据，防止数据显示出错
            try (Cursor cursor = mDB.rawQuery("select * from posting order by postTime desc", new String[0])) {
                while (cursor.moveToNext()) {
                    p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
                }
            }
        //通知观察者数据已经变更
        mPostingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        // 重写onResume()方法，保持显示数据常新
        super.onResume();
        showData();
    }


}
