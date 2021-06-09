package com.taha.touchgestures;

import android.content.ContentResolver;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

class SingleGestureListener implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private int mBrightness;
    private ContentResolver mContentResolver;
    private Window mWindow;

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
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        MainActivity.lastSingleGesture = System.currentTimeMillis();
        mContentResolver = MainActivity.mContentResolver;
        mWindow = MainActivity.mWindow;
        if (MainActivity.lastSingleGesture - MainActivity.lastDoubleGesture > 500) {
            if (Math.abs(distanceX) > 2 * Math.abs(distanceY)) {
                if (distanceX > 0) {
                    //scroll left
                    MainActivity.announce("single scroll left");
                    MainActivity.mXDistanceView.setText(String.valueOf(distanceX));
                    MainActivity.mYDistanceView.setText("0.0");

                } else {
                    //scroll wright
                    MainActivity.announce("single scroll right");
                    MainActivity.mXDistanceView.setText(String.valueOf(distanceX));
                    MainActivity.mYDistanceView.setText("0.0");
                }
            } else if (Math.abs(distanceY) > 2 * Math.abs(distanceX)) {

                Settings.System.putInt(mContentResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                try {
                    mBrightness = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS);
                    System.out.println("BRIGHTNESS CONTROL: " + mBrightness);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }

                if (distanceY > 0) {
                    //scroll up
                    MainActivity.announce("single scroll up");
                    MainActivity.mXDistanceView.setText("0.0");
                    MainActivity.mYDistanceView.setText(String.valueOf(distanceY));

                    if (mBrightness >= 0 && mBrightness <= 255) {
                        mBrightness += .75 * distanceY;
                    } else {
                        mBrightness = 255;
                    }

                    Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, mBrightness);
                    WindowManager.LayoutParams layoutParams = mWindow.getAttributes();
                    layoutParams.screenBrightness = (mBrightness) / (float) 255;
                    mWindow.setAttributes(layoutParams);

                } else {
                    //scroll down
                    MainActivity.announce("single scroll down");
                    MainActivity.mXDistanceView.setText("0.0");
                    MainActivity.mYDistanceView.setText(String.valueOf(distanceY));

                    if (mBrightness >= 0 && mBrightness <= 257) {
                        mBrightness += .75 * distanceY;
                    } else {
                        mBrightness = 0;
                    }
                    Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, mBrightness);
                    WindowManager.LayoutParams layoutParams = mWindow.getAttributes();
                    layoutParams.screenBrightness = (mBrightness - distanceX * 10) / (float) 255;
                    mWindow.setAttributes(layoutParams);
                }
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        MainActivity.lastSingleGesture = System.currentTimeMillis();
        if (MainActivity.lastSingleGesture - MainActivity.lastDoubleGesture > 500) {
            MainActivity.announce("single long tap confirmed");
        }
        System.out.printf("DEBUGGING TAG: single long tap confirmed");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        MainActivity.lastSingleGesture = System.currentTimeMillis();
        if (MainActivity.lastSingleGesture - MainActivity.lastDoubleGesture > 500) {
            MainActivity.announce("single single tap confirmed");
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        MainActivity.lastSingleGesture = System.currentTimeMillis();
        if (MainActivity.lastSingleGesture - MainActivity.lastDoubleGesture > 500) {
            MainActivity.announce("single double tap confirmed");
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}
