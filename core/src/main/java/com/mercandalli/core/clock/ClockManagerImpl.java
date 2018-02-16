package com.mercandalli.core.clock;

import com.mercandalli.core.main_thread.MainThreadPost;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A manager to deal with Thread.
 */
/* package */
class ClockManagerImpl implements ClockManager {

    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final AtomicBoolean hasToStop = new AtomicBoolean(false);
    private final AtomicInteger msPerFrame = new AtomicInteger();
    private final List<OnClockTickListener> listeners = new ArrayList<>();

    private final MainThreadPost mainThreadPost;

    /* package */ ClockManagerImpl(MainThreadPost mainThreadPost) {
        this.mainThreadPost = mainThreadPost;
    }

    @Override
    public void start(float fps) {
        if (isStarted.get()) {
            if (hasToStop.get()) {
                hasToStop.set(false);
            }
            return;
        }
        Thread thread = createNewThread();
        isStarted.set(true);
        msPerFrame.set((int) (1_000f / fps));
        thread.start();
    }

    @Override
    public void stop() {
        hasToStop.set(true);
    }

    @Override
    public void addOnClockTickListener(final OnClockTickListener listener) {
        if (listener == null || listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    @Override
    public void removeOnClockTickListener(final OnClockTickListener listener) {
        listeners.remove(listener);
    }

    private void notifyOnThreadTickListeners() {
        if (!mainThreadPost.isOnMainThread()) {
            mainThreadPost.post(new Runnable() {
                @Override
                public void run() {
                    notifyOnThreadTickListeners();
                }
            });
            return;
        }
        for (final OnClockTickListener listener : listeners) {
            listener.onClockTick();
        }
    }

    private Thread createNewThread() {
        Thread thread = new Thread(new Runnable() {

            private long lastFrame;

            @Override
            public void run() {
                while (!hasToStop.get()) {
                    final long now = System.currentTimeMillis();
                    final int delay = msPerFrame.get();
                    if (now - lastFrame >= delay) {
                        lastFrame = now;
                        notifyOnThreadTickListeners();
                    }
                    final long timeToSleep = delay - (System.currentTimeMillis() - now);
                    if (timeToSleep > 5) {
                        try {
                            Thread.sleep(timeToSleep);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                isStarted.set(false);
                hasToStop.set(false);
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }
}