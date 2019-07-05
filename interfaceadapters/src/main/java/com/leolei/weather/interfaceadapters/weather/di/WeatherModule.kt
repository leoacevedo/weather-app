package com.leolei.weather.interfaceadapters.weather.di

import com.leolei.weather.interfaceadapters.weather.WeatherRepository
import com.leolei.weather.interfaceadapters.weather.rest.RestService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import javax.inject.Singleton

@Module
class WeatherModule {
    @Provides
    @Singleton
    fun provideHttpClient(timeoutMillis: Long): OkHttpClient {
        val duration = Duration.ofMillis(timeoutMillis)
        return OkHttpClient.Builder()
            .callTimeout(duration)
            .connectTimeout(duration)
            .readTimeout(duration)
            .writeTimeout(duration)
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()

                chain.proceed(newRequest.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRestService(
        baseUrl: String,
        httpClient: OkHttpClient
    ): RestService {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(restService: RestService): WeatherRepository {
        return WeatherRepository(restService)
    }
}