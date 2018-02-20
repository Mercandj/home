package com.mercandalli.android.home.main;

import android.app.Application;

import com.mercandalli.core.main.CoreGraph;

/**
 * Created by Jonathan on 20-Feb-18.
 */

public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        CoreGraph.init(this);
    }
}
