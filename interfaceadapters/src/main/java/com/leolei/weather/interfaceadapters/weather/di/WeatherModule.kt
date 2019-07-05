package com.leolei.weather.interfaceadapters.weather.di

import com.leolei.weather.interfaceadapters.weather.WeatherRepository
import com.leolei.weather.interfaceadapters.weather.rest.RestService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.SocketFactory

@Module
class WeatherModule {
    @Provides
    @Singleton
    fun provideHttpClient(
        timeoutMillis: Int,
        @AppId weatherAppId: String,
        @Units defaultUnits: String
    ): OkHttpClient {
        val timeoutLong = timeoutMillis.toLong()

        return OkHttpClient.Builder()
            .callTimeout(timeoutLong, TimeUnit.MILLISECONDS)
            .connectTimeout(timeoutLong, TimeUnit.MILLISECONDS)
            .readTimeout(timeoutLong, TimeUnit.MILLISECONDS)
            .writeTimeout(timeoutLong, TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .url(
                        request.url().newBuilder()
                            .addQueryParameter("appid", weatherAppId)
                            .addQueryParameter("units", defaultUnits)
                            .build()
                    )
                    .build()

                chain.proceed(newRequest)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRestService(@BaseUrl  baseUrl: String, httpClient: OkHttpClient): RestService {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(baseUrl)
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