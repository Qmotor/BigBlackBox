package com.example.bigblackbox.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bigblackbox.ChooseSchool;
import com.example.bigblackbox.GuessNum;
import com.example.bigblackbox.GuessNumChoose;
import com.example.bigblackbox.Plan;
import com.example.bigblackbox.R;
import com.example.bigblackbox.RealStuffShare;
import com.example.bigblackbox.activity.Chat;
import com.example.bigblackbox.activity.GenSubject;
import com.example.bigblackbox.activity.GraduateData;
import com.example.bigblackbox.activity.Teacher;
import com.example.bigblackbox.adapter.LoopViewAdapter;
import com.example.bigblackbox.tool.PagerOnClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Community extends Fragment {
    private ViewPager viewPager;  //轮播图模块
    private int[] mImg;
    private int[] mImg_id;
    private String[] mDec;
    private ArrayList<ImageView> mImgList;
    private LinearLayout ll_dots_container;
    private TextView loop_dec;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;
    private Intent intent;
    private final String[] names = new String[]{"谈天说地","院校信息","统考科目备考","专业课备考","老师推荐","考研资料","研招网","轻松一刻","复试准备","干货分享"};
    private final int[] imgIds = new int[]{R.drawable.p1,R.drawable.p2,R.drawable.p4,R.drawable.p5,R.drawable.p6,
            R.drawable.p7,R.drawable.p8,R.drawable.p10,R.drawable.p11,
            R.drawable.p12};

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
        GridView gridView = view.findViewById(R.id.gridView);
        viewPager = view.findViewById(R.id.loopViewPager);
        ll_dots_container = view.findViewById(R.id.ll_dots_loop);
        loop_dec = view.findViewById(R.id.loop_dec);
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
                    case 1:
                        intent = new Intent(getActivity(), ChooseSchool.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), GenSubject.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), Teacher.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), GraduateData.class);
                        startActivity(intent);
                        break;
                    case 6:
                        Uri uri = Uri.parse("https://yz.chsi.com.cn");
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(getActivity(), GuessNumChoose.class);
                        startActivity(intent);
                        break;
                    case 9:
                        intent = new Intent(getActivity(), RealStuffShare.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        // 图片资源id数组
        mImg = new int[]{
                R.drawable.zy, R.drawable.yjs, R.drawable.bk, R.drawable.gg, R.drawable.aq
        };

        // 文本描述
        mDec = new String[]{
                "考研数学名师——张宇", "“别人家的”寝室，4人同时考上研究生", "这个寒假，23考研人该如何启动备考？", "平台维护公告", "完善密保信息，维护账号安全"
        };

        mImg_id = new int[]{
                R.id.pager_img1, R.id.pager_img2, R.id.pager_img3, R.id.pager_img4, R.id.pager_img5
        };

        // 初始化要展示的5个ImageView
        mImgList = new ArrayList<>();
        ImageView imageView;
        View dotView;
        LinearLayout.LayoutParams layoutParams;
        for(int i=0;i<mImg.length;i++){
            //初始化要显示的图片对象
            imageView = new ImageView(requireContext());
            imageView.setBackgroundResource(mImg[i]);
            imageView.setId(mImg_id[i]);
            imageView.setOnClickListener(new PagerOnClickListener(requireContext()));
            mImgList.add(imageView);
            //加引导点
            dotView = new View(requireContext());
            dotView.setBackgroundResource(R.drawable.dot);
            layoutParams = new LinearLayout.LayoutParams(10,10);
            if(i!=0){
                layoutParams.leftMargin=10;
            }
            //设置默认所有都不可用
            dotView.setEnabled(false);
            ll_dots_container.addView(dotView,layoutParams);
        }

        ll_dots_container.getChildAt(0).setEnabled(true);
        loop_dec.setText(mDec[0]);
        previousSelectedPosition=0;
        //设置适配器
        viewPager.setAdapter(new LoopViewAdapter(mImgList));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) %mImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int newPosition = i % mImgList.size();
                loop_dec.setText(mDec[newPosition]);
                ll_dots_container.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_dots_container.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // 开启轮询
        new Thread(){
            public void run(){
                isRunning = true;
                while(isRunning){
                    try{
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //下一条
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
            }
        }.start();

    }
}
