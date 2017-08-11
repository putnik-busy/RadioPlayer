package com.justApp.RadioPlayer.splash;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.justApp.RadioPlayer.ui.activity.StationsActivity;

import java.util.concurrent.TimeUnit;

/**
 * @author Sergey Rodionov
 */

public class SplashActivity extends AppCompatActivity {

    private CustomHandlerThread mThread;
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThread = new CustomHandlerThread("SplashThread");
        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        mUiHandler.post(this::startNewActivity);

        mThread.start();
        mThread.prepareHandler();
        mThread.postTask(task);
    }

    @Override
    protected void onDestroy() {
        mThread.quit();
        super.onDestroy();
    }


    private void startNewActivity() {
        Intent intent = new Intent(this, StationsActivity.class);
        startActivity(intent);
        finish();
    }
}
