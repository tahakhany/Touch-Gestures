package com.taha.touchgestures;

import android.view.MotionEvent;

public class DoubleGestureListener implements Runnable {
    private static int mAction;
    private static int numTimesTouched = 0;
    public final int NO_ACTION = 0;
    public final int DOUBLE_SINGLE_TAP = 1;
    public final int DOUBLE_DOUBLE_TAP = 2;
    public final int DOUBLE_SINGLE_TAP_CONFIRMED = 3;
    public final int DOUBLE_TRIPLE_TAP = 4;
    public final int DOUBLE_LONG_TAP = 5;
    private final long MAXIMUM_TOUCH_DURATION = 200;
    private MotionEvent mEvent;
    private long mPreviousTapTime = 0;
    private long mFirstSingleTapTime = 0;
    private long mSecondSingleTapTime = 0;
    private final long mActionDownEnd = 0;
    private long mActionDownStart = 0;

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

    public void setFirstSingleTapTime(long firstSingleTapTime) {
        mFirstSingleTapTime = firstSingleTapTime;
    }

    public long getSecondSingleTapTime() {
        return mSecondSingleTapTime;
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
            Thread thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return mAction;
    }

    public void setAction(int action) {
        mAction = action;
    }


    @Override
    public void run() {
        if (mEvent.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN ||
                mEvent.getActionMasked() == MotionEvent.ACTION_MOVE ||
                mEvent.getActionMasked() != MotionEvent.ACTION_POINTER_UP) {
            if (mActionDownStart == 0) {
                mActionDownStart = getCurrentTime();
            } else if (getCurrentTime() - mActionDownStart >= MAXIMUM_TOUCH_DURATION) {
                setAction(DOUBLE_LONG_TAP);
                System.out.println("TESTING ACTION THREAD: " + getActionMasked());
            }
        } else if (mEvent.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {

            numTimesTouched++;
            try {
                Thread.sleep(MAXIMUM_TOUCH_DURATION);
                System.out.println("TESTING ACTION THREAD: " + numTimesTouched);
                switch (numTimesTouched) {
                    case 0:
                        break;
                    case 1:
                        this.setAction(DOUBLE_SINGLE_TAP_CONFIRMED);
                        numTimesTouched = 0;
                        System.out.println("TESTING ACTION THREAD: " + getActionMasked());
                        break;
                    case 2:
                        this.setAction(DOUBLE_DOUBLE_TAP);
                        numTimesTouched = 0;
                        System.out.println("TESTING ACTION THREAD: " + getActionMasked());
                        break;
                    case 3:
                        this.setAction(DOUBLE_TRIPLE_TAP);
                        numTimesTouched = 0;
                        System.out.println("TESTING ACTION THREAD: " + getActionMasked());
                        break;
                    default:
                        this.setAction(NO_ACTION);
                        numTimesTouched = 0;
                        System.out.println("TESTING ACTION THREAD: " + getActionMasked());
                        break;
                }
                mActionDownStart = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getActionMasked() {
        switch (mAction) {
            case DOUBLE_SINGLE_TAP:
                return "DOUBLE_SINGLE_TAP";
            case DOUBLE_DOUBLE_TAP:
                return "DOUBLE_DOUBLE_TAP";
            case DOUBLE_SINGLE_TAP_CONFIRMED:
                return "DOUBLE_SINGLE_TAP_CONFIRMED";
            case DOUBLE_LONG_TAP:
                return "DOUBLE_LONG_TAP";
            case DOUBLE_TRIPLE_TAP:
                return "DOUBLE_TRIPLE_TAP";
            default:
                return "NO_ACTION";
        }
    }
}
