package ru.alexkulikov.smartvalentine;

import android.app.Application;

import com.vk.sdk.VKSdk;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
