package com.taha.touchgestures;

import android.view.GestureDetector;
import android.view.MotionEvent;

class SingleGestureListener implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        MainActivity.lastSingleGesture = System.currentTimeMillis();
            if (MainActivity.lastSingleGesture - MainActivity.lastDoubleGesture > 200) {
                MainActivity.announce("single long tap confirmed");
            }
        System.out.printf("DEBUGGING TAG: single long tap confirmed");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        MainActivity.lastSingleGesture = System.currentTimeMillis();
        if (MainActivity.lastSingleGesture - MainActivity.lastDoubleGesture > 200) {
            if (Math.abs(velocityX) > 2 * Math.abs(velocityY)) {
                if (velocityX > 0) {
                    //swipe right
                    MainActivity.announce("single fling right");
                } else {
                    //swipe left
                    MainActivity.announce("single fling left");
                }
            } else if (Math.abs(velocityY) > 2 * Math.abs(velocityX)) {
                if (velocityY > 0) {
                        //swipe down
                        MainActivity.announce("single fling down");
                    } else {
                        //swipe up
                        MainActivity.announce("single fling up");
                    }
                }
            }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        MainActivity.lastSingleGesture = System.currentTimeMillis();
        if (MainActivity.lastSingleGesture - MainActivity.lastDoubleGesture > 200 &&
                MainActivity.lastDetectedGesture != "DOUBLE_SINGLE_TAP_CONFIRMED") {
            MainActivity.announce("single single tap confirmed");
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        MainActivity.lastSingleGesture = System.currentTimeMillis();
        if (MainActivity.lastSingleGesture - MainActivity.lastDoubleGesture > 200) {
            MainActivity.announce("single double tap confirmed");
        }
        //System.out.printf("DEBUGGING TAG: single double tap confirmed");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}
