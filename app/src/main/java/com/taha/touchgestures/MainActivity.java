package com.taha.touchgestures;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
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
    private static AudioManager audioManager;
    public static Window mWindow;
    private static BluetoothAdapter BluetoothAdapter;
    private static PackageManager packageManager;
    private static CameraManager cameraManager;
    private static boolean torchStat = false;
    final String TAG = "DEBUGGING TAG ";
    GestureDetectorCompat mSingleGestureDetector;
    SingleGestureListener mSingleGestureListener = new SingleGestureListener();
    private int mBrightness;

    public static void announce(String string) {
        MainActivity.mDetectedGesture.setText(string);
        lastDetectedGesture = string;
    }

    public static void changeVolume(Boolean isUp) {
        if (isUp)
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        else {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        }
        System.out.println("AUDIO DEBUGGING: " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    public static void toggleBluetooth() {
        try {
            if (BluetoothAdapter.isEnabled()) BluetoothAdapter.disable();
            else BluetoothAdapter.enable();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void toggleFlashlight(Boolean isBlinking) {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try {
                if (isBlinking) {
                    torchStat = !torchStat;
                    while (torchStat) {
                        cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], true);
                        Thread.sleep(250);
                        cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], false);
                        Thread.sleep(250);
                    }
                } else {
                    torchStat = !torchStat;
                    cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], torchStat);
                }
            } catch (CameraAccessException | InterruptedException e) {
                e.printStackTrace();
            }
        }
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

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        BluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();

        packageManager = this.getPackageManager();

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

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
