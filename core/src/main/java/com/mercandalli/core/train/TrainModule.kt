package com.mercandalli.core.train

import com.mercandalli.core.main_thread.MainThreadPost
import okhttp3.OkHttpClient

class TrainModule(
        private val okHttpClient: OkHttpClient,
        private val mainThreadPost: MainThreadPost) {

    fun provideTrainManager(): TrainManager {
        return TrainManagerImpl(
                provideTrainApi(),
                mainThreadPost)
    }

    private fun provideTrainApi(): TrainApi {
        return TrainApiOkHttp(okHttpClient)
    }

}
