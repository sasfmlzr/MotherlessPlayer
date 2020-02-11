package com.sasfmlzr.motherless.di.core

import com.sasfmlzr.motherless.AppConfig
import com.sasfmlzr.motherless.data.api.MotherlessApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ThirdModule {

    @Provides
    @Singleton
    fun dvachRetrofitService(okHttpClient: OkHttpClient): MotherlessApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(AppConfig.MOTHERLESS_URL)
            .client(okHttpClient)
            .addConverterFactory(getOwnerContactConverterFactory())
            .build()
        return retrofit.create(MotherlessApi::class.java)
    }


    @Provides
    @Singleton
    fun okHttp() = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()
}
