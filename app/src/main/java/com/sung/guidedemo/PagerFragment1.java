package com.sung.guidedemo;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sung.guidedemo.view.VariableDirectionProgressbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment1 extends Fragment {
    private View rootView;
    private View anim1;
    private View anim2;
    private View anim3;

    public PagerFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pager_fragment1, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anim1 = rootView.findViewById(R.id.tv_anim_1);
        anim2 = rootView.findViewById(R.id.tv_anim_2);
        anim3 = rootView.findViewById(R.id.ll_anim_3);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnim();
    }

    public void startAnim(){
        if (anim1 == null || anim2 == null || anim3 == null)
            return;

        anim(anim1,100);
        anim(anim2,300);
        anim(anim3,600);
    }

    private void anim(final View view, long delay){
        float targetX = view.getX();
        float targetY = view.getY();

        float begainX = targetX + 100;
        float begainY = targetY + 100;

        view.setVisibility(View.INVISIBLE);
        view.clearAnimation();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(view,"alpha",0,1,1);
        alpha.setDuration(2000);
        alpha.setStartDelay(delay);
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationY", begainY, targetY,targetY);
        translation.setDuration(2000);
        translation.setStartDelay(delay);

        translation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        alpha.start();
        translation.start();
    }
}
