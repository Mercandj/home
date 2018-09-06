package com.mercandalli.android.home.main;

import com.google.android.things.update.UpdateManager;
import com.mercandalli.core.main.CoreGraph;

import androidx.multidex.MultiDexApplication;

public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        CoreGraph.init(this);

        UpdateManager manager = UpdateManager.getInstance();
        manager.setChannel("dev-channel");
    }
}
