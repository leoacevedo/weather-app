package com.leolei.weather.interfaceadapters.weather.rest

import retrofit2.http.GET
import retrofit2.http.Query

interface RestService {
    @GET("data/2.5/weather")
    suspend fun getForecast(@Query("q") city: String): RestForecast
}
