package com.taha.touchgestures;

import android.view.MotionEvent;

public class DoubleGestureListener {
    public final int NO_ACTION = 0;
    public final int DOUBLE_SINGLE_TAP = 1;
    public final int DOUBLE_DOUBLE_TAP = 2;
    public final int DOUBLE_SINGLE_TAP_CONFIRMED = 3;
    public final int DOUBLE_HOLD_DOWN_BUTTON = 4;

    private static int mAction;

    private MotionEvent mEvent;
    private final long MAXIMUM_TOUCH_DURATION = 500;
    private long mPreviousTapTime = 0;
    private long mFirstSingleTapTime = 0;
    private long mSecondSingleTapTime = 0;

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

    public long getFirstSingleTapTime() {
        return mFirstSingleTapTime;
    }

    public long getSecondSingleTapTime() {
        return mSecondSingleTapTime;
    }

    public void setFirstSingleTapTime(long firstSingleTapTime) {
        mFirstSingleTapTime = firstSingleTapTime;
    }

    public void setSecondSingleTapTime(long secondSingleTapTime) {
        mSecondSingleTapTime = secondSingleTapTime;
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

                if (Math.abs(this.mEvent.getDownTime() - this.mEvent.getEventTime()) <= MAXIMUM_TOUCH_DURATION) {

                    if (getTapDifference() == -1) {
                        System.out.println("DEBUGGING TAG:  previous tap:" + getTapDifference());
                        setPreviousTapTime(mEvent.getEventTime());
                        setAction(DOUBLE_SINGLE_TAP);
                        setFirstSingleTapTime(getCurrentTime());
                        System.out.println("DEBUGGING TAG 1:" + this.getActionMasked());

                    } else if (getTapDifference() > MAXIMUM_TOUCH_DURATION) {
                        System.out.println("DEBUGGING TAG:  previous tap:" + getTapDifference());
                        setPreviousTapTime(mEvent.getEventTime());
                        setFirstSingleTapTime(getCurrentTime());
                        setAction(DOUBLE_SINGLE_TAP);
                        System.out.println("DEBUGGING TAG 1:" + this.getActionMasked());

                    } else if (getTapDifference() <= MAXIMUM_TOUCH_DURATION) {
                        setAction(DOUBLE_DOUBLE_TAP);
                        System.out.println("DEBUGGING TAG: previous tap:" + getTapDifference());
                        System.out.println("DEBUGGING TAG 3:" + this.getActionMasked());
                        setSecondSingleTapTime(getCurrentTime());
                        setPreviousTapTime(0);
                    }

                    if (mAction == DOUBLE_SINGLE_TAP) {
                        System.out.println("DEBUGGING TAG: checking for single tap confirmation:");
                        System.out.println("DEBUGGING TAG: first tap: " + getFirstSingleTapTime());
                        System.out.println("DEBUGGING TAG: second tap: " + getSecondSingleTapTime());
                        if (getSecondSingleTapTime() - getFirstSingleTapTime() > MAXIMUM_TOUCH_DURATION || getSecondSingleTapTime() == 0) {
                            setAction(DOUBLE_SINGLE_TAP_CONFIRMED);
                        }
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
            case DOUBLE_SINGLE_TAP:
                return "DOUBLE_SINGLE_TAP";
            case DOUBLE_DOUBLE_TAP:
                return "DOUBLE_DOUBLE_TAP";
            case DOUBLE_SINGLE_TAP_CONFIRMED:
                return "DOUBLE_SINGLE_TAP_CONFIRMED";
            case DOUBLE_HOLD_DOWN_BUTTON:
                return "DOUBLE_HOLD_DOWN_BUTTON";
            default:
                return "NO_ACTION";
        }
    }
}
