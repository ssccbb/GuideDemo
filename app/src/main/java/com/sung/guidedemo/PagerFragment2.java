package com.sung.guidedemo;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sung.guidedemo.view.VariableDirectionProgressbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment2 extends Fragment {
    private static final String TAG = PagerFragment2.class.getSimpleName();
    private View tittle,subTittle,woman,man;


    public PagerFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_fragment2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tittle = view.findViewById(R.id.tv_tittle);
        subTittle = view.findViewById(R.id.tv_subtittle);
        woman = view.findViewById(R.id.ll_avatar_woman);
        man = view.findViewById(R.id.ll_avatar_man);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnim();
    }

    public void startAnim(){
        if (tittle == null || subTittle == null || woman == null || man == null)
            return;

        anim(tittle,100);
        anim(subTittle,300);
        anim(woman,600);
        anim(man,1000);
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
