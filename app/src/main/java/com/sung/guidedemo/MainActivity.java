package com.sung.guidedemo;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager mPager;
    private PagerIndicatorView mIndicator;
    private Button mNext;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mPager = findViewById(R.id.vp_pager);
        mIndicator = findViewById(R.id.pi_indicator);
        mNext = findViewById(R.id.btn_next);

        final List<Fragment> fragmentss = new ArrayList<>();
        PagerFragment1 fragment1 = new PagerFragment1();
        PagerFragment2 fragment2 = new PagerFragment2();
        PagerFragment3 fragment3 = new PagerFragment3();
        PagerFragment4 fragment4 = new PagerFragment4();
        fragmentss.add(fragment1);
        fragmentss.add(fragment2);
        fragmentss.add(fragment3);
        fragmentss.add(fragment4);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),fragmentss);
        mIndicator.setPagerCount(fragmentss.size());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.setSelectPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition++;
                if (currentPosition >= fragmentss.size()){
                    currentPosition = 0;
                }
                mPager.setCurrentItem(currentPosition,true);
            }
        });
    }
}
