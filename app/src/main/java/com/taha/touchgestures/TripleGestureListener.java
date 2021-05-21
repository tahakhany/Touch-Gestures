package com.taha.touchgestures;

import android.view.GestureDetector;
import android.view.MotionEvent;

class TripleGestureListener implements
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
        MainActivity.announce("Triple long tap confirmed");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > 2 * Math.abs(velocityY)) {
            if (velocityX > 0) {
                //swipe right
                MainActivity.announce("Triple fling right");
            } else {
                //swipe left
                MainActivity.announce("Triple fling left");
            }
        } else if (Math.abs(velocityY) > 2 * Math.abs(velocityX)) {
            if (velocityY > 0) {
                //swipe down
                MainActivity.announce("Triple fling down");
            } else {
                //swipe up
                MainActivity.announce("Triple fling up");
            }
        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        MainActivity.announce("Triple single tap confirmed");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        MainActivity.announce("Triple double tap confirmed");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}
