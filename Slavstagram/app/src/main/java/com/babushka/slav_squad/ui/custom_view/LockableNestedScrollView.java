package com.babushka.slav_squad.ui.custom_view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by iliyan on 29.09.17.
 */

public class LockableNestedScrollView extends NestedScrollView {

    private boolean mScrollingEnabled;

    public LockableNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScrollingEnabled = true;
    }

    public boolean isScrollingEnabled() {
        return mScrollingEnabled;
    }

    public void setScrollingEnabled(boolean scrollingEnabled) {
        mScrollingEnabled = scrollingEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollingEnabled) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return false; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!mScrollingEnabled) return false;
        else return super.onInterceptTouchEvent(ev);
    }
}
