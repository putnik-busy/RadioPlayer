package com.justApp.RadioPlayer;

import android.app.Application;

/**
 * @author Sergey Rodionov
 */

public class App extends Application {

    private static App INSTANCE;

    public synchronized static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
