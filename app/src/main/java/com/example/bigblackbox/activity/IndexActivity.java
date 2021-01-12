package com.example.bigblackbox.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.bigblackbox.R;
import com.example.bigblackbox.adpater.ViewAdpater;
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
        setContentView(R.layout.activity_index);

        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(new Home());
        fragments.add(new Community());
        fragments.add(new Mine());

        final ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new ViewAdpater(this,fragments));
        viewPager2.setUserInputEnabled(false);

        final TabLayout tabLayout = findViewById(R.id.buttomView);

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
}