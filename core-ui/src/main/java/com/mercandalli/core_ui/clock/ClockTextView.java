package com.mercandalli.core_ui.clock;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.mercandalli.core.clock.ClockBasicDate;
import com.mercandalli.core.clock.ClockManager;
import com.mercandalli.core.clock.ClockUtils;
import com.mercandalli.core.main.CoreGraph;

import java.util.Locale;

public class ClockTextView extends AppCompatTextView implements
        ClockManager.OnClockTickListener {

    private static final String HOUR_STRING_TEMPLATE = "%02d : %02d : %02d";
    private ClockManager clockManager;

    public ClockTextView(Context context) {
        super(context);
        init();
    }

    public ClockTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            return;
        }
        internalResume();
        attachToClockManager();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (isInEditMode()) {
            super.onDetachedFromWindow();
            return;
        }
        internalPause();
        detachFromClockManager();
        super.onDetachedFromWindow();
    }

    @Override
    public void onClockTick() {
        updateFromBasicDate();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        clockManager = CoreGraph.get().provideClockManager();
    }

    public void updateFromBasicDate() {
        setBasicDate(ClockUtils.getCurrentBasicDate());
    }

    private void setBasicDate(ClockBasicDate clockBasicDate) {
        setText(String.format(
                Locale.US,
                HOUR_STRING_TEMPLATE,
                clockBasicDate.getHourOfTheDay(),
                clockBasicDate.getMinute(),
                clockBasicDate.getSecond()));
    }

    private void attachToClockManager() {
        setBasicDate(ClockUtils.getCurrentBasicDate());
        clockManager.addOnClockTickListener(this);
        clockManager.start(1);
    }

    private void detachFromClockManager() {
        clockManager.stop();
        clockManager.removeOnClockTickListener(this);
    }

    private void internalResume() {
        if (!isInEditMode()) {
            attachToClockManager();
        }
    }

    private void internalPause() {
        if (!isInEditMode()) {
            detachFromClockManager();
        }
    }
}
