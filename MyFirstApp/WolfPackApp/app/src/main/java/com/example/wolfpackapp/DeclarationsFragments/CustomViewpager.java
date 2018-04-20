package com.example.wolfpackapp.DeclarationsFragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomViewpager extends ViewPager {

    private boolean enabled;

    public CustomViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enabled && super.onInterceptTouchEvent(event);
    }


    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPagingEnabled() {
        return enabled;
    }
}