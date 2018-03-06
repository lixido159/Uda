package com.example.xyzreader.ui;

import android.support.design.widget.FloatingActionButton;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by m on 2018/3/4.
 */

public class AnimatorUtil {
    private static AccelerateDecelerateInterpolator LINEAR_INTERPOLATOR=new AccelerateDecelerateInterpolator();
    public static void hide(FloatingActionButton fab,MyBehavior.AnimatiorStateListener listener){
        fab.animate()
                .scaleY(0f)
                .scaleX(0f)
                .alpha(0f)
                .setDuration(600)
                .setInterpolator(LINEAR_INTERPOLATOR)
                .setListener(listener)
                .start();
    } public static void show(FloatingActionButton fab){
        fab.animate()
                .scaleY(1f)
                .scaleX(1f)
                .alpha(1f)
                .setDuration(600)
                .setInterpolator(LINEAR_INTERPOLATOR)
                .start();
    }
    public static void show(FloatingActionButton fab, MyBehavior.AnimatiorStateListener listener){
        fab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(600)
                .setInterpolator(LINEAR_INTERPOLATOR)
                .setListener(listener)
                .start();
    }
}
