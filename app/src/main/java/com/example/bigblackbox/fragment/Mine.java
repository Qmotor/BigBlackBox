package com.example.bigblackbox.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bigblackbox.EditPwd;
import com.example.bigblackbox.EditUserInfo;
import com.example.bigblackbox.MainActivity;
import com.example.bigblackbox.MyPosting;
import com.example.bigblackbox.R;
import com.example.bigblackbox.UserInfo;

public class Mine extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView name = view.findViewById(R.id.uName);
        TextView id = view.findViewById(R.id.uID);
        TextView edit = view.findViewById(R.id.edit_userInfo);
        TextView myPost = view.findViewById(R.id.myPosting);
        TextView editPwd = view.findViewById(R.id.editPwd);
        TextView loginOut = view.findViewById(R.id.login_out);

        name.setText(UserInfo.userName);
        id.setText("用户ID："+UserInfo.userID);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditUserInfo.class);
                startActivity(intent);
            }
        });

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyPosting.class);
                startActivity(intent);
            }
        });

        editPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditPwd.class);
                startActivity(intent);
            }
        });


        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("温馨提示");
                builder.setMessage("你确定要注销当前登录账号吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfo.userName = null;
                        UserInfo.userID = null;
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });
    }
}