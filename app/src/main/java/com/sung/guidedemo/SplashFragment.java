package com.sung.guidedemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.sung.guidedemo.view.ShakingLodingView;

/**
 * Created by sung on 2018/3/16.
 */

public class SplashFragment extends DialogFragment {
    public static final String TAG = SplashFragment.class.getSimpleName();
    private ShakingLodingView mLoadingView;
    private ImageView mAnimImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_dialog,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.FILL_PARENT;
            window.setAttributes(lp);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);

        mLoadingView = view.findViewById(R.id.slv_loading_view);
        mAnimImg = view.findViewById(R.id.iv_anim);
        uiHandler.sendEmptyMessageDelayed(0,3500);
    }

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mLoadingView.destroy();
            mLoadingView.setVisibility(View.GONE);
            doScaleAnimation();
        }
    };

    private void doScaleAnimation(){
        mAnimImg.setVisibility(View.VISIBLE);
        //计算放大倍数
        // 以view中心为缩放点，由初始状态放大
        float scale = getScale();
        ScaleAnimation animation = new ScaleAnimation(
                1.0f, scale, 1.0f, scale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setDuration(700L);
        animation.setInterpolator(new AccelerateInterpolator());
        mAnimImg.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SplashFragment.this.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private float getScale(){
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mAnimImg.measure(w, h);
        float viewHeight = mAnimImg.getMeasuredHeight();
        float height = dm.heightPixels + 400;// 屏幕高度（像素）

        float scale = height / viewHeight;
        return scale;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ((MainActivity)getActivity()).init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
