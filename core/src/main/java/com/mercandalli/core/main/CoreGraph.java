package com.mercandalli.core.main;

import android.app.Application;

import com.mercandalli.core.clock.ClockManager;
import com.mercandalli.core.clock.ClockModule;
import com.mercandalli.core.gitlab.GitLabManager;
import com.mercandalli.core.gitlab.GitLabModule;
import com.mercandalli.core.main_thread.MainThreadModule;
import com.mercandalli.core.main_thread.MainThreadPost;
import com.mercandalli.core.network.NetworkModule;
import com.mercandalli.core.notification.NotificationManager;
import com.mercandalli.core.notification.NotificationModule;
import com.mercandalli.core.schedule.ScheduleManager;
import com.mercandalli.core.schedule.SchedulerModule;
import com.mercandalli.core.train.TrainManager;
import com.mercandalli.core.train.TrainModule;
import com.mercandalli.core.weather.WeatherManager;
import com.mercandalli.core.weather.WeatherModule;

import okhttp3.OkHttpClient;

public class CoreGraph {

    private static CoreGraph instance;

    public static CoreGraph get() {
        if (instance == null) {
            throw new IllegalStateException("Init first");
        }
        return instance;
    }


    public static CoreGraph init(Application application) {
        if (instance == null) {
            instance = new CoreGraph(application);
        }
        return instance;
    }

    private final GitLabManager gitLabManager;
    private final ClockManager clockManager;
    private final WeatherManager weatherManager;
    private final TrainManager trainManager;
    private final ScheduleManager scheduleManager;
    private final NotificationManager notificationManager;

    private CoreGraph(Application application) {
        NetworkModule networkModule = new NetworkModule();
        OkHttpClient okHttpClient = networkModule.provideOkHttpClient();
        MainThreadModule mainThreadModule = new MainThreadModule();
        MainThreadPost mainThreadPost = mainThreadModule.provideMainThreadPost();
        GitLabModule gitLabModule = new GitLabModule(
                okHttpClient,
                mainThreadPost);
        gitLabManager = gitLabModule.provideGitLabManager();
        ClockModule clockModule = new ClockModule();
        clockManager = clockModule.provideClockManager(mainThreadPost);
        WeatherModule weatherModule = new WeatherModule(okHttpClient, mainThreadPost);
        weatherManager = weatherModule.provideWeatherManager();
        TrainModule trainModule = new TrainModule(okHttpClient, mainThreadPost);
        trainManager = trainModule.provideTrainManager();
        NotificationModule notificationModule = new NotificationModule();
        notificationManager = notificationModule.provideNotificationManager(application);
        SchedulerModule schedulerModule = new SchedulerModule(application);
        scheduleManager = schedulerModule.provideScheduleManager();
        scheduleManager.initialize();
    }

    public GitLabManager getGitLabManager() {
        return gitLabManager;
    }

    public ClockManager provideClockManager() {
        return clockManager;
    }

    public WeatherManager provideWeatherManager() {
        return weatherManager;
    }

    public TrainManager provideTrainManager() {
        return trainManager;
    }

    public ScheduleManager provideScheduleManager() {
        return scheduleManager;
    }

    public NotificationManager provideNotificationManager() {
        return notificationManager;
    }
}
