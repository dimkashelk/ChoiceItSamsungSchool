package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class LockedBottomSheetBehavior extends BottomSheetBehavior {
    private final boolean swipe = false;

    public LockedBottomSheetBehavior() {
    }

    public LockedBottomSheetBehavior(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent event) {
        if (swipe) {
            return super.onInterceptTouchEvent(parent, child, event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent event) {
        if (swipe) {
            return super.onTouchEvent(parent, child, event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if (swipe) {
            return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
        } else {
            return false;
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (swipe) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        if (swipe) {
            super.onStopNestedScroll(coordinatorLayout, child, target, type);
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        if (swipe) {
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        } else {
            return false;
        }
    }
}
