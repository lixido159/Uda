package com.example.xyzreader.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

/**
 * Created by m on 2018/3/3.
 */

public class CustomPagerTransform implements ViewPager.PageTransformer {
    //0代表当前屏幕显示的view的position，1代表当前view的下一个view所在的position，-1代表当前view的前一个view所在的position
    @Override
    public void transformPage(View page, float position) {
        float MIN_SCALE = 0.75f;
        if (position < -1) {
            page.setAlpha(0);
        } else if (position <= 0) {
            page.setAlpha(1+position);
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else if (position <= 1) {
            page.setAlpha(1 - position);
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else {
            page.setAlpha(0);

        }
    }
}
