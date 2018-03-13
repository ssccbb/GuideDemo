package com.sung.guidedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by sung on 2018/3/13.
 */

public class PagerIndicatorView extends LinearLayout {
    private Context context;
    private int pagerCount = 1;
    private int selectPosition = 0;

    private static final int Indicator_Width = 50;
    private static final int Indicator_Margin = 10;

    public PagerIndicatorView(Context context, int indicator_num) {
        super(context);
        this.context = context;
        this.pagerCount = indicator_num;
        inflater();
    }

    public PagerIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PagerIndicatorView);
        if (typedArray != null){
            int count = typedArray.getInteger(R.styleable.PagerIndicatorView_indicator_count, 1);
            this.pagerCount = count;
            typedArray.recycle();
        }

        inflater();
    }

    private void inflater() {
        LinearLayout view = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.pager_indicator, null, false);

        if (pagerCount >= 1) {
            for (int i = 0; i < pagerCount; i++) {
                ImageView indicator = new ImageView(context);
                indicator.setImageResource(R.drawable.page_indicator_selector);
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(Indicator_Width, Indicator_Width);
                params.setMargins(Indicator_Margin, 0, Indicator_Margin, 0);
                view.addView(indicator, params);
                indicator.setSelected(selectPosition == i);
            }
        }

        this.removeAllViews();
        this.addView(view, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void changeSelectStatus(int select){
        if (select == this.selectPosition
                || select >= this.pagerCount)
            return;

        LinearLayout view = (LinearLayout) this.getChildAt(0);
        for (int i = 0; i < view.getChildCount(); i++) {
            if (view.getChildAt(i) instanceof ImageView){
                ImageView indicator = (ImageView) view.getChildAt(i);
                indicator.setSelected(select == i);
            }
        }
    }

    public void setPagerCount(int pagerCount) {
        this.pagerCount = pagerCount;
        inflater();
        changeSelectStatus(this.selectPosition);
    }

    public void setSelectPosition(int select) {
        changeSelectStatus(select);
        this.selectPosition = select;
    }

    public int getSelectPosition() {
        return selectPosition;
    }
}
