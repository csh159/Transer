package com.scott.example.app;

import android.app.Application;

import com.scott.transer.event.TaskEventBus;

/**
 * Created by shijiale on 2017/12/16.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TaskEventBus.init(getApplicationContext());
    }
}
