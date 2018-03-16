package com.sung.guidedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sung.guidedemo.view.PagerIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager mPager;
    private PagerIndicatorView mIndicator;
    private TextView mNext;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        showSplashFragment();
    }

    public void init(){
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
        mIndicator.addOnIndicatorClickListener(new PagerIndicatorView.onIndicatorClickListener() {
            @Override
            public void onIndicatorClick(int position) {
                currentPosition = position;
                mPager.setCurrentItem(currentPosition,true);
            }
        });

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

    private void showSplashFragment(){
        SplashFragment splashFragment = new SplashFragment();
        splashFragment.show(getSupportFragmentManager().beginTransaction(),SplashFragment.TAG);
    }

    public static void launch(Context context){
        Intent launch = new Intent();
        launch.setClass(context,MainActivity.class);
        context.startActivity(launch);
    }
}
