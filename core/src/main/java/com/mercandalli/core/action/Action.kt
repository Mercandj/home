package com.mercandalli.core.action

import android.support.annotation.StringDef

data class Action(
        @ActionType val type: String) {

    companion object {

        @StringDef(
                ACTION_TRAFFIC_NOTIFICATION)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ActionType

        const val ACTION_TRAFFIC_NOTIFICATION = "ACTION_TRAFFIC_NOTIFICATION"

    }


}
