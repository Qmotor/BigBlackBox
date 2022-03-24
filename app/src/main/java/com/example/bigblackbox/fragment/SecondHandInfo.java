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

import com.example.bigblackbox.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.UserInfo;
import com.example.bigblackbox.adapter.PostingAdapter;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.Post_detail;
import com.example.bigblackbox.entity.User;

import java.util.ArrayList;
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

        mPostingAdapter = new PostingAdapter(getContext(), p);
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
                        if(!name.equals(UserInfo.userName)){
                            Toast.makeText(getContext(),"权限不足!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            mDB.execSQL("delete from posting where postUserName = ? and postID = ?",
                                    new String[]{UserInfo.userName, String.valueOf(posting.getPostID())});
                            Toast.makeText(getContext(),"删除成功", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
        showSellData();
    }
    private void showSellData(){
        p.clear();           //清空List中的数据，防止数据多次显示
            try (Cursor cursor = mDB.rawQuery("select * from posting where postFollow = 3 order by postTime desc", new String[0])) {
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
