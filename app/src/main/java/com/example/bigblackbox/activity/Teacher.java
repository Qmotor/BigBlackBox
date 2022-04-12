package com.example.bigblackbox.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bigblackbox.R;
import com.example.bigblackbox.adapter.ViewAdapter;
import com.example.bigblackbox.fragment.English;
import com.example.bigblackbox.fragment.Math;
import com.example.bigblackbox.fragment.Polity;
import com.example.bigblackbox.fragment.Profession;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher);

//        int id = getIntent().getIntExtra("id", 0);
//        if (id == 1) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container,new Profession())
//                    .addToBackStack(null)
//                    .commit();
//        }

        List<Fragment> fragments = new ArrayList<>(4);
        /*
        向ListView中添加不同的片段
         */
        fragments.add(new Math());
        fragments.add(new English());
        fragments.add(new Polity());
        fragments.add(new Profession());

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
