package com.taha.touchgestures;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

public class MainActivity extends AppCompatActivity {

    public static ImageView mImageView;
    public static TextView mXVelocityView;
    public static TextView mYVelocityView;
    public static TextView mDetectedGesture;
    public static TextView mTouchEventView;
    final String TAG = "DEBUGGING TAG ";
    GestureDetectorCompat mSingleGestureDetector;
    GestureDetectorCompat mDoubleGestureDetector;
    GestureDetectorCompat mTripleGestureDetector;
    ScaleGestureDetector mScaleDetector;
    public static long lastSingleGesture = 0;
    public static long lastDoubleGesture = 0;
    public static long lastTripleGesture = 0;
    public static String lastDetectedGesture;
    int[] pointerID;
    int[] pointerIndex;
    SingleGestureListener mSingleGestureListener = new SingleGestureListener();

    public static void announce(String string) {
        MainActivity.mDetectedGesture.setText(string);
        lastDetectedGesture = string;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mXVelocityView = findViewById(R.id.xVelocityView);
        mYVelocityView = findViewById(R.id.yVelocityView);
        mDetectedGesture = findViewById(R.id.detectedGesture);
        mImageView = findViewById(R.id.imageView);
        mTouchEventView = findViewById(R.id.touchEventTextView);

        mSingleGestureDetector = new GestureDetectorCompat(this, mSingleGestureListener);
        DoubleGestureListener mDoubleGestureListener = new DoubleGestureListener();
        TripleGestureListener mTripleGestureListener = new TripleGestureListener();

        pointerID = new int[3];
        pointerIndex = new int[3];

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //System.out.println("DEBUGGING TAG: " + motionEvent.getPointerCount());
                mTouchEventView.setText(MotionEvent.actionToString(motionEvent.getActionMasked()));

                /*String previousEvent = MotionEvent.actionToString(motionEvent.getAction());
                if(previousEvent != MotionEvent.actionToString(motionEvent.getAction())){
                    System.out.println("actionToString: " + MotionEvent.actionToString(motionEvent.getAction()));
                }*/


                /*if (motionEvent.getPointerCount() == 3) {
                    mTripleGestureListener.onTouchEvent(motionEvent);
                    if (mTripleGestureListener.getEvent() != mTripleGestureListener.NO_ACTION) {
                        lastTripleGesture = System.currentTimeMillis();
                        announce(mTripleGestureListener.getActionMasked());
                    }
                    return true;
                } else */
                if (motionEvent.getPointerCount() == 2) {
                    mDoubleGestureListener.onTouchEvent(motionEvent);
                    if (mDoubleGestureListener.getEvent() != mDoubleGestureListener.NO_ACTION) {
                        lastDoubleGesture = System.currentTimeMillis();
                        announce(mDoubleGestureListener.getActionMasked());
                    }
                    return true;
                } else if (motionEvent.getPointerCount() == 1) {
                    mSingleGestureDetector.onTouchEvent(motionEvent);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
