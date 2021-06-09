package com.taha.touchgestures;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

public class MainActivity extends AppCompatActivity {

    public static ImageView mImageView;
    public static TextView mXDistanceView;
    public static TextView mYDistanceView;
    public static TextView mDetectedGesture;
    public static TextView mTouchEventView;
    public static long lastSingleGesture = 0;
    public static long lastDoubleGesture = 0;
    public static String lastDetectedGesture;
    public static ContentResolver mContentResolver;
    public static Window mWindow;
    final String TAG = "DEBUGGING TAG ";
    GestureDetectorCompat mSingleGestureDetector;
    private int mBrightness;
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

        mXDistanceView = findViewById(R.id.xDistanceView);
        mYDistanceView = findViewById(R.id.yDistanceView);
        mDetectedGesture = findViewById(R.id.detectedGesture);
        mImageView = findViewById(R.id.imageView);
        mTouchEventView = findViewById(R.id.touchEventTextView);

        mContentResolver = getContentResolver();
        mWindow = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        try {
            Settings.System.putInt(mContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            mBrightness = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        mSingleGestureDetector = new GestureDetectorCompat(this, mSingleGestureListener);
        DoubleGestureListener mDoubleGestureListener = new DoubleGestureListener();

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mTouchEventView.setText(MotionEvent.actionToString(motionEvent.getActionMasked()));
                System.out.println("TESTING ACTION: " + motionEvent.getActionMasked() + ": " + MotionEvent.actionToString(motionEvent.getActionMasked()));
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
