package com.mercandalli.android.home.train;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.mercandalli.core.train.TrainTraffic;

import org.jetbrains.annotations.NotNull;

public class TrainTrafficCardView extends CardView {
    public TrainTrafficCardView(@NonNull final Context context) {
        super(context);
    }

    public TrainTrafficCardView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public TrainTrafficCardView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnTrainTrafficClickListener(@NotNull final OnTrainTrafficClickListener onPlaylistClickListener) {

    }

    public void setTrainTraffic(@NotNull final TrainTraffic trainTraffic) {

    }

    public interface OnTrainTrafficClickListener {


    }
}
