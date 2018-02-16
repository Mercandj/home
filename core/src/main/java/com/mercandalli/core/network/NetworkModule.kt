package com.mercandalli.core.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class NetworkModule {

    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
        return builder.build()
    }
}
