package com.mercandalli.core.clock;

/**
 * A manager to deal with Thread.
 */
public interface ClockManager {

    /**
     * Start the {@link Thread}.
     *
     * @param fps The frequency. Example: fps == 1 will notify the {@link OnClockTickListener}
     *            one time per second.
     */
    void start(final float fps);

    /**
     * Stop the {@link Thread}.
     */
    void stop();

    void addOnClockTickListener(OnClockTickListener listener);

    void removeOnClockTickListener(OnClockTickListener listener);

    interface OnClockTickListener {

        void onClockTick();
    }
}