package com.example.bigblackbox.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bigblackbox.R;
import com.example.bigblackbox.adapter.ViewAdapter;
import com.example.bigblackbox.fragment.EnglishData;
import com.example.bigblackbox.fragment.MathData;
import com.example.bigblackbox.fragment.PolityData;
import com.example.bigblackbox.fragment.ProfessionData;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class GraduateData extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grad_data);

        List<Fragment> fragments = new ArrayList<>(4);
        /*
        向ListView中添加不同的片段
         */
        fragments.add(new MathData());
        fragments.add(new EnglishData());
        fragments.add(new PolityData());
        fragments.add(new ProfessionData());

        final ViewPager2 viewPager2 = findViewById(R.id.DataViewPager);
        viewPager2.setAdapter(new ViewAdapter(this,fragments));

        final TabLayout tabLayout = findViewById(R.id.DataButtonView);

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
