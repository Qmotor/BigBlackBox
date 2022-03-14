package com.example.bigblackbox.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.R;
import com.example.bigblackbox.activity.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Community extends Fragment {

    private GridView gridView;
    private Intent intent;
    private String[] names = new String[]{"谈天说地","院校信息","考研大纲","统考科目备考","专业课备考","老师推荐","考研资料","研招网","轻松一刻","备考时间表","复试准备","干货分享","13","14","15"};
    private int[] imgIds = new int[]{R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,
            R.drawable.p7,R.drawable.p8,R.drawable.p9,R.drawable.p10,R.drawable.p11,
            R.drawable.p12,R.drawable.p13,R.drawable.p14,R.drawable.p15};

    private List<Map<String,Object>> data = new ArrayList<>();

    private void initData(){
        for(int i = 0; i < names.length; i++){
            Map<String,Object>item = new HashMap<>();
            item.put("name", names[i]);
            item.put("imgId", imgIds[i]);
            data.add(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        绑定控件，将数据库查询结果显示在相应的ListView中
         */
        gridView = view.findViewById(R.id.gridView);
        initData();
        SimpleAdapter adapter = new SimpleAdapter(getContext(),data,R.layout.grid_item,new String[]{"name","imgId"}
                ,new int[]{R.id.name,R.id.img});
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), Chat.class);
                        startActivity(intent);
                        break;
                    case 7:
                        Uri uri = Uri.parse("https://yz.chsi.com.cn");
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
