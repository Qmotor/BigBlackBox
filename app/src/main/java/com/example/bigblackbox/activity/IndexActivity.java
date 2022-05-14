package com.example.bigblackbox.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.bigblackbox.R;
import com.example.bigblackbox.tool.UserInfo;
import com.example.bigblackbox.adapter.ViewAdapter;
import com.example.bigblackbox.fragment.Community;
import com.example.bigblackbox.fragment.Home;
import com.example.bigblackbox.fragment.Mine;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends AppCompatActivity {

    //static String user_name = getIntent().getStringExtra("userName");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(UserInfo.isAdmin.equals("1")){
//            Toast.makeText(this,"欢迎管理员",Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this,"欢迎您",Toast.LENGTH_SHORT).show();
//        }

        setContentView(R.layout.activity_index);

        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(new Home());
        fragments.add(new Community());
        fragments.add(new Mine());

        final ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new ViewAdapter(this,fragments));
        viewPager2.setUserInputEnabled(false);

        final TabLayout tabLayout = findViewById(R.id.buttonView);
        tabLayout.setSelectedTabIndicator(null);   // 去除底部TabLayout上的滚动条

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    long time = System.currentTimeMillis();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long now=System.currentTimeMillis();
            if(now - time < 2000){
                this.finish();
            }
            else {
                time = now;
                Toast.makeText(this,"再按一次退出系统",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}