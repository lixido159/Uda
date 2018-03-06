package com.example.xyzreader.ui;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.example.xyzreader.R;


/**
 * Created by m on 2018/3/3.
 */

public class MyBehavior extends FloatingActionButton.Behavior {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private boolean isAnimating=false;
    private boolean isShown=true;
    public MyBehavior() {
    }

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
       if ((dyConsumed>0||dyUnconsumed>0)&&!isAnimating&&isShown){
           AnimatorUtil.hide(child,new AnimatiorStateListener());
       }else if ((dyConsumed<0||dyUnconsumed<0)&&!isAnimating&&!isShown){
           AnimatorUtil.show(child,new AnimatiorStateListener());
       }
    }

    public class AnimatiorStateListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            isAnimating=true;
            isShown=!isShown;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimating=false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
