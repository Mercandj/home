package com.mercandalli.core.clock;

import com.mercandalli.core.main_thread.MainThreadPost;

public class ClockModule {

    public ClockManager provideClockManager(MainThreadPost mainThreadPost) {
        return new ClockManagerImpl(mainThreadPost);
    }
}
