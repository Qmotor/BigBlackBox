package com.example.bigblackbox.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bigblackbox.R;
import com.example.bigblackbox.adapter.ViewAdapter;
import com.example.bigblackbox.fragment.AllNews;
import com.example.bigblackbox.fragment.SecondHandInfo;
import com.example.bigblackbox.fragment.Lecture;
import com.example.bigblackbox.fragment.Talk;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        List<Fragment> fragments = new ArrayList<>(4);
        /*
        向ListView中添加不同的片段
         */
        fragments.add(new AllNews());
        fragments.add(new Talk());
        fragments.add(new Lecture());
        fragments.add(new SecondHandInfo());

        final ViewPager2 viewPager2 = findViewById(R.id.ChatViewPager);
//        viewPager2.setOffscreenPageLimit(5);
        viewPager2.setAdapter(new ViewAdapter(this,fragments));

        final TabLayout tabLayout = findViewById(R.id.ChatButtonView);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                tabLayout.setScrollPosition(position,positionOffset,true);
            }
        });

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