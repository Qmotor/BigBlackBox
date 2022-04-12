package com.example.bigblackbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseTeacher extends AppCompatActivity {
    private final String[] names = new String[]{"全部","计算机","法学","医学","力学","历史学","文学","管理学","经济学","教育学","哲学","物理学","心理学","数学","艺术学","农学"};
    private final int[] imgIds = new int[]{R.drawable.all,R.drawable.computer,R.drawable.law,R.drawable.medicine,R.drawable.mechanics,R.drawable.history,
            R.drawable.literature,R.drawable.manage,R.drawable.economy,R.drawable.edu,R.drawable.philosophy,
            R.drawable.physics,R.drawable.psychology,R.drawable.math,R.drawable.art,R.drawable.agronomy};

    private final List<Map<String,Object>> data = new ArrayList<>();

    private void initData(){
        for(int i = 0; i < names.length; i++){
            Map<String,Object>item = new HashMap<>();
            item.put("name", names[i]);
            item.put("imgId", imgIds[i]);
            data.add(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_teahcer);
        GridView gridView = findViewById(R.id.teacherGridView);
        initData();
        SimpleAdapter adapter = new SimpleAdapter(this,data,R.layout.grid_item,new String[]{"name","imgId"}
                ,new int[]{R.id.name,R.id.img});
        gridView.setAdapter(adapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                switch (position) {
                    case 0:
                        UserInfo.jug = "全部";
                        finish();
                        break;
                    case 1:
                        UserInfo.jug = "计算机";
                        finish();
                        break;
                    case 2:
                        UserInfo.jug = "法学";
                        finish();
                        break;
                    case 3:
                        UserInfo.jug = "医学";
                        finish();
                        break;
                    case 4:
                        UserInfo.jug = "力学";
                        finish();
                        break;
                    case 5:
                        UserInfo.jug = "历史学";
                        finish();
                        break;
                    case 6:
                        UserInfo.jug = "文学";
                        finish();
                        break;
                    case 7:
                        UserInfo.jug = "管理学";
                        finish();
                        break;
                    case 8:
                        UserInfo.jug = "经济学";
                        finish();
                        break;
                    case 9:
                        UserInfo.jug = "教育学";
                        finish();
                        break;
                    case 10:
                        UserInfo.jug = "哲学";
                        finish();
                        break;
                    case 11:
                        UserInfo.jug = "物理学";
                        finish();
                        break;
                    case 12:
                        UserInfo.jug = "心理学";
                        finish();
                        break;
                    case 13:
                        UserInfo.jug = "数学";
                        finish();
                        break;
                    case 14:
                        UserInfo.jug = "艺术学";
                        finish();
                        break;
                    case 15:
                        UserInfo.jug = "农学";
                        finish();
                        break;
                }
            }
        });
    }
}
