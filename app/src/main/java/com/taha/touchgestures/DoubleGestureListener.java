package com.taha.touchgestures;

import android.view.MotionEvent;

public class DoubleGestureListener {
    public final int NO_ACTION = 0;
    public final int DOUBLE_SINGLE_TAP = 1;
    public final int DOUBLE_DOUBLE_TAP = 2;
    public final int DOUBLE_HOLD_DOWN_BUTTON = 3;
    private final long MINIMUM_TOUCH_DURATION = 500;
    private MotionEvent mEvent;

    private int mAction;
    private long mPreviousTapTime = 0;
    private long mPreviousSingleTapTime = 0;


    public DoubleGestureListener(MotionEvent event) {
        onTouchEvent(event);
    }

    public DoubleGestureListener() {
        onTouchEvent(null);
    }

    public void onTouchEvent(MotionEvent event) {
        if (event != null) this.mEvent = event;
    }

    public long getPreviousTapTime() {
        return mPreviousTapTime;
    }

    public void setPreviousTapTime(long previousTapTime) {
        mPreviousTapTime = previousTapTime;
    }

    public long getPreviousSingleTapTime() {
        return mPreviousSingleTapTime;
    }

    public void setPreviousSingleTapTime(long previousSingleTapTime) {
        mPreviousSingleTapTime = previousSingleTapTime;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public long getTapDifference() {
        if (getPreviousTapTime() == 0) {
            return -1;
        } else {
            return (this.mEvent.getEventTime() - getPreviousTapTime());
        }
    }


    public int getEvent() {
        try {
            if (mEvent.getAction() == MotionEvent.ACTION_POINTER_UP) {
                System.out.println("\nDEBUGGING TAG event down time: " + (this.mEvent.getDownTime()));
                System.out.println("DEBUGGING TAG event Time: " + (this.mEvent.getEventTime()));
                System.out.println("DEBUGGING TAG tap difference: " + getTapDifference());
                System.out.println("DEBUGGING TAG time difference between up and down: " + Math.abs(this.mEvent.getDownTime() - this.mEvent.getEventTime()));

                if (Math.abs(this.mEvent.getDownTime() - this.mEvent.getEventTime()) <= MINIMUM_TOUCH_DURATION) {

                    if (getTapDifference() == -1) {
                        System.out.println("DEBUGGING TAG:  previous tap:" + getTapDifference());
                        setPreviousTapTime(mEvent.getEventTime());
                        setAction(DOUBLE_SINGLE_TAP);
                        System.out.println("DEBUGGING TAG 1:" + this.getActionMasked());

                    } else if (getTapDifference() > MINIMUM_TOUCH_DURATION) {
                        System.out.println("DEBUGGING TAG:  previous tap:" + getTapDifference());
                        setPreviousTapTime(mEvent.getEventTime());
                        setPreviousSingleTapTime(getCurrentTime());
                        setAction(DOUBLE_SINGLE_TAP);
                        System.out.println("DEBUGGING TAG 1:" + this.getActionMasked());

                    } else if (getTapDifference() <= MINIMUM_TOUCH_DURATION) {
                        setAction(DOUBLE_DOUBLE_TAP);
                        System.out.println("DEBUGGING TAG: previous tap:" + getTapDifference());
                        System.out.println("DEBUGGING TAG 3:" + this.getActionMasked());
                        setPreviousTapTime(0);
                    }

                } else {
                    setAction(NO_ACTION);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return mAction;
    }

    public void setAction(int action) {
        mAction = action;
    }

    public String getActionMasked() {
        switch (mAction) {
            case 1:
                return "DOUBLE_SINGLE_TAP";
            case 2:
                return "DOUBLE_DOUBLE_TAP";
            case 3:
                return "DOUBLE_HOLD_DOWN_BUTTON";
            default:
                return "NO_ACTION";
        }
    }
}
