package com.example.bigblackbox.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bigblackbox.R;
import com.example.bigblackbox.adapter.ViewAdapter;
import com.example.bigblackbox.fragment.EnglishTeacher;
import com.example.bigblackbox.fragment.MathTeacher;
import com.example.bigblackbox.fragment.PolityTeacher;
import com.example.bigblackbox.fragment.ProfessionTeacher;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher);

        List<Fragment> fragments = new ArrayList<>(4);
        /*
        向ListView中添加不同的片段
         */
        fragments.add(new MathTeacher());
        fragments.add(new EnglishTeacher());
        fragments.add(new PolityTeacher());
        fragments.add(new ProfessionTeacher());

        final ViewPager2 viewPager2 = findViewById(R.id.TeacherViewPager);
        viewPager2.setAdapter(new ViewAdapter(this,fragments));

        final TabLayout tabLayout = findViewById(R.id.TeacherButtonView);

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
