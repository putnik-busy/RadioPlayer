package com.justApp.RadioPlayer.splash;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * @author Sergey Rodionov
 */

public class CustomHandlerThread extends HandlerThread {

    private Handler mHandler;

    public CustomHandlerThread(String name) {
        super(name);
    }

    public void postTask(Runnable task) {
        mHandler.post(task);
    }

    public void prepareHandler() {
        mHandler = new Handler();
    }
}
