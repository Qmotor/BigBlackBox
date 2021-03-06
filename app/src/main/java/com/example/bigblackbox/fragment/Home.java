package com.example.bigblackbox.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bigblackbox.AddPush;
import com.example.bigblackbox.tool.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.tool.UserInfo;
import com.example.bigblackbox.adapter.PushInfoAdapter;
import com.example.bigblackbox.entity.PushInfo;
import com.example.bigblackbox.Push_detail;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    final List<PushInfo> pi = new ArrayList<>();
    private PushInfoAdapter mPushInfoAdapter;
    private ImageView addBtn,searchBtn;
    private SQLiteDatabase mDB;

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addBtn = view.findViewById(R.id.addBtnHome);
        searchBtn = view.findViewById(R.id.searchBtnHome);

        // ?????????????????????????????????????????????????????????ListView???
        final ListView listView = view.findViewById(R.id.homeList);

        mPushInfoAdapter = new PushInfoAdapter(requireContext(), pi);
        listView.setAdapter(mPushInfoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushInfo pushInfo = pi.get(position);
                Intent intent = new Intent(getContext(), Push_detail.class);
                // ????????????????????????????????????????????????????????????ID?????????????????????Push_detail???
                intent.putExtra("pushInfoID",pushInfo.getPushID());
                startActivity(intent);
            }
        });

        // ??????????????????
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(UserInfo.isAdmin.equals("1")) {
                    final PushInfo pushInfo = pi.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("??????");
                    builder.setMessage("?????????????????????????????????");
                    builder.setPositiveButton("????????????0_o", null);
                    builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (UserInfo.isAdmin.equals("1")) {
                                mDB.execSQL("delete from pushing where push_id = ?",
                                        new String[]{String.valueOf(pushInfo.getPushID())});
                                Toast.makeText(getContext(), "????????????", Toast.LENGTH_SHORT).show();
                                requireActivity().onBackPressed();
                                Intent intent = new Intent(getActivity(), com.example.bigblackbox.activity.IndexActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "????????????!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.create().show();
                }
                return true;
            }
        });

        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if(UserInfo.isAdmin.equals("1")) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                    // ???ListView??????????????????????????????????????????
                        case 0:
                            addBtn.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);
                            break;
                      // ???ListView???????????????????????????????????????
                        case 1:
                        case 2:
                            addBtn.setVisibility(View.GONE);
                            searchBtn.setVisibility(View.GONE);
                            break;
                    }
                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }else {
            addBtn.setVisibility(View.GONE);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPush.class);
                startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                final LinearLayout linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.dialogname,null);
                final EditText albumName = linearLayout.findViewById(R.id.eUser);
                albumName.setHint("??????????????????");
                builder.setTitle("????????????");
                builder.setView(linearLayout);
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pi.clear();
                        @SuppressLint("Recycle") Cursor cursor = mDB.rawQuery("select * from pushing where push_title like ? and push_follow = '0' order by push_time desc", new String[]{"%" + String.valueOf(albumName.getText()).trim() + "%"});
                        while (cursor.moveToNext()) {
                            pi.add(new PushInfo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6)));
                        }
                        listView.setAdapter(new PushInfoAdapter(requireContext(), pi));
                    }
                });
                builder.setNegativeButton("??????",null);
                builder.create().show();
            }
        });
    }

    private void showData() {
        pi.clear();                //??????List???????????????????????????????????????
            try(Cursor cursor = mDB.rawQuery("select * from pushing where push_follow = '0' order by push_time desc",new String[0])){
                while(cursor.moveToNext()){
                    pi.add(new PushInfo(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6)));
                }
            }
        //?????????????????????????????????
        mPushInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        // ??????onResume()?????????????????????????????????
        super.onResume();
        showData();
    }
}