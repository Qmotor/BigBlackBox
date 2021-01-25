package com.example.bigblackbox.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.Add_post;
import com.example.bigblackbox.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.Search;
import com.example.bigblackbox.adpater.PostingAdpater;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.Post_detail;

import java.util.ArrayList;
import java.util.List;

public class All_news extends Fragment {
    private ImageView sb,sb1;
    SQLiteOpenHelper helper;
    private PostingAdpater mPostingAdpater;
    private List<Posting> p = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DbUtil(getContext());
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
        final ListView listView = view.findViewById(R.id.allNewsList);

        mPostingAdpater = new PostingAdpater(getContext(), p);
        listView.setAdapter(mPostingAdpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                根据用户点击的帖子，在Posting实体类中获取相应的帖子ID，并将该值传入Post_detail中
                */
                Posting posting = p.get(position);
                Intent intent = new Intent(getContext(), Post_detail.class);
                intent.putExtra("postID", posting.getPostID());
                startActivity(intent);
            }
        });
        showData();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    /*
                    当ListView不滚动时，悬浮按钮状态为可见
                     */
                    case 0:
                        sb.setVisibility(View.VISIBLE);
                        sb1.setVisibility(View.VISIBLE);
                        break;
                     /*
                      当ListView滚动时，悬浮按钮状态为隐藏
                      */
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
                Intent intent = new Intent(getContext(), Add_post.class);
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
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            try (Cursor cursor = db.rawQuery("select * from posting order by postTime desc", new String[0])) {
                while (cursor.moveToNext()) {
                    p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5)));
                }
            }
        }
        //通知观察者数据已经变更
        mPostingAdpater.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        /*
        重写onResume()方法，保持显示数据常新
         */
        super.onResume();
        showData();
    }
}
