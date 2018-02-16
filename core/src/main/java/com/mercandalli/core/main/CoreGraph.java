package com.mercandalli.core.main;

import com.mercandalli.core.clock.ClockManager;
import com.mercandalli.core.clock.ClockModule;
import com.mercandalli.core.main_thread.MainThreadModule;
import com.mercandalli.core.main_thread.MainThreadPost;
import com.mercandalli.core.network.NetworkModule;
import com.mercandalli.core.weather.WeatherManager;
import com.mercandalli.core.weather.WeatherModule;

import okhttp3.OkHttpClient;

public class CoreGraph {

    private static CoreGraph instance;

    public static CoreGraph get() {
        if (instance == null) {
            instance = new CoreGraph();
        }
        return instance;
    }

    private final ClockManager clockManager;
    private final WeatherManager weatherManager;

    private CoreGraph() {
        NetworkModule networkModule = new NetworkModule();
        OkHttpClient okHttpClient = networkModule.provideOkHttpClient();
        MainThreadModule mainThreadModule = new MainThreadModule();
        MainThreadPost mainThreadPost = mainThreadModule.provideMainThreadPost();
        ClockModule clockModule = new ClockModule();
        clockManager = clockModule.provideClockManager(mainThreadPost);
        WeatherModule weatherModule = new WeatherModule(okHttpClient, mainThreadPost);
        weatherManager = weatherModule.provideWeatherManager();
    }

    public ClockManager provideClockManager() {
        return clockManager;
    }

    public WeatherManager provideWeatherManager() {
        return weatherManager;
    }
}
